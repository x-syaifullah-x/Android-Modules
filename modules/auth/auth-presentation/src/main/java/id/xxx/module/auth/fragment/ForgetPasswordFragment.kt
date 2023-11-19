package id.xxx.module.auth.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.IForgetPasswordFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.ForgetPasswordFragmentBinding

class ForgetPasswordFragment : BaseFragment(R.layout.forget_password_fragment) {

    private val progress by lazy { ProgressDialog(context) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ForgetPasswordFragmentBinding.bind(view)

        val textInputEditTextEmail = binding.textInputEditTextEmail
        val textInputLayoutEmail = binding.textInputLayoutEmail
        textInputEditTextEmail.doOnTextChanged { _, _, _, _ ->
            if (textInputLayoutEmail.error != null) {
                textInputLayoutEmail.error = null
            }
        }
        val listener = getListener<IForgetPasswordFragment>()
        progress.setOnCancelListener {
            listener?.onAction(IForgetPasswordFragment.Action.Cancel)
        }
        binding.buttonNext.setOnClickListener {
            val email = textInputEditTextEmail.text.toString()
            if (!ValidationUtils.isValidEmail(email)) {
                textInputLayoutEmail.error = "Please enter a valid email"
                return@setOnClickListener
            }
            val action = IForgetPasswordFragment.Action.Next(
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