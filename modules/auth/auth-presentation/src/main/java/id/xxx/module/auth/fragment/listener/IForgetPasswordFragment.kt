package id.xxx.module.auth.fragment.listener

interface IForgetPasswordFragment {

    sealed interface Action {

        data class Next(
            val email: String,
        ) : Action

        data object Cancel : Action
    }

    fun onAction(action: Action)
}