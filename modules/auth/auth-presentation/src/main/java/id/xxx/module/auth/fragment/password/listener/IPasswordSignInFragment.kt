package id.xxx.module.auth.fragment.password.listener

interface IPasswordSignInFragment {

    sealed interface Action {

        data class ClickForgetPassword(
            val email: String
        ) : Action

        data class ClickSignIn(
            val email: String,
            val password: String,
        ) : Action

        data class ClickSignUp(
            val email: String,
        ) : Action

        data class ClickContinueWithPhone(
            val email: String,
        ) : Action

        data class ClickContinueWithGoogle(
            val token: String
        ) : Action
    }

    fun onAction(action: Action)
}