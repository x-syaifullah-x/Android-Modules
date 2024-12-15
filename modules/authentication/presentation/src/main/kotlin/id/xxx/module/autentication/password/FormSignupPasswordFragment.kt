package id.xxx.module.autentication.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import id.xxx.module.autentication.IAuthentication
import id.xxx.module.auth.domain.model.AuthenticationType
import id.xxx.module.common.Resources
import id.xxx.module.ktx.authentication
import id.xxx.module.viewbinding.ktx.viewBinding
import id.xxx.modules.authentication.presentation.databinding.FragmentSignUpBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

class FormSignupPasswordFragment : Fragment() {

    private val vBinding by viewBinding<FragmentSignUpBinding>()

    private var jobSignUp: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = vBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vBinding.textInputEditTextEmail.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                val hint =
                    if (hasFocus)
                        "Email"
                    else
                        "Please enter your email"
                vBinding.textInputLayoutEmail.hint = hint
            }
        vBinding.textInputEditTextPassword.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                val hint =
                    if (hasFocus)
                        "Password"
                    else
                        "Please enter your password"
                vBinding.textInputLayoutPassword.hint = hint
            }

        vBinding.textInputEditTextRetryPassword.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                val hint =
                    if (hasFocus)
                        "Re Password"
                    else
                        "Please re enter your password"
                vBinding.textInputLayoutRetryPassword.hint = hint
            }
        vBinding.buttonSignUp.setOnClickListener(this::signUp)
    }

    private fun signUp(v: View) {
        vBinding.buttonSignUp.isEnabled = false
        vBinding.progressBar.visibility = View.VISIBLE
        jobSignUp = lifecycleScope.launch {
            val email = "${vBinding.textInputEditTextEmail.text}"
            val password = "${vBinding.textInputEditTextPassword.text}"
            val t = AuthenticationType.Password(
                email = email,
                password = password,
                type = AuthenticationType.Password.Type.UP
            )
            val res = authentication?.onAuthentication(t)?.lastOrNull()
            if (res is Resources.Failure) {
                val c = v.context
                Toast.makeText(c, res.value.message, Toast.LENGTH_LONG).show()
            }
            vBinding.buttonSignUp.isEnabled = true
            vBinding.progressBar.visibility = View.GONE
        }
    }
}