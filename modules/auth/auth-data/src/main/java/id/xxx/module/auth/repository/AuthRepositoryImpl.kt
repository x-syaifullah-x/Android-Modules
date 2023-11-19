package id.xxx.module.auth.repository

import id.xxx.module.auth.model.LookupModel
import id.xxx.module.auth.model.PasswordResetModel
import id.xxx.module.auth.model.PhoneVerificationModel
import id.xxx.module.auth.model.SignModel
import id.xxx.module.auth.model.VerifyEmailModel
import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.model.parms.SignInType
import id.xxx.module.auth.model.parms.SignUpType
import id.xxx.module.auth.model.parms.UpdateType
import id.xxx.module.auth.repository.ktx.getString
import id.xxx.module.auth.repository.source.remote.auth.email.AuthEmailDataSourceRemote
import id.xxx.module.auth.repository.source.remote.response.Header
import id.xxx.module.auth.repository.source.remote.response.Response
import id.xxx.module.common.Resources
import id.xxx.module.io.ktx.read
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.concurrent.atomic.AtomicLong

class AuthRepositoryImpl private constructor(
    private val remoteDataSource: AuthEmailDataSourceRemote,
) : AuthRepository {

    companion object {
        @Volatile
        private var INSTANCE: AuthRepository? = null

        fun getInstance() = INSTANCE ?: synchronized(this) {
            INSTANCE ?: AuthRepositoryImpl(
                AuthEmailDataSourceRemote.getInstance(),
            ).also { INSTANCE = it }
        }
    }

    override fun signIn(type: SignInType) = asResources(
        request = { remoteDataSource.signIn(type) },
        result = { header, response ->
            val j = JSONObject(response)
            SignModel(
                uid = j.getString("localId"),
                token = j.getString("idToken"),
                refreshToken = j.getString("refreshToken"),
                expiresIn = (j.getLong("expiresIn") * 1000) + header.date,
            )
        },
    )

    override fun signUp(type: SignUpType) = asResources(
        request = { remoteDataSource.signUp(type) },
        result = { header, response ->
            val j = JSONObject(response)
            SignModel(
                uid = j.getString("localId"),
                token = j.getString("idToken"),
                refreshToken = j.getString("refreshToken"),
                expiresIn = (j.getLong("expiresIn") * 1000) + header.date,
            )
        },
    )

    override fun sendCode(code: Code.PhoneVerification) = asResources(
        request = { remoteDataSource.sendOobCode(code) },
        result = { _, response ->
            val j = JSONObject(response)
            val sessionInfo = j.getString("sessionInfo")
            PhoneVerificationModel(sessionInfo = sessionInfo)
        },
    )

    override fun sendCode(code: Code.PasswordReset) = asResources(
        request = { remoteDataSource.sendOobCode(code) },
        result = { _, response ->
            val j = JSONObject(response)
            PasswordResetModel(
                kind = j.getString("kind", ""), email = j.getString("email", "")
            )
        },
    )

    override fun sendCode(code: Code.VerifyEmail) = asResources(
        request = { remoteDataSource.sendOobCode(code) },
        result = { _, response ->
            val j = JSONObject(response)
            VerifyEmailModel(
                kind = j.getString("kind", ""),
                email = j.getString("email", ""),
            )
        },
    )

    override fun lookup(idToken: String) = asResources(
        request = { remoteDataSource.lookup(idToken) },
        result = { _, response ->
            val j = JSONObject(response)
            val users = j.getJSONArray("users")
            val user = users.getJSONObject(0)
            val isEmailVerify = user.getBoolean("emailVerified")
            LookupModel(
                isEmailVerify = isEmailVerify
            )
        },
    )

    override fun update(type: UpdateType) = asResources(
        request = { remoteDataSource.update(type) },
        result = { _, response -> response },
    )

    private fun <T> asResources(
        request: suspend () -> Response<InputStream>,
        result: (header: Header, response: String) -> T
    ) = flow {
        try {
            val countAtomic = AtomicLong(0)
            val lengthAtomic = AtomicLong(0)
            val progress = Resources.Loading.Progress(countAtomic, lengthAtomic)
            val loading = Resources.Loading(progress)
            emit(loading)
            val response = request.invoke()
            val header = response.header
            lengthAtomic.set(header.contentLength)
            val data = response.body
            val out = ByteArrayOutputStream()
            data.read(
                onRead = { bytes ->
                    val size = bytes.size
                    out.write(bytes, 0, size)
                    if (lengthAtomic.get() != -1L) {
                        countAtomic.set(countAtomic.get() + size)
                        emit(loading)
                    }
                },
                onReadComplete = {
                    val outString = out.toString()
                    if (header.code in 200..299) {
                        emit(Resources.Success(result.invoke(header, outString)))
                    } else {
                        val error = JSONObject(outString).getJSONObject("error")
                        val message = error.getString("message", "Error")
//                    val code = error.getInt("code")
                        throw Throwable(message)
                    }
                },
                onError = { e -> throw e },
            )
        } catch (e: Throwable) {
            emit(Resources.Failure(e))
        }
    }.flowOn(Dispatchers.IO).catch { emit(Resources.Failure(it)) }
}