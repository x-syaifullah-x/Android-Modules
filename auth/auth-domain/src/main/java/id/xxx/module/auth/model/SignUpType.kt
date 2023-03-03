package id.xxx.module.auth.model

sealed interface SignUpType {

    data class Password(
        val password: String,
        val data: UserData,
    ) : SignUpType

    data class Phone(
        val sessionInfo: String,
        val otp: String,
        val data: UserData? = null
    ) : SignUpType
}