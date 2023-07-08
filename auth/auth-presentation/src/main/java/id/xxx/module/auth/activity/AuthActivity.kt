package id.xxx.module.auth.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.impl.OnBackPressedCallbackImpl
import id.xxx.module.auth.activity.utils.IOTPFragmentUtils
import id.xxx.module.auth.activity.utils.SignInPasswordFragmentUtils
import id.xxx.module.auth.activity.utils.SignInPhoneFragmentUtils
import id.xxx.module.auth.activity.utils.SignUpPasswordFragmentUtils
import id.xxx.module.auth.activity.utils.SignUpPhoneFragmentUtils
import id.xxx.module.auth.fragment.SignInPasswordFragment
import id.xxx.module.auth.fragment.listener.IOTPFragment
import id.xxx.module.auth.fragment.listener.ISignInPasswordFragment
import id.xxx.module.auth.fragment.listener.ISignInPhoneFragment
import id.xxx.module.auth.fragment.listener.ISignUpPasswordFragment
import id.xxx.module.auth.fragment.listener.ISignUpPhoneFragment
import id.xxx.module.auth.ktx.isDarkThemeOn
import id.xxx.module.auth.model.SignInType
import id.xxx.module.auth.model.SignUpType
import id.xxx.module.auth.model.User
import id.xxx.module.auth.usecase.AuthUseCase
import id.xxx.module.auth.viewmodel.AuthViewModel
import id.xxx.module.auth.viewmodel.AuthViewModelProviderFactory
import id.xxx.module.auth_presentation.R

open class AuthActivity(useCase: AuthUseCase) : AppCompatActivity(),
    ISignUpPasswordFragment,
    ISignInPasswordFragment,
    ISignUpPhoneFragment,
    ISignInPhoneFragment,
    IOTPFragment {

    companion object {
        internal val CONTAINER_ID = R.id.content

        const val RESULT_USER = "a_s_d_f_g_h_j_k_L"
    }

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelProviderFactory(useCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher
            .addCallback(this, OnBackPressedCallbackImpl(this))

        setTheme(R.style.Theme_Auth)

        setContentView(R.layout.auth_activity)

        if (!isDarkThemeOn()) {
            val windowInsetsController = WindowInsetsControllerCompat(window, window.decorView)
            if (!windowInsetsController.isAppearanceLightStatusBars)
                windowInsetsController.isAppearanceLightStatusBars = true
        }

        val fragmentHome = SignInPasswordFragment()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(CONTAINER_ID, fragmentHome, null)
                .commit()
        }
    }

    override fun onAction(action: ISignUpPasswordFragment.Action) {
        SignUpPasswordFragmentUtils(
            activity = this,
            action = action,
            block = viewModel::signUp
        )
    }

    override fun onAction(action: ISignInPasswordFragment.Action) {
        SignInPasswordFragmentUtils(
            activity = this,
            action = action,
            block = viewModel::signIn
        )
    }

    override fun onAction(action: ISignUpPhoneFragment.Action) {
        SignUpPhoneFragmentUtils(
            activity = this,
            action = action,
            block = viewModel::sendVerificationCode,
        )
    }

    override fun onAction(action: ISignInPhoneFragment.Action) {
        SignInPhoneFragmentUtils(
            action = action,
            activity = this,
            block = viewModel::sendVerificationCode
        )
    }

    override fun onAction(action: IOTPFragment.Action) {
        IOTPFragmentUtils(
            activity = this,
            action = action,
            block = { _action ->
                if (_action.isNewUser) {
                    viewModel.signUp(
                        SignUpType.Phone(sessionInfo = _action.sessionInfo, otp = _action.otp)
                    )
                } else {
                    viewModel.signIn(
                        SignInType.Phone(sessionInfo = _action.sessionInfo, otp = _action.otp)
                    )
                }.asLiveData()
            }
        )
    }

    fun result(user: User) {
        val result = Intent()
        result.putExtra(RESULT_USER, user)
        setResult(Activity.RESULT_OK, result)
        finishAfterTransition()
    }
}