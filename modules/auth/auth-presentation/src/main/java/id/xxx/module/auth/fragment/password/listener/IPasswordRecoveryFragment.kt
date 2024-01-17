package id.xxx.module.auth.fragment.password.listener

interface IPasswordRecoveryFragment {

    sealed interface Action {

        data class Next(
            val email: String,
        ) : Action

        data object Cancel : Action
    }

    fun onAction(action: Action)
}