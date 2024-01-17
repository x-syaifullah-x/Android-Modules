package id.xxx.module.auth.model.parms

sealed interface SignType {

    data class CostumeToken(
        val token: String
    ) : SignType

    data class PasswordUp(
        val password: String,
        val data: UserData,
    ) : SignType

    data class PasswordIn(
        val email: String,
        val password: String,
    ) : SignType

    data class Phone(
        val sessionInfo: String,
        val otp: String,
    ) : SignType

    data class Google(
        val token: String,
    ) : SignType
}