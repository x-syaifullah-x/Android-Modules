package id.xxx.module.auth.fragment.password

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordSignInFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.ktx.hideSoftInputFromWindow
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.google_sign.GoogleAccountResultContract
import id.xxx.modules.auth.auth_presentation.databinding.PasswordSignInFragmentBinding

class PasswordSignInFragment : BaseFragment<PasswordSignInFragmentBinding>() {

    private val googleAccountLauncher =
        registerForActivityResult(GoogleAccountResultContract()) { result ->
            if (result != null) {
                getListener<IPasswordSignInFragment>()?.onAction(
                    IPasswordSignInFragment.Action.ClickContinueWithGoogle(
                        token = "${result.idToken}"
                    )
                )
            } else {
                println("google sign canceled")
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null)
            viewBinding.textInputEditTextEmail
                .setText(SignInputPreferences.getInputEmail(context))

        viewBinding.buttonForgetPassword.setOnClickListener { moveToForgetPassword() }
        viewBinding.buttonSignIn.setOnClickListener { signIn() }
//        val textSignUp = getString(R.string.auth_message_sign_up)
//        viewBinding.textViewDonTHaveAnAccount.text = RichTextUtils.setText(
//            context = requireContext(),
//            firstText = textSignUp,
//            lastText = "${getString(R.string.auth_message_don_t_have_an_account)} $textSignUp",
//            lastTextOnClick = { if (!viewBinding.progressBar.isVisible) moveToSignUp() }
//        )
//        viewBinding.textViewDonTHaveAnAccount.movementMethod = LinkMovementMethod.getInstance()
        viewBinding.tvSignUp.setOnClickListener {
            if (!viewBinding.progressBar.isVisible)
                moveToSignUp()
        }
        viewBinding.buttonUsePhone.setOnClickListener { moveToSignInWithPhone() }
        viewBinding.buttonUseGoogle.setOnClickListener {
            googleAccountLauncher.launch(null)
        }
    }

    fun loadingVisible() = loadingSetVisible(true)

    fun loadingGone() = loadingSetVisible(false)

    private fun loadingSetVisible(isVisible: Boolean) {
        val viewFinal = view
        if (viewFinal != null) {
            viewBinding.buttonForgetPassword.isEnabled = !isVisible
            viewBinding.buttonSignIn.isEnabled = !isVisible
            viewBinding.buttonUsePhone.isEnabled = !isVisible
            viewBinding.progressBar.isVisible = isVisible
        }
    }

    fun <T : Throwable> showError(err: T) {
        Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
    }

    fun setSignInOnCancel(onCancel: () -> Unit) {
        viewBinding.progressBar.setOnClickListener { onCancel.invoke() }
    }

    private fun moveToSignInWithPhone() {
        getListener<IPasswordSignInFragment>()?.onAction(
            IPasswordSignInFragment.Action
                .ClickContinueWithPhone(email = "${viewBinding.textInputEditTextEmail.text}")
        )
    }

    private fun moveToSignUp() {
        getListener<IPasswordSignInFragment>()?.onAction(
            IPasswordSignInFragment.Action.ClickSignUp(
                email = "${viewBinding.textInputEditTextEmail.text}",
            )
        )
    }

    private fun signIn() {
        validateFields()?.let { action ->
            hideSoftInputFromWindow()
            getListener<IPasswordSignInFragment>()?.onAction(action)
        }
    }

    private fun validateFields(): IPasswordSignInFragment.Action.ClickSignIn? {
        val email = "${viewBinding.textInputEditTextEmail.text}"
        var validateMessage = ValidationUtils.validateEmail(email)
        if (validateMessage != null) {
            viewBinding.textInputLayoutEmail.requestFocus()
            viewBinding.textInputEditTextEmail.error = validateMessage
            return null
        }
        val password = "${viewBinding.textInputLayoutPassword.editText?.text}"
        validateMessage = ValidationUtils.isValidPassword(password)
        if (validateMessage != null) {
            viewBinding.textInputLayoutPassword.requestFocus()
            viewBinding.textInputEditTextPassword.error = validateMessage
            return null
        }
        return IPasswordSignInFragment.Action.ClickSignIn(
            email = "${viewBinding.textInputLayoutEmail.editText?.text}",
            password = "${viewBinding.textInputLayoutPassword.editText?.text}",
        )
    }

    private fun moveToForgetPassword() {
        hideSoftInputFromWindow()
        getListener<IPasswordSignInFragment>()?.onAction(
            IPasswordSignInFragment.Action.ClickForgetPassword(
                email = "${viewBinding.textInputEditTextEmail.text}",
            )
        )
    }
}