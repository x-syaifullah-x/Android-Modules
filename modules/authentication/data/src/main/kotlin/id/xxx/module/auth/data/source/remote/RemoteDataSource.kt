package id.xxx.module.auth.data.source.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider
import id.xxx.module.auth.data.source.remote.model.SignResult
import id.xxx.module.auth.domain.model.AuthenticationType
import kotlinx.coroutines.tasks.await

class RemoteDataSource private constructor() {

    companion object {

        @Volatile
        private var _sInstance: RemoteDataSource? = null

        fun getInstance() = _sInstance ?: synchronized(this) {
            _sInstance ?: RemoteDataSource().also { _sInstance = it }
        }
    }

    internal suspend fun sign(type: AuthenticationType): SignResult {
        val auth = FirebaseAuth.getInstance()
        val authResult = when (type) {
            is AuthenticationType.Google -> {
                auth.signInWithCredential(GoogleAuthProvider.getCredential(type.idToken, null))
            }

            is AuthenticationType.Phone -> {
                auth.signInWithCredential(
                    PhoneAuthProvider.getCredential(type.verificationId, type.code)
                )
            }

            is AuthenticationType.Password -> {
                val email = type.email
                val password = type.password
                if (type.type == AuthenticationType.Password.Type.UP)
                    auth.createUserWithEmailAndPassword(email, password)
                else
                    auth.signInWithEmailAndPassword(email, password)
            }
        }.await()
        val user = authResult.user
            ?: throw NullPointerException("user")
        val token = user.getIdToken(false).await()
        val claimsFirebase = token.claims["firebase"] as Map<*, *>
        val signProvider = "${claimsFirebase["sign_in_provider"]}"
        val additionalUserInfo = authResult.additionalUserInfo
        val isNewUser = additionalUserInfo?.isNewUser
            ?: throw NullPointerException("isNewUser")
        return SignResult(uid = user.uid, isNewUser = isNewUser, signProvider = signProvider)
    }
}