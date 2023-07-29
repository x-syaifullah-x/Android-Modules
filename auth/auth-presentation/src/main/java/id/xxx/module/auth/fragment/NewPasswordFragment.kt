package id.xxx.module.auth.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.INewPasswordFragment
import id.xxx.module.auth.fragment.listener.ISignInPasswordFragment
import id.xxx.module.auth.ktx.get
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.utils.RichTextUtils
import id.xxx.module.auth.utils.ValidationUtils
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.NewPasswordFragmentBinding
import id.xxx.module.auth_presentation.databinding.SignInPasswordFragmentBinding

class NewPasswordFragment : BaseFragment(R.layout.new_password_fragment) {

    companion object {
        const val KEY_OOB_CODE = "key_oob_code"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val oobCode = arguments?.getString(KEY_OOB_CODE)
            ?: throw NullPointerException("required oob code")
        val binding = NewPasswordFragmentBinding.bind(view)
        binding.buttonNext.setOnClickListener {
            val newPassword = binding.textInputEditTextNewPassword.text.toString()
            get<INewPasswordFragment>()?.onAction(
                INewPasswordFragment.Action(
                    oobCode = oobCode,
                    newPassword = newPassword
                )
            )
        }
    }
}