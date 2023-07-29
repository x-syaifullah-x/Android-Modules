package id.xxx.module.auth.fragment.listener

interface IForgetPasswordFragment {

    data class Action(
        val email: String,
        val loading: () -> Unit,
        val error: (Throwable) -> Unit,
        val success: () -> Unit
    )

    fun onAction(action: Action)
}