package id.xxx.module.auth.model.parms

sealed interface SignInType {

    data class CostumeToken(
        val token: String
    ) : SignInType

    data class Password(
        val email: String,
        val password: String,
    ) : SignInType

    data class Phone(
        val sessionInfo: String, val otp: String
    ) : SignInType

    data class Google(
        val token: String,
    ) : SignInType
}