package id.xxx.module.auth.activity.utils

import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.fragment.SignInPasswordFragment
import id.xxx.module.auth.fragment.SignUpPasswordFragment
import id.xxx.module.auth.fragment.SignUpPhoneFragment
import id.xxx.module.auth.fragment.listener.ISignUpPasswordFragment
import id.xxx.module.auth.ktx.getFragment
import id.xxx.module.auth.model.SignUpType
import id.xxx.module.auth.model.User
import id.xxx.module.auth.model.UserData
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.common.Resources
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

class SignUpPasswordFragmentUtils(
    private val activity: AuthActivity,
    action: ISignUpPasswordFragment.Action,
    private val block: (SignUpType) -> Flow<Resources<User>>,
) {

    init {
        when (action) {
            is ISignUpPasswordFragment.Action.ClickSignUp -> onClickSignUp(action)
            is ISignUpPasswordFragment.Action.ClickSignIn -> onClickSignIn(action)
            is ISignUpPasswordFragment.Action.ClickSignUpWithPhone -> onClickSignUpWithPhone(action)
        }
    }

    private fun onClickSignUpWithPhone(action: ISignUpPasswordFragment.Action.ClickSignUpWithPhone) {
        SignInputPreferences.setInputEmail(activity, action.email)
        activity.supportFragmentManager.beginTransaction()
            .replace(AuthActivity.CONTAINER_ID, SignUpPhoneFragment::class.java, null)
            .commit()
    }

    private fun onClickSignIn(action: ISignUpPasswordFragment.Action.ClickSignIn) {
        SignInputPreferences.setInputEmail(activity, action.email)
        activity.supportFragmentManager.beginTransaction()
            .replace(AuthActivity.CONTAINER_ID, SignInPasswordFragment::class.java, null)
            .commit()
    }

    private fun onClickSignUp(action: ISignUpPasswordFragment.Action.ClickSignUp) {
        val fragment = activity.getFragment<SignUpPasswordFragment>()
        val job = Job()
        val type = SignUpType.Password(
            password = action.password,
            data = UserData(
                email = action.email,
                phoneNumber = ""
            )
        )
        val liveData = block(type).asLiveData(job)
        val observer = object : Observer<Resources<User>> {
            override fun onChanged(value: Resources<User>) {
                when (value) {
                    is Resources.Loading -> {
                        fragment?.loadingVisible()
                    }

                    is Resources.Failure -> {
                        fragment?.loadingGone()
                        fragment?.showError(err = value.value)
                        liveData.removeObserver(this)
                    }

                    is Resources.Success -> {
                        fragment?.loadingGone()
                        liveData.removeObserver(this)
                        activity.result(value.value)
                    }
                }
            }
        }
        liveData.observe(activity, observer)
        fragment?.setSignUpOnCancel {
            fragment.loadingGone()
            fragment.showError(err = Throwable("Sign in canceled"))
            liveData.removeObserver(observer)
            job.cancel()
        }
    }
}