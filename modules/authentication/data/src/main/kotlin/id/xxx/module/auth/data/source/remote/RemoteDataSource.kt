package id.xxx.module.auth.data.source.remote

import id.xxx.module.auth.data.source.remote.helpers.MyFirebase
import id.xxx.module.auth.data.source.remote.model.SignUpResult
import id.xxx.module.auth.domain.model.TypeSignIn
import id.xxx.module.auth.domain.model.TypeSignUp
import kotlinx.coroutines.tasks.await

class RemoteDataSource private constructor() {

    companion object {

        @Volatile
        private var _sInstance: RemoteDataSource? = null

        fun getInstance() = _sInstance ?: synchronized(this) {
            _sInstance ?: RemoteDataSource().also { _sInstance = it }
        }
    }

    internal suspend fun signUp(type: TypeSignUp): SignUpResult {
        val res = when (type) {
            is TypeSignUp.Password -> {
                val email = type.email
                val password = type.password
                val auth = MyFirebase.getFirebaseAuth()
                val result = auth.createUserWithEmailAndPassword(
                    email, password
                ).await()
                val user = result.user
                if (user == null)
                    throw NullPointerException()
                else
                    SignUpResult(uid = user.uid, isNewUser = true)
            }
        }
        return res
    }

    internal suspend fun signIn(type: TypeSignIn) = when (type) {
        is TypeSignIn.Password -> {
            val auth = MyFirebase.getFirebaseAuth()
            val email = type.email
            val password = type.password
            val authResult = auth.signInWithEmailAndPassword(
                email, password
            ).await()
            val user = authResult.user
            if (user == null)
                throw NullPointerException("user")
            SignUpResult(uid = user.uid, isNewUser = false)
        }
    }
}