package id.xxx.module.auth.activity

import android.os.Bundle
import android.widget.Toast
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
import id.xxx.module.auth_presentation.R

open class AuthActivity(private val useCase: AuthUseCase) : AppCompatActivity(),
    ISignUpPasswordFragment,
    ISignInPasswordFragment,
    ISignUpPhoneFragment,
    ISignInPhoneFragment,
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

    override fun onAction(action: ISignUpPasswordFragment.Action) {
        SignUpPasswordFragmentUtils(
            activity = this,
            action = action,
            block = useCase::signUp
        )
    }

    override fun onAction(action: ISignInPasswordFragment.Action) {
        SignInPasswordFragmentUtils(
            activity = this,
            action = action,
            block = useCase::signIn
        )
    }

    override fun onAction(action: ISignUpPhoneFragment.Action) {
        SignUpPhoneFragmentUtils(
            activity = this,
            action = action,
            block = useCase::sendVerificationCode,
        )
    }

    override fun onAction(action: ISignInPhoneFragment.Action) {
        SignInPhoneFragmentUtils(
            action = action,
            activity = this,
            block = useCase::sendVerificationCode
        )
    }

    override fun onAction(action: IOTPFragment.Action) {
        IOTPFragmentUtils(
            activity = this,
            action = action,
            block = { _action ->
                if (_action.isNewUser) {
                    useCase.signUp(
                        SignUpType.Phone(sessionInfo = _action.sessionInfo, otp = _action.otp)
                    )
                } else {
                    useCase.signIn(
                        SignInType.Phone(sessionInfo = _action.sessionInfo, otp = _action.otp)
                    )
                }.asLiveData()
            }
        )
    }

    fun result(user: User) {
        Toast.makeText(this, "$user", Toast.LENGTH_LONG).show()
    }
}