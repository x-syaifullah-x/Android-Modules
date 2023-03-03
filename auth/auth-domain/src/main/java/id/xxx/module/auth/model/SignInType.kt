package id.xxx.module.auth.model

sealed interface SignInType {

    data class CostumeToken(
        val token: String
    ) : SignInType

    data class Password(
        val email: String,
        val password: String,
        val isRemember: Boolean = true,
    ) : SignInType

    data class Phone(
        val sessionInfo: String,
        val otp: String
    ) : SignInType
}