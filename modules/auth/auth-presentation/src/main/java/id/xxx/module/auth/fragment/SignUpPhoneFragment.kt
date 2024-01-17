package id.xxx.module.auth.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.ISecurityChallengeFragment
import id.xxx.module.auth.fragment.listener.ISignUpPhoneFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.model.SecurityChallengeResult
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.SignUpPhoneFragmentBinding
import id.xxx.module.google_sign.GoogleAccountContract

class SignUpPhoneFragment : BaseFragment<SignUpPhoneFragmentBinding>(), ISecurityChallengeFragment {

    private val googleAccountLauncher =
        registerForActivityResult(GoogleAccountContract()) { result ->
            if (result != null) {
                ISignUpPhoneFragment.Action.ClickSignUpWithGoogle(
                    token = result.idToken ?: throw NullPointerException()
                ).apply { getListener<ISignUpPhoneFragment>()?.onAction(this) }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null)
            viewBinding.textInputEditTextPhoneNumber.setText(
                SignInputPreferences.getInputPhoneNumber(context)
            )

        viewBinding.buttonNext.setOnClickListener { nextButtonClicked() }
        viewBinding.buttonSignIn.setOnClickListener { signInTextClicked() }
        viewBinding.buttonContinueWithEmail.setOnClickListener { signUpWithEmailButtonClicked() }
        viewBinding.buttonContinueWithGoogle.setOnClickListener { signUpWithGoogleButtonClicked() }
    }

    private fun signUpWithGoogleButtonClicked() {
        googleAccountLauncher.launch(null)
    }

    private fun signUpWithEmailButtonClicked() {
        ISignUpPhoneFragment.Action.ClickSignUpWithEmail(
            phoneNumber = "${viewBinding.textInputEditTextPhoneNumber.text}"
        ).apply { getListener<ISignUpPhoneFragment>()?.onAction(this) }
    }

    private fun signInTextClicked() {
        ISignUpPhoneFragment.Action.ClickSignIn(
            phoneNumber = "${viewBinding.textInputEditTextPhoneNumber.text}"
        ).apply { getListener<ISignUpPhoneFragment>()?.onAction(this) }
    }

    private fun nextButtonClicked() {
        val phoneNumber = "${viewBinding.textInputEditTextPhoneNumber.text}"
        val message = ValidationUtils.validPhoneNumber(phoneNumber)
        if (message != null) {
            viewBinding.textInputEditTextPhoneNumber.requestFocus()
            viewBinding.textInputLayoutPhoneNumber.error = message
            return
        }
        showSecurityChallenge(phoneNumber = phoneNumber)
    }

    private fun showSecurityChallenge(phoneNumber: String) {
        val bundle = bundleOf(SecurityChallengeFragment.KEY_PHONE_NUMBER to phoneNumber)
        childFragmentManager.beginTransaction().add(
            R.id.container_security_challenge,
            SecurityChallengeFragment::class.java,
            bundle,
            null
        ).commit()
    }

    override fun onResult(result: SecurityChallengeResult) {
        when (result) {
            is SecurityChallengeResult.Success -> {
                if (result.isNewUser) {
                    val action = ISignUpPhoneFragment.Action.ClickNext(
                        phoneNumber = result.phoneNumber, recaptchaResponse = result.response
                    )
                    getListener<ISignUpPhoneFragment>()?.onAction(action)
                } else {
                    val viewFinal = view
                    val messageError = "The user is already registered"
                    if (viewFinal != null) {
                        viewBinding.textInputEditTextPhoneNumber.error = messageError
                        viewBinding.textInputEditTextPhoneNumber.requestFocus()
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
            viewBinding.buttonNext.isEnabled = !isVisible
            viewBinding.progressBar.isVisible = isVisible
            viewBinding.buttonContinueWithEmail.isEnabled = !isVisible
        }
    }

    fun setSignUpOnCancel(block: () -> Unit) {
        val viewFinal = view
        if (viewFinal != null) {
            viewBinding.progressBar.setOnClickListener { block.invoke() }
        }
    }
}