package id.xxx.module.auth.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.ISignInPasswordFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.utils.RichTextUtils
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.SignInPasswordFragmentBinding
import id.xxx.module.google_sign.GoogleAccountContract

class SignInPasswordFragment : BaseFragment<SignInPasswordFragmentBinding>() {

    private val googleAccountLauncher =
        registerForActivityResult(GoogleAccountContract()) { result ->
            if (result != null) {
                getListener<ISignInPasswordFragment>()?.onAction(
                    ISignInPasswordFragment.Action.ClickContinueWithGoogle(
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

        viewBinding.textViewForgetPassword.setOnClickListener { moveToForgetPassword() }
        viewBinding.buttonSignIn.setOnClickListener { signIn() }
        val textSignUp = getString(R.string.auth_message_sign_up)
        viewBinding.textViewDonTHaveAnAccount.text = RichTextUtils.setText(
            context = requireContext(),
            firstText = textSignUp,
            lastText = "${getString(R.string.auth_message_don_t_have_an_account)} $textSignUp",
            lastTextOnClick = { if (!viewBinding.progressBar.isVisible) moveToSignUp() }
        )
        viewBinding.textViewDonTHaveAnAccount.movementMethod = LinkMovementMethod.getInstance()
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
            viewBinding.textViewForgetPassword.isEnabled = !isVisible
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
        getListener<ISignInPasswordFragment>()?.onAction(
            ISignInPasswordFragment.Action
                .ClickContinueWithPhone(email = "${viewBinding.textInputEditTextEmail.text}")
        )
    }

    private fun moveToSignUp() {
        getListener<ISignInPasswordFragment>()?.onAction(
            ISignInPasswordFragment.Action.ClickSignUp(
                email = "${viewBinding.textInputEditTextEmail.text}",
            )
        )
    }

    private fun signIn() {
        validateFields()
            ?.let { action -> getListener<ISignInPasswordFragment>()?.onAction(action) }
    }

    private fun validateFields(): ISignInPasswordFragment.Action? {
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
        return ISignInPasswordFragment.Action.ClickSignIn(
            email = "${viewBinding.textInputLayoutEmail.editText?.text}",
            password = "${viewBinding.textInputLayoutPassword.editText?.text}",
        )
    }

    private fun moveToForgetPassword() {
        getListener<ISignInPasswordFragment>()?.onAction(
            ISignInPasswordFragment.Action.ClickForgetPassword(
                email = "${viewBinding.textInputEditTextEmail.text}",
            )
        )
    }
}