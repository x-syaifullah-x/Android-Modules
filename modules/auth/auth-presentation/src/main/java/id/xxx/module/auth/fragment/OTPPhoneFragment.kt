package id.xxx.module.auth.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.listener.IOTPPhoneFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth_presentation.databinding.OtpPhoneFragmentBinding
import java.security.InvalidParameterException

class OTPPhoneFragment : BaseFragment<OtpPhoneFragmentBinding>() {

    companion object {
        const val KEY_IS_NEW_USER = "KEY_IS_NEW_USER"
        const val KEY_SESSION_INFO = "KEY_SESSION_INFO"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.buttonNext.setOnClickListener {
            val otp = "${viewBinding.textInputEditTextOtp.text}"
            if (otp.length != 6) {
                viewBinding.textInputEditTextOtp.requestFocus()
                viewBinding.textInputEditTextOtp.error = "Invalid otp"
                return@setOnClickListener
            }
            val sessionInfo = arguments?.getString(KEY_SESSION_INFO)
                ?: throw Throwable("required session info")
            val listener = getListener<IOTPPhoneFragment>()
            val isNewUser = arguments?.getBoolean(KEY_IS_NEW_USER)
                ?: throw InvalidParameterException()
            val action = IOTPPhoneFragment.Action.ClickNext(
                isNewUser = isNewUser,
                otp = "${viewBinding.textInputEditTextOtp.text}",
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
            viewBinding.buttonNext.isEnabled = !isVisible
            viewBinding.progressBar.isVisible = isVisible
        }
    }

    fun setCancelProcess(block: () -> Unit) {
        val viewFinal = view
        if (viewFinal != null) {
            viewBinding.progressBar.setOnClickListener { block.invoke() }
        }
    }
}