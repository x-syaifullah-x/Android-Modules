package id.xxx.module.auth.fragment.phone

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.phone.listener.IPhoneSignFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.ktx.hideSoftInputFromWindow
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.google_sign.GoogleAccountResultContract
import id.xxx.modules.auth.auth_presentation.databinding.PhoneSignFragmentBinding

class PhoneSignFragment : BaseFragment<PhoneSignFragmentBinding>() {

    private val googleAccountLauncher =
        registerForActivityResult(GoogleAccountResultContract()) { result ->
            if (result != null) {
                val action = IPhoneSignFragment.Action.ClickSignInWithGoogle(
                    token = result.idToken ?: throw NullPointerException()
                )
                getListener<IPhoneSignFragment>()?.onAction(action)
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
    }

    fun setOnCancelProcess(block: () -> Unit) {
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
        val phoneNumber = "${viewBinding.textInputEditTextPhoneNumber.text}"
        val errorMessage = ValidationUtils.validPhoneNumber(phoneNumber)
        if (errorMessage != null) {
            viewBinding.textInputEditTextPhoneNumber.requestFocus()
            viewBinding.textInputLayoutPhoneNumber.error = errorMessage
            return
        }
        hideSoftInputFromWindow()
        val action = IPhoneSignFragment.Action.ClickNext(phoneNumber = phoneNumber)
        getListener<IPhoneSignFragment>()?.onAction(action)
    }
}