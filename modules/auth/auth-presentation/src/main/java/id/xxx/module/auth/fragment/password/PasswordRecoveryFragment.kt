package id.xxx.module.auth.fragment.password

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordRecoveryFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.modules.auth.auth_presentation.databinding.PasswordRecoveryFragmentBinding

class PasswordRecoveryFragment : BaseFragment<PasswordRecoveryFragmentBinding>() {

    private val progress by lazy { ProgressDialog(context) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textInputEditTextEmail = viewBinding.textInputEditTextEmail
        val textInputLayoutEmail = viewBinding.textInputLayoutEmail
        textInputEditTextEmail.doOnTextChanged { _, _, _, _ ->
            if (textInputLayoutEmail.error != null) {
                textInputLayoutEmail.error = null
            }
        }
        val listener = getListener<IPasswordRecoveryFragment>()
        progress.setOnCancelListener {
            listener?.onAction(IPasswordRecoveryFragment.Action.Cancel)
        }
        viewBinding.buttonNext.setOnClickListener {
            val email = textInputEditTextEmail.text.toString()
            if (!ValidationUtils.isValidEmail(email)) {
                textInputLayoutEmail.error = "Please enter a valid email"
                return@setOnClickListener
            }
            val action = IPasswordRecoveryFragment.Action.Next(
                email = email
            )
            listener?.onAction(action)
        }
    }

    internal fun onLoading(message: String = "Loading ...") {
        progress.setMessage(message)
        progress.show()
    }

    internal fun onError(err: Throwable) {
        progress.cancel()
        Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
    }

    internal fun onSuccess() {
        progress.cancel()
        Toast.makeText(
            context,
            "Email verification code sent successfully",
            Toast.LENGTH_LONG
        ).show()
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}