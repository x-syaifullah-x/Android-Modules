package id.xxx.module.auth.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.ISignInPasswordFragment
import id.xxx.module.auth.ktx.get
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.utils.RichTextUtils
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.SignInPasswordFragmentBinding

class SignInPasswordFragment : BaseFragment(R.layout.sign_in_password_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = SignInPasswordFragmentBinding.bind(view)

        if (savedInstanceState == null)
            binding.textInputEditTextEmail
                .setText(SignInputPreferences.getInputEmail(context))

        binding.textViewForgetPassword.setOnClickListener { moveToForgetPassword(binding) }
        binding.buttonSignIn.setOnClickListener { signIn(binding) }
        val textSignUp = getString(R.string.sign_in_password_sign_up)
        binding.textViewDonTHaveAnAccount.text = RichTextUtils.setText(
            context = requireContext(),
            firstText = textSignUp,
            lastText = "${getString(R.string.sign_in_password_don_t_have_an_account)} $textSignUp",
            lastTextOnClick = { if (!binding.progressBar.isVisible) moveToSignUp(binding) }
        )
        binding.textViewDonTHaveAnAccount.movementMethod = LinkMovementMethod.getInstance()
        binding.buttonUsePhone.setOnClickListener { moveToSignInWithPhone(binding) }
    }

    fun loadingVisible() = loadingSetVisible(true)

    fun loadingGone() = loadingSetVisible(false)

    private fun loadingSetVisible(isVisible: Boolean) {
        val viewFinal = view
        if (viewFinal != null) {
            val binding = SignInPasswordFragmentBinding.bind(viewFinal)
            binding.textViewForgetPassword.isEnabled = !isVisible
            binding.buttonSignIn.isEnabled = !isVisible
            binding.buttonUsePhone.isEnabled = !isVisible
            binding.progressBar.isVisible = isVisible
        }
    }

    fun <T : Throwable> showError(err: T) {
        Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
    }

    fun setSignInOnCancel(onCancel: () -> Unit) {
        val binding = SignInPasswordFragmentBinding.bind(requireView())
        binding.progressBar.setOnClickListener { onCancel.invoke() }
    }

    private fun moveToSignInWithPhone(binding: SignInPasswordFragmentBinding) {
        get<ISignInPasswordFragment>()?.onAction(
            ISignInPasswordFragment.Action
                .ClickSignInWithPhone(email = "${binding.textInputEditTextEmail.text}")
        )
    }

    private fun moveToSignUp(binding: SignInPasswordFragmentBinding) {
        get<ISignInPasswordFragment>()?.onAction(
            ISignInPasswordFragment.Action.ClickSignUp(
                email = "${binding.textInputEditTextEmail.text}",
            )
        )
    }

    private fun signIn(binding: SignInPasswordFragmentBinding) {
        validateFields(binding)
            ?.let { action -> get<ISignInPasswordFragment>()?.onAction(action) }
    }

    private fun validateFields(
        binding: SignInPasswordFragmentBinding
    ): ISignInPasswordFragment.Action? {
        val email = "${binding.textInputEditTextEmail.text}"
        var validateMessage = ValidationUtils.validateEmail(email)
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
        return ISignInPasswordFragment.Action.ClickSignIn(
            email = "${binding.textInputLayoutEmail.editText?.text}",
            password = "${binding.textInputLayoutPassword.editText?.text}",
        )
    }

    private fun moveToForgetPassword(binding: SignInPasswordFragmentBinding) {
        get<ISignInPasswordFragment>()?.onAction(
            ISignInPasswordFragment.Action.ClickForgetPassword(
                email = "${binding.textInputEditTextEmail.text}",
            )
        )
    }
}