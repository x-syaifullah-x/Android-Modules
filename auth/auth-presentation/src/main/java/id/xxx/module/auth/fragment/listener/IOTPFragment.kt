package id.xxx.module.auth.fragment.listener

interface IOTPFragment {

    sealed interface Action {

        data class ClickNext(
            val isNewUser: Boolean,
            val otp: String,
            val sessionInfo: String,
        ) : Action
    }

    fun onAction(action: Action)
}