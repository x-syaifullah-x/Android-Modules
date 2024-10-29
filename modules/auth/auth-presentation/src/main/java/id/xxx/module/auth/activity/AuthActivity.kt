package id.xxx.module.auth.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.impl.OnBackPressedCallbackImpl
import id.xxx.module.auth.activity.utils.IOTPFragmentUtils
import id.xxx.module.auth.activity.utils.PasswordRecoveryFragmentUtils
import id.xxx.module.auth.activity.utils.PhoneSignFragmentUtils
import id.xxx.module.auth.activity.utils.SignInPasswordFragmentUtils
import id.xxx.module.auth.activity.utils.SignUpPasswordFragmentUtils
import id.xxx.module.auth.fragment.password.PasswordSignInFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordRecoveryFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordSignInFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordSignUpFragment
import id.xxx.module.auth.fragment.phone.PhoneSignFragment
import id.xxx.module.auth.fragment.phone.PhoneSignOTPFragment
import id.xxx.module.auth.fragment.phone.listener.IPhoneSignFragment
import id.xxx.module.auth.fragment.phone.listener.IPhoneSignOTPFragment
import id.xxx.module.auth.fragment.recaptcha.RecaptchaFragment
import id.xxx.module.auth.fragment.recaptcha.listener.IRecaptchaFragment
import id.xxx.module.auth.ktx.isDarkThemeOn
import id.xxx.module.auth.model.PhoneVerificationModel
import id.xxx.module.auth.model.SignModel
import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.usecase.AuthUseCase
import id.xxx.module.auth.viewmodel.AuthViewModel
import id.xxx.module.auth.viewmodel.AuthViewModelProviderFactory
import id.xxx.module.common.Resources
import id.xxx.module.fragment.ktx.getFragment
import id.xxx.modules.auth.auth_presentation.R
import kotlinx.coroutines.Job

abstract class AuthActivity(useCase: AuthUseCase) : AppCompatActivity(),
    IPasswordSignInFragment,
    IPasswordSignUpFragment,
    IPhoneSignFragment,
    IPhoneSignOTPFragment,
    IPasswordRecoveryFragment,
    IRecaptchaFragment {

    companion object {
        internal const val CONTAINER_ID = android.R.id.content

        const val RESULT_USER = "a_s_d_f_g_h_j_k_L"
    }

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelProviderFactory(useCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onBackPressedCallbackImpl = OnBackPressedCallbackImpl(this)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallbackImpl)

        setTheme(R.style.Theme_Auth)

//        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val isTopActivity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            am.appTasks[0].taskInfo.numActivities
//        } else {
//            @Suppress("DEPRECATION") am.getRunningTasks(Int.MAX_VALUE)[0].numActivities
//        } == 1
//        val ivArrowBack = findViewById<ImageView>(R.id.iv_arrow_back)
//        ivArrowBack.isVisible = !isTopActivity
//        ivArrowBack.setOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
//        }

        if (!isDarkThemeOn()) {
            val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
            if (!windowInsetsController.isAppearanceLightStatusBars) windowInsetsController.isAppearanceLightStatusBars =
                true
        }

        if (savedInstanceState == null) {
            val fragmentHome = PasswordSignInFragment()
            supportFragmentManager.beginTransaction().replace(CONTAINER_ID, fragmentHome, null)
                .commit()
        }
    }

    override fun onAction(action: IPasswordSignInFragment.Action) {
        SignInPasswordFragmentUtils(
            activity = this, action = action, block = viewModel::sign
        )
    }

    override fun onAction(action: IPasswordSignUpFragment.Action) {
        SignUpPasswordFragmentUtils(
            activity = this,
            action = action,
            viewModel = viewModel,
        )
    }

    override fun onAction(action: IPhoneSignFragment.Action) {
        PhoneSignFragmentUtils(
            action = action,
            activity = this,
            viewModel = viewModel,
        )
    }

    override fun onAction(action: IPhoneSignOTPFragment.Action) {
        IOTPFragmentUtils(
            activity = this,
            action = action,
            viewModel = viewModel,
        )
    }

    override fun onAction(action: IPasswordRecoveryFragment.Action) {
        PasswordRecoveryFragmentUtils(
            activity = this,
            action = action,
            viewModel = viewModel
        )
    }

    override fun onAction(action: IRecaptchaFragment.Action) {
        val recaptchaFragment = getFragment<RecaptchaFragment>()
        if (recaptchaFragment != null)
            supportFragmentManager.beginTransaction().remove(recaptchaFragment).commit()
        val phoneSignFragment = getFragment<PhoneSignFragment>()
        when (action) {
            is IRecaptchaFragment.Action.Success -> {
                val job = Job()
                val code = Code.PhoneVerification(
                    phoneNumber = action.phoneNumber,
                    recaptchaResponse = action.response,
                )
                val liveData = viewModel.sendCode(code).asLiveData(job)
                val observer = object : Observer<Resources<PhoneVerificationModel>> {
                    override fun onChanged(value: Resources<PhoneVerificationModel>) {
                        when (value) {
                            is Resources.Loading -> phoneSignFragment?.loadingVisible()
                            is Resources.Success -> {
                                val sessionInfo = value.value.sessionInfo
                                val bundle = bundleOf(
                                    PhoneSignOTPFragment.KEY_SESSION_INFO to sessionInfo,
                                )
                                val transaction =
                                    supportFragmentManager.beginTransaction().add(
                                        CONTAINER_ID, PhoneSignOTPFragment::class.java, bundle,
                                    )
                                phoneSignFragment?.loadingGone()
                                transaction.commit()
                                liveData.removeObserver(this)
                                job.cancel()
                            }

                            is Resources.Failure -> {
                                phoneSignFragment?.loadingGone()
                                phoneSignFragment?.showError(err = value.value)
                                liveData.removeObserver(this)
                                job.cancel()
                            }
                        }
                    }
                }
                liveData.observe(phoneSignFragment?.viewLifecycleOwner ?: this, observer)
                phoneSignFragment?.setOnCancelProcess {
                    liveData.removeObserver(observer)
                    job.cancel()
                    phoneSignFragment.loadingGone()
                    phoneSignFragment.showError(Throwable("Sign in Canceled"))
                }
            }

            is IRecaptchaFragment.Action.Error -> {
                phoneSignFragment?.showError(action.err)
            }
        }
    }

    internal fun setResult(model: SignModel) {
        val result = Intent().putExtra(RESULT_USER, model)
        setResult(RESULT_OK, result)
        finishAfterTransition()
    }
}