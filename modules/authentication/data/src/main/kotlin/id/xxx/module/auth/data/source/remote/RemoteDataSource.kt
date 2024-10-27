package id.xxx.module.auth.data.source.remote

import id.xxx.module.auth.data.source.remote.helpers.MyFirebase
import id.xxx.module.auth.data.source.remote.model.SignResult
import id.xxx.module.auth.domain.model.TypeSign
import kotlinx.coroutines.tasks.await

class RemoteDataSource private constructor() {

    companion object {

        @Volatile
        private var _sInstance: RemoteDataSource? = null

        fun getInstance() = _sInstance ?: synchronized(this) {
            _sInstance ?: RemoteDataSource().also { _sInstance = it }
        }
    }

    internal suspend fun sign(type: TypeSign): SignResult {
        val auth = MyFirebase.getFirebaseAuth()
        return when (type) {
            is TypeSign.UpPassword -> {
                val email = type.email
                val password = type.password
                val result = auth.createUserWithEmailAndPassword(
                    email, password
                ).await()
                val user = result.user
                if (user == null)
                    throw NullPointerException()
                else
                    SignResult(uid = user.uid, isNewUser = true)
            }

            is TypeSign.InPassword -> {
                val email = type.email
                val password = type.password
                val authResult = auth.signInWithEmailAndPassword(
                    email, password
                ).await()
                val user = authResult.user
                if (user == null)
                    throw NullPointerException("user")
                SignResult(uid = user.uid, isNewUser = false)
            }
        }
    }
}