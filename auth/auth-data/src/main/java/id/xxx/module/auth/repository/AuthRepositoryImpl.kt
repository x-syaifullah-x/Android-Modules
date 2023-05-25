package id.xxx.module.auth.repository

import id.xxx.module.auth.model.OobType
import id.xxx.module.auth.model.SignInType
import id.xxx.module.auth.model.SignUpType
import id.xxx.module.auth.model.UpdateType
import id.xxx.module.auth.model.User
import id.xxx.module.auth.model.VerificationCodeResult
import id.xxx.module.auth.repository.source.remote.auth.email.AuthEmailDataSourceRemote
import id.xxx.module.auth.repository.source.remote.response.Header
import id.xxx.module.auth.repository.source.remote.response.Response
import id.xxx.module.common.Resources
import id.xxx.module.io.ktx.read
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
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
        private var INSTANCE: AuthRepositoryImpl? = null

        fun getInstance(): AuthRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuthRepositoryImpl(AuthEmailDataSourceRemote.getInstance())
                    .also { INSTANCE = it }
            }
    }

    private var _user: User? = null

    override fun currentUser(): Flow<Resources<User>> {
        return flow {
            emit(Resources.Loading())
            delay(1000)
            val user = _user
            if (user != null)
                emit(Resources.Success(user))
            else
                emit(Resources.Failure(Throwable("user not login")))
        }
    }

    override fun signIn(type: SignInType) = asResources(
        request = { remoteDataSource.signIn(type) },
        result = { _, response ->
            val j = JSONObject(response)
            User(
                uid = j.getString("localId"),
//                token = j.getString("idToken"),
//                refreshToken = j.getString("refreshToken"),
//                expiresIn = (j.getLong("expiresIn") * 1000) + header.date
            )
        }
    )

    override fun signUp(type: SignUpType) = asResources(
        request = { remoteDataSource.signUp(type) },
        result = { _, response ->
            val j = JSONObject(response)
            User(
                uid = j.getString("localId"),
//                token = j.getString("idToken"),
//                refreshToken = j.getString("refreshToken"),
//                expiresIn = (j.getLong("expiresIn") * 1000) + header.date
            )
        }
    )

    override fun sendVerificationCode(
        phoneNumber: String,
        recaptchaResponse: String
    ) = asResources(
        request = { remoteDataSource.sendVerificationCode(phoneNumber, recaptchaResponse) },
        result = { _, response ->
            val j = JSONObject(response)
            val sessionInfo = j.getString("sessionInfo")
            VerificationCodeResult(sessionInfo = sessionInfo)
        }
    )

    override fun signOut(): Flow<Resources<Boolean>> {
        return flow {
            emit(Resources.Loading())
            delay(100)
            _user = null
            emit(Resources.Success(true))
        }
    }

    override fun sendOobCode(type: OobType) = asResources(
        request = { remoteDataSource.sendOobCode(type) },
        result = { _, response -> response }
    )

    override fun resetPassword(
        oobCode: String,
        newPassword: String
    ) = asResources(
        request = { remoteDataSource.resetPassword(oobCode, newPassword) },
        result = { _, response -> response }
    )

    override fun update(type: UpdateType) = asResources(
        request = { remoteDataSource.update(type) },
        result = { _, response -> response }
    )

    private fun <T> asResources(
        request: () -> Response<InputStream>,
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
                onError = { e -> throw e }
            )
        } catch (e: Throwable) {
            emit(Resources.Failure(e))
        }
    }.flowOn(Dispatchers.IO)
        .catch { emit(Resources.Failure(it)) }

    private fun JSONObject.getString(
        name: String, defaultValue: String
    ): String =
        try {
            getString(name)
        } catch (err: Throwable) {
            defaultValue
        }
}