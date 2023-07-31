package id.xxx.module.auth.activity

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
import id.xxx.module.auth.activity.utils.SignUpPasswordFragmentUtils
import id.xxx.module.auth.activity.utils.SignUpPhoneFragmentUtils
import id.xxx.module.auth.fragment.ForgetPasswordFragment
import id.xxx.module.auth.fragment.SignInPasswordFragment
import id.xxx.module.auth.fragment.listener.IForgetPasswordFragment
import id.xxx.module.auth.fragment.listener.IOTPPhoneFragment
import id.xxx.module.auth.fragment.listener.ISignInPasswordFragment
import id.xxx.module.auth.fragment.listener.ISignInPhoneFragment
import id.xxx.module.auth.fragment.listener.ISignUpPasswordFragment
import id.xxx.module.auth.fragment.listener.ISignUpPhoneFragment
import id.xxx.module.auth.ktx.getFragment
import id.xxx.module.auth.ktx.isDarkThemeOn
import id.xxx.module.auth.model.Code
import id.xxx.module.auth.model.PasswordResetModel
import id.xxx.module.auth.model.SignInType
import id.xxx.module.auth.model.SignUpType
import id.xxx.module.auth.model.User
import id.xxx.module.auth.usecase.AuthUseCase
import id.xxx.module.auth.viewmodel.AuthViewModel
import id.xxx.module.auth.viewmodel.AuthViewModelProviderFactory
import id.xxx.module.auth_presentation.R
import id.xxx.module.common.Resources

open class AuthActivity(useCase: AuthUseCase) : AppCompatActivity(), ISignUpPasswordFragment,
    ISignInPasswordFragment,
    ISignUpPhoneFragment,
    ISignInPhoneFragment,
    IOTPPhoneFragment,
    IForgetPasswordFragment {

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
        val isTop =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.appTasks[0].taskInfo.numActivities
            } else {
                @Suppress("DEPRECATION")
                am.getRunningTasks(Int.MAX_VALUE)[0].numActivities
            } == 1

        val ivArrowBack = findViewById<ImageView>(R.id.iv_arrow_back)
        ivArrowBack.isVisible = !isTop
        ivArrowBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (!isDarkThemeOn()) {
            val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
            if (!windowInsetsController.isAppearanceLightStatusBars)
                windowInsetsController.isAppearanceLightStatusBars = true
        }

        if (savedInstanceState == null) {
            val fragmentHome = SignInPasswordFragment()
            supportFragmentManager.beginTransaction()
                .replace(CONTAINER_ID, fragmentHome, null)
                .commit()
        }
    }

    override fun onAction(action: ISignUpPasswordFragment.Action) {
        SignUpPasswordFragmentUtils(
            activity = this, action = action, block = viewModel::signUp
        )
    }

    override fun onAction(action: ISignInPasswordFragment.Action) {
        SignInPasswordFragmentUtils(
            activity = this, action = action, block = viewModel::signIn
        )
    }

    override fun onAction(action: ISignUpPhoneFragment.Action) {
        SignUpPhoneFragmentUtils(
            activity = this,
            action = action,
            block = viewModel::sendCode,
        )
    }

    override fun onAction(action: ISignInPhoneFragment.Action) {
        SignInPhoneFragmentUtils(
            action = action, activity = this, block = viewModel::sendCode
        )
    }

    override fun onAction(action: IOTPPhoneFragment.Action) {
        IOTPFragmentUtils(activity = this, action = action, block = { value ->
            if (value.isNewUser) {
                viewModel.signUp(
                    SignUpType.Phone(sessionInfo = value.sessionInfo, otp = value.otp)
                )
            } else {
                viewModel.signIn(
                    SignInType.Phone(sessionInfo = value.sessionInfo, otp = value.otp)
                )
            }.asLiveData()
        })
    }

    override fun onAction(action: IForgetPasswordFragment.Action) {
        fun resetLiveDataAndObserver() {
            observerForgetPassword?.apply { liveDataForgetPassword?.removeObserver(this) }
            liveDataForgetPassword = null
            observerForgetPassword = null
        }
        when (action) {
            is IForgetPasswordFragment.Action.Next -> {
                liveDataForgetPassword = viewModel.sendCode(
                    Code.PasswordReset(email = action.email)
                ).asLiveData()
                observerForgetPassword = Observer { resources ->
                    val forgetPasswordFragment = getFragment<ForgetPasswordFragment>()
                    when (resources) {
                        is Resources.Loading -> forgetPasswordFragment?.onLoading()
                        is Resources.Failure -> forgetPasswordFragment?.onError(resources.value)
                        is Resources.Success -> {
                            forgetPasswordFragment?.onSuccess()
                            resetLiveDataAndObserver()
                        }
                    }
                }
                observerForgetPassword?.apply {
                    liveDataForgetPassword?.observe(this@AuthActivity, this)
                }
            }

            is IForgetPasswordFragment.Action.Cancel -> {
                resetLiveDataAndObserver()
            }
        }
    }

    internal fun result(user: User) {
        val result = Intent()
            .putExtra(RESULT_USER, user)
        setResult(Activity.RESULT_OK, result)
        finishAfterTransition()
    }
}