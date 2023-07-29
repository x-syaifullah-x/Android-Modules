package id.xxx.module.auth.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.ISecurityChallengeDialogFragment
import id.xxx.module.auth.fragment.listener.ISignInPhoneFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.model.SecurityChallengeResult
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.SignInPhoneFragmentBinding

class SignInPhoneFragment : BaseFragment(R.layout.sign_in_phone_fragment),
    ISecurityChallengeDialogFragment {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = SignInPhoneFragmentBinding.bind(view)

        if (savedInstanceState == null)
            binding.textInputEditTextPhoneNumber.setText(
                SignInputPreferences.getInputPhoneNumber(context)
            )
        binding.buttonNext.setOnClickListener { nextButtonClicked(binding) }
        binding.buttonSignUp.setOnClickListener { signUpTextClicked(binding) }
        binding.buttonSignInWithEmail.setOnClickListener { signInWithEmailButtonClicked(binding) }
    }

    fun setSignInOnCancel(block: () -> Unit) {
        val binding = SignInPhoneFragmentBinding.bind(requireView())
        binding.progressBar.setOnClickListener { block.invoke() }
    }

    fun <T : Throwable> showError(err: T) {
        Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
    }

    fun loadingVisible() = loadingSetVisible(true)

    fun loadingGone() = loadingSetVisible(false)

    private fun loadingSetVisible(isVisible: Boolean) {
        val viewFinal = view
        if (viewFinal != null) {
            val binding = SignInPhoneFragmentBinding.bind(viewFinal)
            binding.buttonNext.isEnabled = !isVisible
            binding.buttonSignUp.isEnabled = !isVisible
            binding.buttonSignInWithEmail.isEnabled = !isVisible
            binding.progressBar.isVisible = isVisible
        }
    }

    private fun signInWithEmailButtonClicked(binding: SignInPhoneFragmentBinding) {
        val action = ISignInPhoneFragment.Action.ClickSignInWithEmail(
            phoneNumber = "${binding.textInputEditTextPhoneNumber.text}"
        )
        getListener<ISignInPhoneFragment>()?.onAction(action)
    }

    private fun signUpTextClicked(binding: SignInPhoneFragmentBinding) {
        val action = ISignInPhoneFragment.Action.ClickSignUp(
            phoneNumber = "${binding.textInputEditTextPhoneNumber.text}"
        )
        getListener<ISignInPhoneFragment>()?.onAction(action)
    }

    private fun nextButtonClicked(binding: SignInPhoneFragmentBinding) {
        val phoneNumber = "${binding.textInputEditTextPhoneNumber.text}"
        val dialog = SecurityChallengeDialogFragment()
        dialog.arguments =
            bundleOf(SecurityChallengeDialogFragment.KEY_PHONE_NUMBER to phoneNumber)
        dialog.show(childFragmentManager, null)
    }

    override fun onResult(result: SecurityChallengeResult) {
        when (result) {
            is SecurityChallengeResult.Success -> {
                if (result.isNewUser) {
                    val viewFinal = view
                    val messageError = "Unregistered user"
                    if (viewFinal != null) {
                        val binding = SignInPhoneFragmentBinding.bind(viewFinal)
                        binding.textInputEditTextPhoneNumber.error = messageError
                        binding.textInputEditTextPhoneNumber.requestFocus()
                    } else {
                        showError(Throwable(messageError))
                    }
                } else {
                    val action = ISignInPhoneFragment.Action.ClickNext(
                        phoneNumber = result.phoneNumber,
                        recaptchaResponse = result.response
                    )
                    getListener<ISignInPhoneFragment>()?.onAction(action)
                }
            }
            is SecurityChallengeResult.Error -> showError(result.err)
        }
    }
}