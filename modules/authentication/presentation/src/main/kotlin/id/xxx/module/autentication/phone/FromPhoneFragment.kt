package id.xxx.module.autentication.phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import id.xxx.module.viewbinding.ktx.viewBinding
import id.xxx.modules.authentication.presentation.databinding.FragmentPhoneBinding

class FromPhoneFragment : Fragment() {

    companion object {

        const val DATA_EXTRA_PHONE_NUMBER = "data_extra_phone_number"
        const val DATA_EXTRA_MESSAGE_ERROR = "data_extra_message_error"
    }

    private val vBinding by viewBinding<FragmentPhoneBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = vBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(DATA_EXTRA_MESSAGE_ERROR)?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            arguments?.remove(DATA_EXTRA_MESSAGE_ERROR)
        }

        val phoneNumber = arguments?.getString(DATA_EXTRA_PHONE_NUMBER)
        vBinding.textInputEditTextPhone.setText(phoneNumber)
        vBinding.textInputEditTextPhone.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                val hint =
                    if (hasFocus)
                        "Phone Number"
                    else
                        "Please enter phone number"
                vBinding.textInputLayoutPhone.hint = hint
            }

        vBinding.buttonNext.setOnClickListener {
            onClickButtonNet(view = it, phoneNumber = "${vBinding.textInputEditTextPhone.text}")
        }
    }

    private fun onClickButtonNet(view: View, phoneNumber: String) {
        requireActivity().supportFragmentManager.beginTransaction().add(
            parentFragment?.id ?: return,
            PhoneOTPSanderFragment::class.java,
            bundleOf(PhoneOTPSanderFragment.DATA_EXTRA_PHONE_NUMBER to phoneNumber),
            null
        ).commit()
    }
}