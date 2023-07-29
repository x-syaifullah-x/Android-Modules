package id.xxx.module.auth.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.ISecurityChallengeDialogFragment
import id.xxx.module.auth.fragment.listener.ISignUpPhoneFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.model.SecurityChallengeResult
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.SignUpPhoneFragmentBinding

class SignUpPhoneFragment : BaseFragment(R.layout.sign_up_phone_fragment),
    ISecurityChallengeDialogFragment {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = SignUpPhoneFragmentBinding.bind(view)

        if (savedInstanceState == null)
            binding.textInputEditTextPhoneNumber.setText(
                SignInputPreferences.getInputPhoneNumber(context)
            )

        binding.buttonNext.setOnClickListener { nextButtonClicked(binding) }
        binding.buttonSignIn.setOnClickListener { signInTextClicked(binding) }
        binding.buttonSignUpWithEmail.setOnClickListener { signUpWithEmailButtonClicked(binding) }
    }

    private fun signUpWithEmailButtonClicked(binding: SignUpPhoneFragmentBinding) {
        ISignUpPhoneFragment.Action.ClickSignUpWithEmail(
            phoneNumber = "${binding.textInputEditTextPhoneNumber.text}"
        ).apply { getListener<ISignUpPhoneFragment>()?.onAction(this) }
    }

    private fun signInTextClicked(binding: SignUpPhoneFragmentBinding) {
        ISignUpPhoneFragment.Action.ClickSignIn(
            phoneNumber = "${binding.textInputEditTextPhoneNumber.text}"
        ).apply { getListener<ISignUpPhoneFragment>()?.onAction(this) }
    }

    private fun nextButtonClicked(binding: SignUpPhoneFragmentBinding) {
        val phoneNumber = "${binding.textInputEditTextPhoneNumber.text}"
        if (!ValidationUtils.isValidPhoneNumber(phoneNumber)) {
            binding.textInputEditTextPhoneNumber.requestFocus()
            binding.textInputEditTextPhoneNumber.error = "Invalid phone number."
            return
        }
        showSecurityChallenge(phoneNumber = phoneNumber)
    }

    private fun showSecurityChallenge(phoneNumber: String) {
        val dialog = SecurityChallengeDialogFragment()
        dialog.arguments =
            bundleOf(SecurityChallengeDialogFragment.KEY_PHONE_NUMBER to phoneNumber)
        dialog.show(childFragmentManager, null)
    }

    override fun onResult(result: SecurityChallengeResult) {
        when (result) {
            is SecurityChallengeResult.Success -> {
                if (result.isNewUser) {
                    val action = ISignUpPhoneFragment.Action.ClickNext(
                        phoneNumber = result.phoneNumber,
                        recaptchaResponse = result.response
                    )
                    getListener<ISignUpPhoneFragment>()?.onAction(action)
                } else {
                    val viewFinal = view
                    val messageError = "The user is already registered"
                    if (viewFinal != null) {
                        val binding = SignUpPhoneFragmentBinding.bind(viewFinal)
                        binding.textInputEditTextPhoneNumber.error = messageError
                        binding.textInputEditTextPhoneNumber.requestFocus()
                    } else {
                        showError(Throwable(messageError))
                    }
                }
            }
            is SecurityChallengeResult.Error -> {
                showError(result.err)
            }
        }
    }

    fun <T : Throwable> showError(err: T) {
        Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
    }

    fun loadingVisible() = loadingSetVisible(true)

    fun loadingGone() = loadingSetVisible(false)

    private fun loadingSetVisible(isVisible: Boolean) {
        val viewFinal = view
        if (viewFinal != null) {
            val binding = SignUpPhoneFragmentBinding.bind(viewFinal)
            binding.buttonNext.isEnabled = !isVisible
            binding.progressBar.isVisible = isVisible
            binding.buttonSignUpWithEmail.isEnabled = !isVisible
        }
    }

    fun setSignUpOnCancel(block: () -> Unit) {
        val viewFinal = view
        if (viewFinal != null) {
            val binding = SignUpPhoneFragmentBinding.bind(viewFinal)
            binding.progressBar.setOnClickListener { block.invoke() }
        }
    }
}