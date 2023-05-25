package id.xxx.module.auth.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.IOTPFragment
import id.xxx.module.auth.ktx.get
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.OtpFragmentBinding
import java.security.InvalidParameterException

class OTPFragment : BaseFragment(R.layout.otp_fragment) {

    companion object {
        const val KEY_IS_NEW_USER = "KEY_IS_NEW_USER"
        const val KEY_SESSION_INFO = "KEY_SESSION_INFO"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = OtpFragmentBinding.bind(view)

        binding.buttonNext.setOnClickListener {
            val otp = "${binding.textInputEditTextOtp.text}"
            if (otp.length != 6) {
                binding.textInputEditTextOtp.requestFocus()
                binding.textInputEditTextOtp.error = "Invalid otp"
                return@setOnClickListener
            }
            val sessionInfo = arguments?.getString(KEY_SESSION_INFO)
                ?: throw Throwable("required session info")
            val listener = get<IOTPFragment>()
            val isNewUser = arguments?.getBoolean(KEY_IS_NEW_USER)
                ?: throw InvalidParameterException()
            val action = IOTPFragment.Action.ClickNext(
                isNewUser = isNewUser,
                otp = "${binding.textInputEditTextOtp.text}",
                sessionInfo = sessionInfo
            )
            listener?.onAction(action)
        }
    }

    fun <T : Throwable> showError(err: T) {
        Toast.makeText(context, err.message, Toast.LENGTH_LONG).show()
    }

    fun loadingVisible() = loadingSetVisible(true)

    fun loadingGone() = loadingSetVisible(false)

    private fun loadingSetVisible(isVisible: Boolean) {
        val viewFinal = view
        if (viewFinal != null) {
            val binding = OtpFragmentBinding.bind(viewFinal)
            binding.buttonNext.isEnabled = !isVisible
            binding.progressBar.isVisible = isVisible
        }
    }

    fun setCancelProcess(block: () -> Unit) {
        val viewFinal = view
        if (viewFinal != null) {
            val binding = OtpFragmentBinding.bind(viewFinal)
            binding.progressBar.setOnClickListener { block.invoke() }
        }
    }
}