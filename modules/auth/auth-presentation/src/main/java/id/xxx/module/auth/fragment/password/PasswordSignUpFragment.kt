package id.xxx.module.auth.fragment.password

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordSignUpFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.google_sign.GoogleAccountResultContract
import id.xxx.modules.auth.auth_presentation.databinding.PasswordSignUpFragmentBinding

class PasswordSignUpFragment : BaseFragment<PasswordSignUpFragmentBinding>() {

    private val googleAccountLauncher =
        registerForActivityResult(GoogleAccountResultContract()) { result ->
            if (result != null) {
                val action =
                    IPasswordSignUpFragment.Action.ClickSignInWithGoogle(token = "${result.idToken}")
                getListener<IPasswordSignUpFragment>()?.onAction(action)
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) viewBinding.textInputEditTextEmail.setText(
            SignInputPreferences.getInputEmail(context)
        )
//        val textSignUp = getString(R.string.auth_message_sign_in)
//        viewBinding.textViewAlreadyAnAccount.text =
//            RichTextUtils.setText(
//                context = requireContext(),
//                firstText = textSignUp,
//                lastText = "${getString(R.string.auth_message_already_have_an_account)} $textSignUp",
//                lastTextOnClick = {
//                    if (!viewBinding.progressBar.isVisible)
//                        signInTextClicked()
//                },
//            )
//        viewBinding.textViewAlreadyAnAccount.movementMethod = LinkMovementMethod.getInstance()
        viewBinding.tvSignIn.setOnClickListener {
            if (!viewBinding.progressBar.isVisible)
                signInTextClicked()
        }

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
        IPasswordSignUpFragment.Action.ClickSignUpWithPhone.apply {
            getListener<IPasswordSignUpFragment>()?.onAction(this)
        }
    }

    private fun signInTextClicked() {
        IPasswordSignUpFragment.Action.ClickSignIn(
            email = "${viewBinding.textInputEditTextEmail.text}",
        ).apply { getListener<IPasswordSignUpFragment>()?.onAction(this) }

    }

    private fun signUpButtonClicked() {
        validateFields()?.let { getListener<IPasswordSignUpFragment>()?.onAction(it) }
    }

    private fun validateFields(): IPasswordSignUpFragment.Action? {
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

        return IPasswordSignUpFragment.Action.ClickSignUp(
            email = "${viewBinding.textInputLayoutEmail.editText?.text}",
            password = "${viewBinding.textInputLayoutPassword.editText?.text}"
        )
    }
}