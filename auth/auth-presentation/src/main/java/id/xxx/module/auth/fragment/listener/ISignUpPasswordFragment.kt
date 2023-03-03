package id.xxx.module.auth.fragment.listener

interface ISignUpPasswordFragment {

    sealed interface Action {

        data class ClickSignUp(
            val email: String,
            val password: String
        ) : Action

        data class ClickSignIn(
            val email: String,
        ) : Action

        data class ClickSignUpWithPhone(
            val email: String,
        ) : Action
    }

    fun onAction(action: Action)
}