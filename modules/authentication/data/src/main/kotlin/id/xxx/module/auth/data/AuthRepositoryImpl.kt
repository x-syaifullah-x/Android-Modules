package id.xxx.module.auth.data

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import id.xxx.module.auth.data.source.local.LocalDataSource
import id.xxx.module.auth.data.source.local.entity.UserEntity
import id.xxx.module.auth.data.source.remote.RemoteDataSource
import id.xxx.module.auth.domain.exception.AuthInvalidCredentialsException
import id.xxx.module.auth.domain.exception.AuthNetworkException
import id.xxx.module.auth.domain.exception.AuthUserCollisionException
import id.xxx.module.auth.domain.model.SignModel
import id.xxx.module.auth.domain.model.TypeSignIn
import id.xxx.module.auth.domain.model.TypeSignUp
import id.xxx.module.auth.domain.model.UserModel
import id.xxx.module.auth.domain.repository.AuthRepository
import id.xxx.module.common.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class AuthRepositoryImpl private constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource
) : AuthRepository {

    companion object {

        @Volatile
        private var _sInstance: AuthRepositoryImpl? = null

        fun getInstance(
            local: LocalDataSource,
            remote: RemoteDataSource
        ) = _sInstance ?: synchronized(this) {
            _sInstance ?: AuthRepositoryImpl(
                local = local, remote = remote
            ).also { _sInstance = it }
        }
    }

    override fun signUp(type: TypeSignUp): Flow<Resources<SignModel>> = flow {
        emit(Resources.Loading())
        val signUpResult = remote.signUp(type)
        val uid = signUpResult.uid
        val isNewUser = signUpResult.isNewUser
        val entity = UserEntity(uid = signUpResult.uid)
        val isSaveToLocal = local.save(entity)
        val signModel = SignModel(uid = uid, isNewUser = isNewUser)
        if (isSaveToLocal) {
            emit(Resources.Success(signModel))
            return@flow
        }
        logInfo(AuthRepositoryImpl::signUp.name, "isSaveToLocal: false")
        emit(Resources.Success(signModel))
    }.flowOn(Dispatchers.IO)
        .catch {
            when (it) {
                is FirebaseNetworkException ->
                    emit(Resources.Failure(AuthNetworkException(it.message)))

                is FirebaseAuthUserCollisionException ->
                    emit(Resources.Failure(AuthUserCollisionException(it.message)))

                else -> emit(Resources.Failure(it))
            }
        }

    override fun signIn(type: TypeSignIn): Flow<Resources<SignModel>> = flow {
        emit(Resources.Loading())
        val signResult = remote.signIn(type)
        val uid = signResult.uid
        val isNewUser = signResult.isNewUser
        val userEntity = UserEntity(uid = uid, isLoggedIn = true)
        val isSaveToLocal = local.save(userEntity)
        val signModel = SignModel(uid = uid, isNewUser = isNewUser)
        if (isSaveToLocal) {
            emit(Resources.Success(signModel))
            return@flow
        }
        logInfo(AuthRepositoryImpl::signIn.name, "isSaveToLocal: false")
        emit(Resources.Success(signModel))
    }.flowOn(Dispatchers.IO)
        .catch {
            when (it) {
                is FirebaseNetworkException ->
                    emit(Resources.Failure(AuthNetworkException(it.message)))

                is FirebaseAuthInvalidCredentialsException ->
                    emit(Resources.Failure(AuthInvalidCredentialsException(it.message)))

                else -> emit(Resources.Failure(it))
            }
        }

    override fun getCurrentUser(): Flow<Resources<UserModel?>> = callbackFlow {
        trySend(Resources.Loading())
        val job = launch {
            local.getUserLoggedIn().collect {
                val res =
                    if (it == null)
                        null
                    else
                        UserModel(uid = it.uid)
                res
                trySend(Resources.Success(res))
            }
        }
        awaitClose { job.cancel() }
    }.flowOn(Dispatchers.IO)
        .catch { emit(Resources.Failure(it)) }

    fun logInfo(funName: String, message: String) {
        val tag = "${AuthRepositoryImpl::class.java.simpleName}=>$funName"
        Log.i(tag, message)
    }
}