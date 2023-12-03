package id.xxx.module.auth.fragment

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.listener.ISecurityChallengeFragment
import id.xxx.module.auth.fragment.listener.ISignInPhoneFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.model.SecurityChallengeResult
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth_presentation.databinding.SignInPhoneFragmentBinding
import id.xxx.module.fragment.base.BaseFragmentViewBinding

class SignInPhoneFragment : BaseFragmentViewBinding<SignInPhoneFragmentBinding>(),
    ISecurityChallengeFragment {

    private val onBackPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            viewBinding.containerSecurityChallenge.removeAllViews()
            isEnabled = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null)
            viewBinding.textInputEditTextPhoneNumber
                .setText(SignInputPreferences.getInputPhoneNumber(context))
        viewBinding.buttonNext
            .setOnClickListener { nextButtonClicked() }
        viewBinding.buttonSignUp
            .setOnClickListener { signUpTextClicked(viewBinding) }
        viewBinding.buttonSignInWithEmail
            .setOnClickListener { signInWithEmailButtonClicked(viewBinding) }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )
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

    private fun nextButtonClicked() {
        val imm = ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(viewBinding.root.windowToken, 0)

        onBackPressedCallback.isEnabled = true
        val phoneNumber = "${viewBinding.textInputEditTextPhoneNumber.text}"
        val bundle =
            bundleOf(SecurityChallengeFragment.KEY_PHONE_NUMBER to phoneNumber)
        childFragmentManager
            .beginTransaction()
            .add(
                viewBinding.containerSecurityChallenge.id,
                SecurityChallengeFragment::class.java,
                bundle,
                "SecurityChallengeDialogFragment::class.java"
            )
            .commit()
    }

    override fun onResult(result: SecurityChallengeResult) {
        viewBinding.containerSecurityChallenge
            .removeAllViews()
        onBackPressedCallback.isEnabled = false
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