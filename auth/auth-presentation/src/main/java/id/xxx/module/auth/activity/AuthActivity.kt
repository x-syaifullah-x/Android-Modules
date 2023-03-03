package id.xxx.module.auth.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.impl.OnBackPressedCallbackImpl
import id.xxx.module.auth.activity.utils.SignInPasswordUtils
import id.xxx.module.auth.activity.utils.SignInPhoneUtils
import id.xxx.module.auth.activity.utils.SignUpPasswordUtils
import id.xxx.module.auth.activity.utils.SignUpPhoneUtils
import id.xxx.module.auth.fragment.OTPFragment
import id.xxx.module.auth.fragment.SignInPasswordFragment
import id.xxx.module.auth.fragment.listener.*
import id.xxx.module.auth.ktx.getFragment
import id.xxx.module.auth.ktx.isDarkThemeOn
import id.xxx.module.auth.model.SignInType
import id.xxx.module.auth.model.SignUpType
import id.xxx.module.auth.model.User
import id.xxx.module.auth.usecase.AuthUseCase
import id.xxx.module.auth_presentation.R
import id.xxx.module.common.Resources
import kotlinx.coroutines.Job

open class AuthActivity(private val useCase: AuthUseCase) : AppCompatActivity(),
    ISignInPasswordFragment,
    ISignUpPasswordFragment,
    ISignInPhoneFragment,
    ISignUpPhoneFragment,
    IOTPFragment {

    companion object {
        val CONTAINER_ID = R.id.content
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

    override fun onAction(action: ISignInPasswordFragment.Action) {
        SignInPasswordUtils(
            activity = this,
            action = action,
            block = useCase::signIn
        )
    }

    override fun onAction(action: ISignUpPasswordFragment.Action) {
        SignUpPasswordUtils(
            activity = this,
            action = action,
            block = useCase::signUp
        )
    }

    override fun onAction(action: ISignInPhoneFragment.Action) {
        SignInPhoneUtils(
            action = action,
            activity = this,
            block = useCase::sendVerificationCode
        )
    }

    override fun onAction(action: ISignUpPhoneFragment.Action) {
        SignUpPhoneUtils(
            activity = this,
            action = action,
            block = useCase::sendVerificationCode,
        )
    }

    override fun onAction(action: IOTPFragment.Action) {
        when (action) {
            is IOTPFragment.Action.ClickNext -> {
                val job = Job()
                val fragment = getFragment<OTPFragment>()
                val liveData =
                    if (action.isNewUser) {
                        useCase.signUp(
                            SignUpType.Phone(sessionInfo = action.sessionInfo, otp = action.otp)
                        ).asLiveData(job)
                    } else {
                        useCase.signIn(
                            SignInType.Phone(sessionInfo = action.sessionInfo, otp = action.otp)
                        ).asLiveData(job)
                    }
                val observer = Observer<Resources<User>> {
                    when (it) {
                        is Resources.Loading -> {
                            fragment?.loadingVisible()
                        }
                        is Resources.Success -> {
                            fragment?.loadingGone()
                            job.cancel()
                            result(it.value)
                        }
                        is Resources.Failure -> {
                            fragment?.loadingGone()
                            fragment?.showError(it.value)
                            job.cancel()
                        }
                    }
                }
                liveData.observe(this, observer)
                fragment?.setCancelProcess {
                    fragment.loadingGone()
                    fragment.showError(Throwable("Cancel"))
                    job.cancel()
                }
            }
        }
    }

    private fun result(user: User) {
        Toast.makeText(this, "$user", Toast.LENGTH_LONG).show()
    }
}