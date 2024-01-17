package id.xxx.module.auth.fragment.listener

interface ISignInPhoneFragment {

    sealed interface Action {

        data class ClickNext(
            val phoneNumber: String,
            val recaptchaResponse: String,
        ) : Action

        data class ClickSignInWithEmail(
            val phoneNumber: String,
        ) : Action

        data class ClickSignInWithGoogle(
            val token: String,
        ) : Action
    }

    fun onAction(action: Action)
}