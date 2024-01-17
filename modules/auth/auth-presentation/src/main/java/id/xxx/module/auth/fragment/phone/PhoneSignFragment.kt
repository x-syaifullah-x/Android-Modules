package id.xxx.module.auth.fragment.phone

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.phone.listener.IPhoneSignFragment
import id.xxx.module.auth.fragment.phone.listener.IRecaptchaFragment
import id.xxx.module.auth.ktx.getInputMethodManager
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.auth_presentation.databinding.PhoneSignFragmentBinding
import id.xxx.module.google_sign.GoogleAccountContract

class PhoneSignFragment : BaseFragment<PhoneSignFragmentBinding>(),
    IRecaptchaFragment {

    private val googleAccountLauncher =
        registerForActivityResult(GoogleAccountContract()) { result ->
            if (result != null) {
                val action = IPhoneSignFragment.Action.ClickSignInWithGoogle(
                    token = result.idToken ?: throw NullPointerException()
                )
                getListener<IPhoneSignFragment>()?.onAction(action)
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
        viewBinding.textInputEditTextPhoneNumber.doOnTextChanged { _, _, _, _ ->
            if (viewBinding.textInputLayoutPhoneNumber.error != null) {
                viewBinding.textInputLayoutPhoneNumber.error = null
            }
        }
        viewBinding.buttonNext
            .setOnClickListener { buttonNextClicked() }
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
            viewBinding.buttonContinueWithEmail.isEnabled = !isVisible
            viewBinding.progressBar.isVisible = isVisible
        }
    }

    private fun buttonContinueWithEmailClicked() {
        val action = IPhoneSignFragment.Action.ClickSignInWithEmail(
            phoneNumber = "${viewBinding.textInputEditTextPhoneNumber.text}"
        )
        getListener<IPhoneSignFragment>()?.onAction(action)
    }

    private fun buttonContinueWithGoogleClicked() {
        googleAccountLauncher.launch(null)
    }

    private fun buttonNextClicked() {
        getInputMethodManager()?.hideSoftInputFromWindow(viewBinding.root.windowToken, 0)
        onBackPressedCallback.isEnabled = true
        val phoneNumber = "${viewBinding.textInputEditTextPhoneNumber.text}"
        val message = ValidationUtils.validPhoneNumber(phoneNumber)
        if (message != null) {
            viewBinding.textInputEditTextPhoneNumber.requestFocus()
            viewBinding.textInputLayoutPhoneNumber.error = message
            return
        }
        val bundle =
            bundleOf(RecaptchaFragment.KEY_PHONE_NUMBER to phoneNumber)
        childFragmentManager
            .beginTransaction()
            .add(
                viewBinding.containerSecurityChallenge.id,
                RecaptchaFragment::class.java,
                bundle,
                "SecurityChallengeDialogFragment::class.java"
            )
            .commit()
    }

    override fun onAction(action: IRecaptchaFragment.Action) {
        viewBinding.containerSecurityChallenge
            .removeAllViews()
        onBackPressedCallback.isEnabled = false
        when (action) {
            is IRecaptchaFragment.Action.Success -> {
                getListener<IPhoneSignFragment>()?.onAction(
                    IPhoneSignFragment.Action.ClickNext(
                        phoneNumber = action.phoneNumber,
                        recaptchaResponse = action.response
                    )
                )
            }

            is IRecaptchaFragment.Action.Error -> showError(action.err)
        }
    }
}