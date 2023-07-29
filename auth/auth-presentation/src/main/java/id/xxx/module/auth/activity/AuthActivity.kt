package id.xxx.module.auth.activity

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.impl.OnBackPressedCallbackImpl
import id.xxx.module.auth.activity.utils.IOTPFragmentUtils
import id.xxx.module.auth.activity.utils.SignInPasswordFragmentUtils
import id.xxx.module.auth.activity.utils.SignInPhoneFragmentUtils
import id.xxx.module.auth.activity.utils.SignUpPasswordFragmentUtils
import id.xxx.module.auth.activity.utils.SignUpPhoneFragmentUtils
import id.xxx.module.auth.fragment.NewPasswordFragment
import id.xxx.module.auth.fragment.SignInPasswordFragment
import id.xxx.module.auth.fragment.listener.IForgetPasswordFragment
import id.xxx.module.auth.fragment.listener.INewPasswordFragment
import id.xxx.module.auth.fragment.listener.IOTPPhoneFragment
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
import id.xxx.module.common.Resources

open class AuthActivity(useCase: AuthUseCase) : AppCompatActivity(), ISignUpPasswordFragment,
    ISignInPasswordFragment,
    ISignUpPhoneFragment,
    ISignInPhoneFragment,
    IOTPPhoneFragment,
    IForgetPasswordFragment,
    INewPasswordFragment {

    companion object {
        internal val CONTAINER_ID = R.id.content

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

        val data = intent.data?.toString()
        if (data != null) {
            val uri = Uri.parse(data)
            val mode = uri.getQueryParameter("mode")
            val oobCode = uri.getQueryParameter("oobCode")
            val apiKey = uri.getQueryParameter("apiKey")
            val lang = uri.getQueryParameter("lang")
            val args = Bundle()
            args.putString(NewPasswordFragment.KEY_OOB_CODE, oobCode)
            supportFragmentManager.beginTransaction()
                .replace(CONTAINER_ID, NewPasswordFragment::class.java, args, null)
                .commit()
        } else if (savedInstanceState == null) {
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
            block = viewModel::sendVerificationCode,
        )
    }

    override fun onAction(action: ISignInPhoneFragment.Action) {
        SignInPhoneFragmentUtils(
            action = action, activity = this, block = viewModel::sendVerificationCode
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
        val liveData = viewModel.sendOobCode(action.email).asLiveData()
        var observer: Observer<Resources<String>>? = null
        observer = Observer { resources ->
            when (resources) {
                is Resources.Loading -> action.onLoading()
                is Resources.Success -> {
                    action.onSuccess()
                    val observerFinal = observer
                    if (observerFinal != null)
                        liveData.removeObserver(observerFinal)
                }

                is Resources.Failure -> action.onError(resources.value)
            }
        }
        liveData.observe(this, observer)
    }

    override fun onAction(action: INewPasswordFragment.Action) {
        viewModel.resetPassword(
            oobCode = action.oobCode,
            newPassword = action.newPassword
        ).asLiveData().observe(this) { resources ->
            println(resources)
            when (resources) {
                is Resources.Loading -> {

                }

                is Resources.Failure -> {

                }

                is Resources.Success -> {
                    supportFragmentManager.beginTransaction()
                        .replace(CONTAINER_ID, SignInPasswordFragment::class.java, null)
                        .commit()
                }
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