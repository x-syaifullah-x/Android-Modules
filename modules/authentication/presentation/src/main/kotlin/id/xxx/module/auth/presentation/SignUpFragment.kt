package id.xxx.module.auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import id.xxx.module.auth.domain.model.TypeSign
import id.xxx.module.viewbinding.ktx.viewBinding
import id.xxx.modules.authentication.presentation.databinding.FragmentSignUpBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private val vBinding by viewBinding<FragmentSignUpBinding>()

    private var jobSignUp: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = vBinding.root.apply {
        if (background == null)
            background = activity?.window?.decorView?.background
    }

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
        vBinding.buttonSignUp.setOnClickListener {
            signUp()
        }
    }

    private fun signUp() {
        vBinding.buttonSignUp.isEnabled = false
        vBinding.progressBar.visibility = View.VISIBLE
        val iSign =
            if (parentFragment is ISign)
                parentFragment as? ISign
            else if (activity is ISign)
                activity as? ISign
            else
                null
        jobSignUp = lifecycleScope.launch {
            val email = "${vBinding.textInputEditTextEmail.text}"
            val password = "${vBinding.textInputEditTextPassword.text}"
            val t = TypeSign.UpPassword(email = email, password = password)
            val res = iSign?.onSign(t)
            if (res is SignResult.Error) {
                val c = vBinding.root.context
                Toast.makeText(c, res.err.message, Toast.LENGTH_LONG).show()
            }
            vBinding.buttonSignUp.isEnabled = true
            vBinding.progressBar.visibility = View.GONE
        }
    }
}