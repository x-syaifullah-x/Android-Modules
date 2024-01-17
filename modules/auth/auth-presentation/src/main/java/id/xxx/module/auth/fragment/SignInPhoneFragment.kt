package id.xxx.module.auth.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.ISecurityChallengeFragment
import id.xxx.module.auth.fragment.listener.ISignInPasswordFragment
import id.xxx.module.auth.fragment.listener.ISignInPhoneFragment
import id.xxx.module.auth.ktx.getInputMethodManager
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.model.SecurityChallengeResult
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth_presentation.databinding.SignInPhoneFragmentBinding
import id.xxx.module.google_sign.GoogleAccountContract

class SignInPhoneFragment : BaseFragment<SignInPhoneFragmentBinding>(),
    ISecurityChallengeFragment {

    private val googleAccountLauncher =
        registerForActivityResult(GoogleAccountContract()) { result ->
            if (result != null) {
                val action = ISignInPhoneFragment.Action.ClickSignInWithGoogle(
                    token = result.idToken ?: throw NullPointerException()
                )
                getListener<ISignInPhoneFragment>()?.onAction(action)
            }
        }

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
            .setOnClickListener { buttonNextClicked() }
        viewBinding.buttonSignUp
            .setOnClickListener { textSignUpClicked() }
        viewBinding.buttonContinueWithEmail
            .setOnClickListener { buttonContinueWithEmailClicked() }
        viewBinding.buttonContinueWithGoogle
            .setOnClickListener { buttonContinueWithGoogleClicked() }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, onBackPressedCallback
        )
    }

    fun setSignInOnCancel(block: () -> Unit) {
        viewBinding.progressBar.setOnClickListener { block.invoke() }
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
            viewBinding.buttonSignUp.isEnabled = !isVisible
            viewBinding.buttonContinueWithEmail.isEnabled = !isVisible
            viewBinding.progressBar.isVisible = isVisible
        }
    }

    private fun buttonContinueWithEmailClicked() {
        val action = ISignInPhoneFragment.Action.ClickSignInWithEmail(
            phoneNumber = "${viewBinding.textInputEditTextPhoneNumber.text}"
        )
        getListener<ISignInPhoneFragment>()?.onAction(action)
    }

    private fun buttonContinueWithGoogleClicked() {
        googleAccountLauncher.launch(null)
    }

    private fun textSignUpClicked() {
        val action = ISignInPhoneFragment.Action.ClickSignUp(
            phoneNumber = "${viewBinding.textInputEditTextPhoneNumber.text}"
        )
        getListener<ISignInPhoneFragment>()?.onAction(action)
    }

    private fun buttonNextClicked() {
        getInputMethodManager()?.hideSoftInputFromWindow(viewBinding.root.windowToken, 0)

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
                        viewBinding.textInputEditTextPhoneNumber.error = messageError
                        viewBinding.textInputEditTextPhoneNumber.requestFocus()
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