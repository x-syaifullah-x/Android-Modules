package id.xxx.module.auth.fragment.listener

interface INewPasswordFragment {

    sealed interface Action {
        data class Next(
            val oobCode: String,
            val newPassword: String
        ) : Action

        object Cancel : Action
    }

    fun onAction(action: Action)
}