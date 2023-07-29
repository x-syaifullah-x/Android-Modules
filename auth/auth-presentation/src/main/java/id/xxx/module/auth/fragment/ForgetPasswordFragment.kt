package id.xxx.module.auth.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.IForgetPasswordFragment
import id.xxx.module.auth.ktx.get
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.ForgetPasswordFragmentBinding

class ForgetPasswordFragment : BaseFragment(R.layout.forget_password_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ForgetPasswordFragmentBinding.bind(view)

        val inputEmail = binding.textInputEditTextEmail
        if (inputEmail.text.isNullOrBlank()) {
            val email = SignInputPreferences.getInputEmail(context)
            inputEmail.setText(email)
            inputEmail.requestFocus()
        }
        binding.textInputEditTextEmail.doOnTextChanged { _, _, _, _ ->
            if (binding.textInputLayoutEmail.error != null) {
                binding.textInputLayoutEmail.error = null
            }
        }
        binding.buttonNext.setOnClickListener {
            val email = inputEmail.text.toString()
            if (!ValidationUtils.isValidEmail(email)) {
                binding.textInputLayoutEmail.error = "Please enter a valid email"
                return@setOnClickListener
            }
            val listener = get<IForgetPasswordFragment>()
            val action = IForgetPasswordFragment.Action(
                email = email,
                loading = {
                    Toast.makeText(it.context, "Loading ...", Toast.LENGTH_LONG).show()
                },
                error = { err ->
                    Toast.makeText(it.context, err.message, Toast.LENGTH_LONG).show()
                },
                success = {
                    Toast.makeText(
                        it.context,
                        "Email verification code sent successfully",
                        Toast.LENGTH_LONG
                    ).show()
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }

            )
            listener?.onAction(action)
        }
    }
}