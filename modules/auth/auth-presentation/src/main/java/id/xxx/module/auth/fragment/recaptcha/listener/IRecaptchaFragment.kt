package id.xxx.module.auth.fragment.recaptcha.listener

interface IRecaptchaFragment {

    sealed interface Action {

        data class Error(
            val err: Throwable,
        ) : Action

        data class Success(
            val phoneNumber: String,
            val response: String,
        ) : Action
    }

    fun onAction(action: Action)
}