package id.xxx.module.auth.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.ISignUpPasswordFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.utils.RichTextUtils
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.SignUpPasswordFragmentBinding
import id.xxx.module.google_sign.GoogleAccountContract

class SignUpPasswordFragment : BaseFragment<SignUpPasswordFragmentBinding>() {

    private val googleAccountLauncher =
        registerForActivityResult(GoogleAccountContract()) { result ->
            if (result != null) {
                val action =
                    ISignUpPasswordFragment.Action.ClickSignInWithGoogle(token = "${result.idToken}")
                getListener<ISignUpPasswordFragment>()?.onAction(action)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) viewBinding.textInputEditTextEmail.setText(
            SignInputPreferences.getInputEmail(context)
        )
        val textSignUp = getString(R.string.auth_message_sign_in)
        viewBinding.textViewAlreadyAnAccount.text =
            RichTextUtils.setText(
                context = requireContext(),
                firstText = textSignUp,
                lastText = "${getString(R.string.auth_message_already_have_an_account)} $textSignUp",
                lastTextOnClick = {
                    if (!viewBinding.progressBar.isVisible)
                        signInTextClicked()
                },
            )
        viewBinding.textViewAlreadyAnAccount.movementMethod = LinkMovementMethod.getInstance()

        viewBinding.buttonSignUp
            .setOnClickListener { signUpButtonClicked() }
        viewBinding.buttonContinueWithPhone
            .setOnClickListener { signUpWithPhoneButtonClicked() }
        viewBinding.buttonContinueWithGoogle
            .setOnClickListener { googleAccountLauncher.launch(null) }
    }

    fun loadingVisible() = loadingSetVisible(true)

    fun loadingGone() = loadingSetVisible(false)

    private fun loadingSetVisible(isVisible: Boolean) {
        val viewFinal = view
        if (viewFinal != null) {
            viewBinding.buttonSignUp.isEnabled = !isVisible
            viewBinding.progressBar.isVisible = isVisible
            viewBinding.buttonContinueWithPhone.isEnabled = !isVisible
        }
    }

    fun <ERR : Throwable> showError(err: ERR) {
        Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
    }

    fun setSignUpOnCancel(block: () -> Unit) {
        viewBinding.progressBar.setOnClickListener { block.invoke() }
    }

    private fun signUpWithPhoneButtonClicked() {
        ISignUpPasswordFragment.Action.ClickSignUpWithPhone.apply {
            getListener<ISignUpPasswordFragment>()?.onAction(this)
        }
    }

    private fun signInTextClicked() {
        ISignUpPasswordFragment.Action.ClickSignIn(
            email = "${viewBinding.textInputEditTextEmail.text}",
        ).apply { getListener<ISignUpPasswordFragment>()?.onAction(this) }

    }

    private fun signUpButtonClicked() {
        validateFields()?.let { getListener<ISignUpPasswordFragment>()?.onAction(it) }
    }

    private fun validateFields(): ISignUpPasswordFragment.Action? {
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

        return ISignUpPasswordFragment.Action.ClickSignUp(
            email = "${viewBinding.textInputLayoutEmail.editText?.text}",
            password = "${viewBinding.textInputLayoutPassword.editText?.text}"
        )
    }
}