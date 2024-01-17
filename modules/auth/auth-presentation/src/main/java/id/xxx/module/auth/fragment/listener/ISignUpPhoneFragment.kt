package id.xxx.module.auth.fragment.listener

interface ISignUpPhoneFragment {

    sealed interface Action {

        data class ClickNext(
            val phoneNumber: String,
            val recaptchaResponse: String,
        ) : Action

        data class ClickSignIn(
            val phoneNumber: String,
        ) : Action

        data class ClickSignUpWithEmail(
            val phoneNumber: String,
        ) : Action

        data class ClickSignUpWithGoogle(
            val token: String,
        ) : Action
    }

    fun onAction(action: Action)
}