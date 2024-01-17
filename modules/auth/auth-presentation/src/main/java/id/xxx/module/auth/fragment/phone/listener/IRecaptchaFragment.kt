package id.xxx.module.auth.fragment.phone.listener

interface IRecaptchaFragment {

    sealed interface Action {

        data class Error(
            val err: Throwable
        ) : Action

        data class Success(
            val response: String,
            val phoneNumber: String
        ) : Action
    }

    fun onAction(action: Action)
}