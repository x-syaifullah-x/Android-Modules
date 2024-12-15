package id.xxx.module.autentication.phone

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import id.xxx.module.auth.domain.model.AuthenticationType
import id.xxx.module.common.Resources
import id.xxx.module.ktx.authentication
import id.xxx.module.viewbinding.ktx.viewBinding
import id.xxx.modules.authentication.presentation.databinding.FragmentPhoneVerificationBinding
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch

class PhoneOTPVerifierFragment : Fragment() {

    companion object {

        const val DATA_EXTRA_PHONE_NUMBER = "data_extra_phone_number"
        const val DATA_EXTRA_SESSION_INFO = "data_extra_session_info"
    }

    private val vBinding by viewBinding<FragmentPhoneVerificationBinding>()

    private val countDownTimer = object : CountDownTimer(30L * 1000L, 1000L) {
        override fun onTick(millisUntilFinished: Long) {
            lifecycleScope.launch {
                vBinding.tvMessageResending.visibility = View.VISIBLE
                val message =
                    "Mohon tunggu dalam ${millisUntilFinished / 1000L} detik untuk kirim ulang"
                vBinding.tvMessageResending.text = message
                vBinding.buttonResending.visibility = View.GONE
            }
        }

        override fun onFinish() {
            lifecycleScope.launch {
                vBinding.tvMessageResending.visibility = View.GONE
                vBinding.buttonResending.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = vBinding.root.apply {

        countDownTimer.start()

        if (background == null)
            background = activity?.window?.decorView?.background
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phoneNumber = arguments
            ?.getString(DATA_EXTRA_PHONE_NUMBER) ?: throw IllegalArgumentException()
        vBinding.textViewMessage.text = "Kode verifikasi telah dikirim melalui sms ke $phoneNumber"
        vBinding.textInputEditTextPhone.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                val hint =
                    if (hasFocus)
                        "OTP"
                    else
                        "Please enter OTP"
                vBinding.textInputLayoutPhone.hint = hint
            }
        vBinding.buttonResending.setOnClickListener {
            val sfm = requireActivity().supportFragmentManager
            val args = bundleOf(PhoneOTPSanderFragment.DATA_EXTRA_PHONE_NUMBER to phoneNumber)
            sfm.beginTransaction().replace(id, PhoneOTPSanderFragment::class.java, args).commit()
        }

        vBinding.buttonVerify.setOnClickListener {
            if (vBinding.progressBar.visibility != View.VISIBLE) {
                vBinding.progressBar.visibility = View.VISIBLE
                viewLifecycleOwner.lifecycleScope.launch {
                    val code = "${vBinding.textInputEditTextPhone.text}"
                    val verificationId = arguments?.getString(DATA_EXTRA_SESSION_INFO)
                        ?: throw NullPointerException("session_info")
                    val type =
                        AuthenticationType.Phone(verificationId = verificationId, code = code)
                    val res = authentication?.onAuthentication(type)?.lastOrNull()
                    if (res is Resources.Failure) {
                        Toast.makeText(context, res.value.message, Toast.LENGTH_LONG).show()
                    }
                    vBinding.progressBar.visibility = View.GONE
                }
            } else {
                Toast.makeText(it.context, "Please wait ...", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        countDownTimer.cancel()
        super.onDestroyView()
    }
}