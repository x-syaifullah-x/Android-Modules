package id.xxx.module.auth.domain.model

sealed interface TypeSignIn {

    data class Password(
        val email: String,
        val password: String
    ) : TypeSignIn
}