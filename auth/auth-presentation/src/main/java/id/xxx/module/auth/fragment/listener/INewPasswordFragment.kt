package id.xxx.module.auth.fragment.listener

interface INewPasswordFragment {

    data class Action(
        val oobCode: String,
        val newPassword: String
    )

    fun onAction(action: Action)
}