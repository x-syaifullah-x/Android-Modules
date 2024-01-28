package id.xxx.module.auth.activity

import SignUpPasswordFragmentUtils
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
import id.xxx.module.auth.activity.impl.OnBackPressedCallbackImpl
import id.xxx.module.auth.activity.utils.IOTPFragmentUtils
import id.xxx.module.auth.activity.utils.PasswordRecoveryFragmentUtils
import id.xxx.module.auth.activity.utils.SignInPasswordFragmentUtils
import id.xxx.module.auth.activity.utils.SignInPhoneFragmentUtils
import id.xxx.module.auth.fragment.password.PasswordSignInFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordRecoveryFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordSignInFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordSignUpFragment
import id.xxx.module.auth.fragment.phone.listener.IPhoneSignFragment
import id.xxx.module.auth.fragment.phone.listener.IPhoneSignOTPFragment
import id.xxx.module.auth.ktx.isDarkThemeOn
import id.xxx.module.auth.model.SignModel
import id.xxx.module.auth.usecase.AuthUseCase
import id.xxx.module.auth.viewmodel.AuthViewModel
import id.xxx.module.auth.viewmodel.AuthViewModelProviderFactory
import id.xxx.module.auth_presentation.R

abstract class AuthActivity(useCase: AuthUseCase) : AppCompatActivity(),
    IPasswordSignInFragment,
    IPasswordSignUpFragment,
    IPhoneSignFragment,
    IPhoneSignOTPFragment,
    IPasswordRecoveryFragment {

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
        SignInPhoneFragmentUtils(
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

    internal fun setResult(model: SignModel) {
        val result = Intent().putExtra(RESULT_USER, model)
        setResult(RESULT_OK, result)
        finishAfterTransition()
    }
}