package id.xxx.module.autentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import id.xxx.module.autentication.google.GoogleAccountResultContract
import id.xxx.module.autentication.password.FormLoginPasswordFragment
import id.xxx.module.autentication.password.FormSignupPasswordFragment
import id.xxx.module.autentication.phone.FromPhoneFragment
import id.xxx.module.autentication.phone.PhoneOTPSanderFragment
import id.xxx.module.autentication.phone.PhoneOTPVerifierFragment
import id.xxx.module.auth.domain.model.AuthenticationType
import id.xxx.module.common.Resources
import id.xxx.module.ktx.authentication
import id.xxx.module.viewbinding.ktx.viewBinding
import id.xxx.modules.authentication.presentation.R
import id.xxx.modules.authentication.presentation.databinding.FragmentAuthenticationBinding
import kotlinx.coroutines.launch

/**
 * EXAMPLE
 * ```
 * class ExampleActivity : AppCompatActivity(), IAuthentication {
 *
 *     private val authenticationViewModel: AuthenticationViewModel by viewModels { Factory }
 *     private val onBackPressedCallback = object : OnBackPressedCallback(true) {
 *         override fun handleOnBackPressed() = finishAfterTransition()
 *     }
 *
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         onBackPressedDispatcher.addCallback(onBackPressedCallback)
 *         if (savedInstanceState == null)
 *             supportFragmentManager.beginTransaction()
 *             .replace(android.R.id.content, AuthenticationFragment::class.java, null)
 *             .commit()
 *     }
 *
 *     override fun onAuthentication(type: AuthenticationType) = demoViewModel.authentication(type).map { res ->
 *         if (res is Resources.Success)
 *             Toast.makeText(this, "authentication success", Toast.LENGTH_LONG).show()
 *         return@map res
 *     }
 * }
 * ```
 */
class AuthenticationFragment : Fragment() {

    companion object {
        private const val KEY_STATE_ORDINAL = "KEY_STATE_SIGN"
    }

    private enum class State { SIGN_IN, SIGN_UP }

    private val vBinding by viewBinding<FragmentAuthenticationBinding>()

    private val googleLauncher =
        registerForActivityResult(GoogleAccountResultContract(), this::googleLauncherCallback)

    private fun googleLauncherCallback(account: GoogleSignInAccount?) =
        lifecycleScope.launch {
            val authType = AuthenticationType.Google(account?.idToken ?: return@launch)
            authentication?.onAuthentication(authType)?.collect { res ->
                vBinding.btnContinueWithGoogle.isEnabled = res !is Resources.Loading
                val progress = vBinding.progressAuthGoogle
                when (res) {
                    is Resources.Loading -> progress.visibility = View.VISIBLE
                    is Resources.Success -> progress.visibility = View.GONE
                    is Resources.Failure -> {
                        progress.visibility = View.GONE
                        Toast.makeText(context, res.value.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() = this@AuthenticationFragment.handleOnBackPressed()
    }

    private var _state = State.SIGN_IN

    private fun handleOnBackPressed() {
        val ft = parentFragmentManager.beginTransaction()
        when (val lastFragment = parentFragmentManager.fragments.lastOrNull()) {
            is PhoneOTPVerifierFragment -> ft.remove(lastFragment).commit()
            is PhoneOTPSanderFragment -> ft.remove(lastFragment).commit()
            else -> {
                onBackPressedCallback.isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = vBinding.root.apply {

        _state = State.entries[savedInstanceState?.getInt(KEY_STATE_ORDINAL) ?: 0]

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, onBackPressedCallback)

        if (background == null)
            background = activity?.window?.decorView?.background

        setForm(childFragmentManager.findFragmentById(vBinding.containerForm.id))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vBinding.btnContinueWithPhone.setOnClickListener(this::onClickContinueWithPhone)
        vBinding.btnContinueWithPassword.setOnClickListener(this::onClickContinueWithPassword)
        vBinding.btnContinueWithGoogle.setOnClickListener(this::onClickContinueWithGoogle)
        vBinding.btnToggleAuth.setOnClickListener(this::onClickToggleAuth)
    }

    private fun onClickToggleAuth(v: View) {
        if (_state == State.SIGN_IN) {
            setForm(FormSignupPasswordFragment())
        } else {
            setForm(FormLoginPasswordFragment())
        }
    }

    private fun onClickContinueWithGoogle(v: View) = googleLauncher.launch(null)

    private fun onClickContinueWithPassword(v: View) =
        if (_state == State.SIGN_IN)
            setForm(FormLoginPasswordFragment())
        else
            setForm(FormSignupPasswordFragment())

    private fun onClickContinueWithPhone(v: View) =
        setForm(FromPhoneFragment())

    private fun setForm(fragment: Fragment?, tag: String? = null) {
        when (fragment) {
            is FormSignupPasswordFragment -> {
                _state = State.SIGN_UP
                setTitleAndToggle(R.string.title_sign_up, R.array.already_have_an_account)
                vBinding.btnContinueWithPhone.visibility = View.VISIBLE
                vBinding.btnContinueWithPassword.visibility = View.INVISIBLE
            }

            is FromPhoneFragment -> {
                setTitleAndToggle(R.string.title_sign_phone)
                vBinding.btnContinueWithPhone.visibility = View.INVISIBLE
                vBinding.btnContinueWithPassword.visibility = View.VISIBLE
            }

            else -> {
                _state = State.SIGN_IN
                setTitleAndToggle(R.string.title_sign_in, R.array.don_t_have_an_account)
                vBinding.btnContinueWithPhone.visibility = View.VISIBLE
                vBinding.btnContinueWithPassword.visibility = View.INVISIBLE
            }
        }
        childFragmentManager.beginTransaction()
            .replace(vBinding.containerForm.id, fragment ?: FormLoginPasswordFragment(), tag)
            .commitNow()
    }

    private fun setTitleAndToggle(titleKey: Int, toggleKey: Int? = null) {
        vBinding.textTitle.text = getString(titleKey)
        if (toggleKey != null) {
            val data = context?.resources?.getStringArray(toggleKey)
            vBinding.tvToggleAuth.text = data?.get(0) ?: ""
            vBinding.btnToggleAuth.text = data?.get(1) ?: ""
            vBinding.tvToggleAuth.visibility = View.VISIBLE
            vBinding.btnToggleAuth.visibility = View.VISIBLE
        } else {
            vBinding.tvToggleAuth.visibility = View.GONE
            vBinding.btnToggleAuth.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_STATE_ORDINAL, _state.ordinal)
        super.onSaveInstanceState(outState)
    }

    /* MEMORY LEAK WITH CredentialManager.create() */
//    private fun continueWithGoogle(c: FragmentActivity) {
//        c.lifecycleScope.launch {
//            val googleIdOption = GetGoogleIdOption.Builder()
//                .setFilterByAuthorizedAccounts(true)
//                .setServerClientId("1098413132051-k1fm576dsbahfigib4e480sa4hflrah7.apps.googleusercontent.com")
//                .setAutoSelectEnabled(false)
//                .build()
//            val request = GetCredentialRequest.Builder()
//                .addCredentialOption(googleIdOption)
//                .build()
//            val credentialManager = CredentialManager.create(c)
//            try {
//                val gcr = credentialManager.getCredential(c, request)
//                val credential = gcr.credential
//                val type = credential.type
//                if (type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
//                    val googleIdTokenCredential = GoogleIdTokenCredential
//                        .createFrom(gcr.credential.data)
//                    val idToken = googleIdTokenCredential.idToken
//                    val r = iSign()?.onSign(TypeSign.Google(idToken = idToken))
//                    if (r is SignResult.Success) {
//                        requireActivity().onBackPressedDispatcher.onBackPressed()
//                    }
//                } else {
//                    throw NotImplementedError()
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//            } finally {
//                cancel()
//            }
//        }
//    }
}