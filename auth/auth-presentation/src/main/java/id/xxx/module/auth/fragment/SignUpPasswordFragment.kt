package id.xxx.module.auth.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.ISignUpPasswordFragment
import id.xxx.module.auth.ktx.get
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.utils.RichTextUtils
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.SignUpPasswordFragmentBinding

class SignUpPasswordFragment : BaseFragment(R.layout.sign_up_password_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = SignUpPasswordFragmentBinding.bind(view)

        if (savedInstanceState == null)
            binding.textInputEditTextEmail
                .setText(SignInputPreferences.getInputEmail(context))
        binding.buttonSignUp.setOnClickListener { signUpButtonClicked(binding) }
        val textSignUp = getString(R.string.sign_up_password_sign_in)
        binding.textViewAlreadyAnAccount.text = RichTextUtils.setText(
            context = requireContext(),
            firstText = textSignUp,
            lastText = "${getString(R.string.sign_up_password_already_have_an_account)} $textSignUp",
            lastTextOnClick = { if (!binding.progressBar.isVisible) signInTextClicked(binding) }
        )
        binding.textViewAlreadyAnAccount.movementMethod = LinkMovementMethod.getInstance()
        binding.buttonSignUpWithPhone.setOnClickListener { signUpWithPhoneButtonClicked(binding) }
    }

    fun loadingVisible() = loadingSetVisible(true)

    fun loadingGone() = loadingSetVisible(false)

    private fun loadingSetVisible(isVisible: Boolean) {
        val viewFinal = view
        if (viewFinal != null) {
            val binding = SignUpPasswordFragmentBinding.bind(viewFinal)
            binding.buttonSignUp.isEnabled = !isVisible
            binding.progressBar.isVisible = isVisible
            binding.buttonSignUpWithPhone.isEnabled = !isVisible
        }
    }

    fun <ERR : Throwable> showError(err: ERR) {
        Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
    }

    fun setSignUpOnCancel(block: () -> Unit) {
        val binding = SignUpPasswordFragmentBinding.bind(requireView())
        binding.progressBar.setOnClickListener { block.invoke() }
    }

    private fun signUpWithPhoneButtonClicked(binding: SignUpPasswordFragmentBinding) {
        ISignUpPasswordFragment.Action.ClickSignUpWithPhone(
            email = "${binding.textInputEditTextEmail.text}",
        ).apply { get<ISignUpPasswordFragment>()?.onAction(this) }
    }

    private fun signInTextClicked(binding: SignUpPasswordFragmentBinding) {
        ISignUpPasswordFragment.Action.ClickSignIn(
            email = "${binding.textInputEditTextEmail.text}",
        ).apply { get<ISignUpPasswordFragment>()?.onAction(this) }

    }

    private fun signUpButtonClicked(binding: SignUpPasswordFragmentBinding) {
        validateFields(binding)
            ?.let { get<ISignUpPasswordFragment>()?.onAction(it) }
    }

    private fun validateFields(
        binding: SignUpPasswordFragmentBinding
    ): ISignUpPasswordFragment.Action? {
        val email = "${binding.textInputEditTextEmail.text}"
        var validateMessage = ValidationUtils.isValidEmail(email)
        if (validateMessage != null) {
            binding.textInputLayoutEmail.requestFocus()
            binding.textInputEditTextEmail.error = validateMessage
            return null
        }
        val password = "${binding.textInputLayoutPassword.editText?.text}"
        validateMessage = ValidationUtils.isValidPassword(password)
        if (validateMessage != null) {
            binding.textInputLayoutPassword.requestFocus()
            binding.textInputEditTextPassword.error = validateMessage
            return null
        }

        return ISignUpPasswordFragment.Action.ClickSignUp(
            email = "${binding.textInputLayoutEmail.editText?.text}",
            password = "${binding.textInputLayoutPassword.editText?.text}"
        )
    }
}