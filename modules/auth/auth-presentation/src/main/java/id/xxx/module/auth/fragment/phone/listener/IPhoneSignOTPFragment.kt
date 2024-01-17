package id.xxx.module.auth.fragment.phone.listener

interface IPhoneSignOTPFragment {

    sealed interface Action {

        data class ClickNext(
            val otp: String,
            val sessionInfo: String,
        ) : Action
    }

    fun onAction(action: Action)
}