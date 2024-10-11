package id.xxx.module.auth.repository

import id.xxx.module.auth.model.LookupModel
import id.xxx.module.auth.model.PasswordResetModel
import id.xxx.module.auth.model.PhoneVerificationModel
import id.xxx.module.auth.model.SignModel
import id.xxx.module.auth.model.VerifyEmailModel
import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.model.parms.SignType
import id.xxx.module.auth.model.parms.UpdateType
import id.xxx.module.auth.repository.ktx.getBoolean
import id.xxx.module.auth.repository.ktx.getString
import id.xxx.module.auth.repository.source.remote.auth.email.AuthDataSourceRemote
import id.xxx.module.auth.repository.source.remote.response.Header
import id.xxx.module.auth.repository.source.remote.response.Response
import id.xxx.module.common.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.concurrent.atomic.AtomicLong

class AuthRepositoryImpl private constructor(
    private val remoteDataSource: AuthDataSourceRemote,
) : AuthRepository {

    companion object {

        @Volatile
        private var _instance: AuthRepository? = null

        fun getInstance() = _instance ?: synchronized(this) {
            _instance ?: AuthRepositoryImpl(
                AuthDataSourceRemote.getInstance(),
            ).also { _instance = it }
        }
    }

    override fun sign(type: SignType) = asResources(
        request = { remoteDataSource.sign(type) },
        result = { header, response ->
            val j = JSONObject(response)
            SignModel(
                uid = j.getString("localId"),
                token = j.getString("idToken"),
                refreshToken = j.getString("refreshToken"),
                expiresInTimeMillis = (j.getLong("expiresIn") * 1000) + header.date,
                isNewUser = when (type) {
                    is SignType.PasswordUp -> true
                    else -> j.getBoolean("isNewUser", false)
                }
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
            emit(Resources.Loading())
            val response = request.invoke()
            val header = response.header
            val count = AtomicLong(0)
            val progress = Resources.Loading.Progress(count = count, length = header.contentLength)
            val loading = Resources.Loading(progress)
            emit(loading)
            val data = response.body
            val out = ByteArrayOutputStream()
            val buffersSize = 1024 * 512
            val buffers = ByteArray(buffersSize)
            while (true) {
                val readCount = data.read(buffers, 0, buffers.size)
                if (readCount != -1) {
                    val bytes = if (readCount == buffersSize) buffers else buffers.copyOf(readCount)
                    out.write(bytes, 0, bytes.size)
                } else {
                    break
                }
                count.set(out.size().toLong())
                emit(loading)
            }

            val outString = out.toString()
            if (header.code in 200..299) {
                emit(Resources.Success(result.invoke(header, outString)))
            } else {
                val error = JSONObject(outString).getJSONObject("error")
                val message = error.getString("message", "Error")
//                    val code = error.getInt("code")
                throw Throwable(message)
            }
        } catch (e: Throwable) {
            emit(Resources.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
}