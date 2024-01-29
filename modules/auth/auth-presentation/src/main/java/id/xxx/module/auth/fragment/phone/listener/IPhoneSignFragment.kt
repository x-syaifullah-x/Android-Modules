package id.xxx.module.auth.fragment.phone.listener

interface IPhoneSignFragment {

    sealed interface Action {

        data class ClickNext(
            val phoneNumber: String,
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