package id.xxx.module.auth.fragment

import android.os.Bundle
import android.view.View
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.preferences.SignInputPreferences
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
    }
}