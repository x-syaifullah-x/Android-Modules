package id.xxx.module.auth.domain.model

sealed interface TypeSign {

    data class InPassword(
        val email: String,
        val password: String
    ) : TypeSign

    data class UpPassword(
        val email: String,
        val password: String
    ) : TypeSign
}