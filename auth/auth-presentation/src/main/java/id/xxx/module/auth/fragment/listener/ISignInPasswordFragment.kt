package id.xxx.module.auth.fragment.listener

interface ISignInPasswordFragment {

    sealed interface Action {

        data class ClickForgetPassword(
            val email: String
        ) : Action

        data class ClickSignIn(
            val email: String,
            val password: String,
            val isRemember: Boolean
        ) : Action

        data class ClickSignUp(
            val email: String,
        ) : Action

        data class ClickSignInWithPhone(
            val email: String,
        ) : Action
    }

    fun onAction(action: Action)
}