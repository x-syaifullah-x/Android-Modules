package id.xxx.module.auth.domain.model

import java.io.Serializable

sealed interface AuthenticationType : Serializable {

    data class Password(
        val email: String,
        val password: String,
        val type: Type = Type.IN
    ) : AuthenticationType {
        enum class Type { IN, UP }
    }

    data class Phone(
        val verificationId: String,
        val code: String,
    ) : AuthenticationType

    data class Google(
        val idToken: String,
    ) : AuthenticationType
}