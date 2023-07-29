package id.xxx.module.auth.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.INewPasswordFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.NewPasswordFragmentBinding

class NewPasswordFragment : BaseFragment(R.layout.new_password_fragment) {

    companion object {
        const val KEY_OOB_CODE = "key_oob_code"
    }

    private val progressDialog by lazy { ProgressDialog(activity) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val oobCode = arguments?.getString(KEY_OOB_CODE)
            ?: throw NullPointerException("required oob code")

        val binding = NewPasswordFragmentBinding.bind(view)

        val listener = getListener<INewPasswordFragment>()

        progressDialog.setOnCancelListener {
            listener?.onAction(INewPasswordFragment.Action.Cancel)
        }

        binding.textInputEditTextNewPassword.doOnTextChanged { _, _, _, _ ->
            if (binding.textInputLayoutNewPassword.error != null)
                binding.textInputLayoutNewPassword.error = null
        }
        binding.buttonNext.setOnClickListener {
            val newPassword = binding.textInputEditTextNewPassword.text.toString()
            if (newPassword.length < 6) {
                binding.textInputLayoutNewPassword.error =
                    "Password should be at least 6 characters"
                return@setOnClickListener
            }
            listener?.onAction(
                INewPasswordFragment.Action.Next(
                    oobCode = oobCode,
                    newPassword = newPassword
                )
            )
        }
    }

    internal fun onLoading() {
        progressDialog.setMessage("Loading ...")
        progressDialog.show()
    }

    internal fun <T : Throwable> onError(err: T) {
        Toast.makeText(context, err.localizedMessage, Toast.LENGTH_LONG).show()
        progressDialog.cancel()
    }

    internal fun onSuccess() {
        progressDialog.cancel()
    }
}