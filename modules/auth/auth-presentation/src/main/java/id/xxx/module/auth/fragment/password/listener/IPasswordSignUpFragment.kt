package id.xxx.module.auth.fragment.password.listener

interface IPasswordSignUpFragment {

    sealed interface Action {

        data class ClickSignUp(
            val email: String,
            val password: String
        ) : Action

        data class ClickSignIn(
            val email: String,
        ) : Action

        data object ClickSignUpWithPhone : Action

        data class ClickSignInWithGoogle(
            val token: String
        ) : Action
    }

    fun onAction(action: Action)
}