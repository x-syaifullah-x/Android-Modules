package id.xxx.module.auth.fragment.listener

interface IForgetPasswordFragment {

    data class Action(
        val email: String,
        val onLoading: () -> Unit,
        val onError: (Throwable) -> Unit,
        val onSuccess: () -> Unit
    )

    fun onAction(action: Action)
}