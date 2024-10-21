package id.xxx.module.auth.domain.model

sealed interface TypeSignUp {

    data class Password(
        val email: String,
        val password: String
    ) : TypeSignUp
}