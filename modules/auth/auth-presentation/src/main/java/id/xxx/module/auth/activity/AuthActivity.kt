package id.xxx.module.auth.activity

import SignUpPasswordFragmentUtils
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.impl.OnBackPressedCallbackImpl
import id.xxx.module.auth.activity.utils.IOTPFragmentUtils
import id.xxx.module.auth.activity.utils.SignInPasswordFragmentUtils
import id.xxx.module.auth.activity.utils.SignInPhoneFragmentUtils
import id.xxx.module.auth.fragment.password.PasswordRecoveryFragment
import id.xxx.module.auth.fragment.password.PasswordSignInFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordRecoveryFragment
import id.xxx.module.auth.fragment.phone.listener.IPhoneSignOTPFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordSignInFragment
import id.xxx.module.auth.fragment.phone.listener.IPhoneSignFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordSignUpFragment
import id.xxx.module.auth.ktx.isDarkThemeOn
import id.xxx.module.auth.model.PasswordResetModel
import id.xxx.module.auth.model.SignModel
import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.model.parms.SignType
import id.xxx.module.auth.usecase.AuthUseCase
import id.xxx.module.auth.viewmodel.AuthViewModel
import id.xxx.module.auth.viewmodel.AuthViewModelProviderFactory
import id.xxx.module.auth_presentation.R
import id.xxx.module.common.Resources
import id.xxx.module.fragment.ktx.getFragment

open class AuthActivity(useCase: AuthUseCase) : AppCompatActivity(),
    IPasswordSignInFragment,
    IPasswordSignUpFragment,
    IPhoneSignFragment,
    IPhoneSignOTPFragment,
    IPasswordRecoveryFragment {

    companion object {
        internal val CONTAINER_ID = R.id.content

        const val RESULT_USER = "a_s_d_f_g_h_j_k_L"
    }

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelProviderFactory(useCase)
    }

    private var liveDataForgetPassword: LiveData<Resources<PasswordResetModel>>? = null
    private var observerForgetPassword: Observer<Resources<PasswordResetModel>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val onBackPressedCallbackImpl = OnBackPressedCallbackImpl(this)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallbackImpl)

        setTheme(R.style.Theme_Auth)

        setContentView(R.layout.auth_activity)

        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val isTopActivity =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.appTasks[0].taskInfo.numActivities
            } else {
                @Suppress("DEPRECATION")
                am.getRunningTasks(Int.MAX_VALUE)[0].numActivities
            } == 1

        val ivArrowBack = findViewById<ImageView>(R.id.iv_arrow_back)
        ivArrowBack.isVisible = !isTopActivity
        ivArrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (!isDarkThemeOn()) {
            val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
            if (!windowInsetsController.isAppearanceLightStatusBars)
                windowInsetsController.isAppearanceLightStatusBars = true
        }

        if (savedInstanceState == null) {
            val fragmentHome = PasswordSignInFragment()
            supportFragmentManager.beginTransaction()
                .replace(CONTAINER_ID, fragmentHome, null)
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
            activity = this, action = action, block = viewModel::sign
        )
    }

    override fun onAction(action: IPhoneSignFragment.Action) {
        SignInPhoneFragmentUtils(
            action = action,
            activity = this,
            viewModel = viewModel
        )
    }

    override fun onAction(action: IPhoneSignOTPFragment.Action) {
        IOTPFragmentUtils(activity = this, action = action, block = { value ->
            if (value.isNewUser) {
                viewModel.sign(
                    SignType.PhoneUp(sessionInfo = value.sessionInfo, otp = value.otp)
                )
            } else {
                viewModel.sign(
                    SignType.PhoneIn(sessionInfo = value.sessionInfo, otp = value.otp)
                )
            }.asLiveData()
        })
    }

    override fun onAction(action: IPasswordRecoveryFragment.Action) {
        fun resetLiveDataAndObserver() {
            observerForgetPassword?.apply { liveDataForgetPassword?.removeObserver(this) }
            liveDataForgetPassword = null
            observerForgetPassword = null
        }
        when (action) {
            is IPasswordRecoveryFragment.Action.Next -> {
                liveDataForgetPassword = viewModel.sendCode(
                    Code.PasswordReset(email = action.email)
                ).asLiveData()
                observerForgetPassword = Observer { resources ->
                    val passwordRecoveryFragment = getFragment<PasswordRecoveryFragment>()
                    when (resources) {
                        is Resources.Loading -> passwordRecoveryFragment?.onLoading()
                        is Resources.Failure -> passwordRecoveryFragment?.onError(resources.value)
                        is Resources.Success -> {
                            passwordRecoveryFragment?.onSuccess()
                            resetLiveDataAndObserver()
                        }
                    }
                }
                observerForgetPassword?.apply {
                    liveDataForgetPassword?.observe(this@AuthActivity, this)
                }
            }

            is IPasswordRecoveryFragment.Action.Cancel -> {
                resetLiveDataAndObserver()
            }
        }
    }

    internal fun result(model: SignModel) {
        val result = Intent().putExtra(RESULT_USER, model)
        setResult(Activity.RESULT_OK, result)
        finishAfterTransition()
    }
}