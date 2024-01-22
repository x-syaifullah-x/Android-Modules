package id.xxx.module.auth.activity.utils

import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.fragment.password.PasswordRecoveryFragment
import id.xxx.module.auth.fragment.password.PasswordSignInFragment
import id.xxx.module.auth.fragment.phone.PhoneSignFragment
import id.xxx.module.auth.fragment.password.PasswordSignUpFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordSignInFragment
import id.xxx.module.fragment.ktx.getFragment
import id.xxx.module.auth.model.SignModel
import id.xxx.module.auth.model.parms.SignType
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.common.Resources
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

class SignInPasswordFragmentUtils(
    private val activity: AuthActivity,
    action: IPasswordSignInFragment.Action,
    private val block: (SignType) -> Flow<Resources<SignModel>>,
) {

    init {
        when (action) {
            is IPasswordSignInFragment.Action.ClickForgetPassword ->
                handleActionForgetPassword(action)

            is IPasswordSignInFragment.Action.ClickSignIn ->
                handleActionSignIn(action)

            is IPasswordSignInFragment.Action.ClickSignUp ->
                handleActionSignUp(action)

            is IPasswordSignInFragment.Action.ClickContinueWithPhone ->
                handleActionSignWithEmail(action)

            is IPasswordSignInFragment.Action.ClickContinueWithGoogle ->
                handleActionSignWithGoogle(action)
        }
    }

    private fun handleActionSignWithGoogle(action: IPasswordSignInFragment.Action.ClickContinueWithGoogle) {
        val signInType = SignType.Google(
            token = action.token
        )
        block(signInType)
            .asLiveData()
            .observe(activity) { value ->
                val fragment = activity.getFragment<PasswordSignInFragment>()
                when (value) {
                    is Resources.Loading -> {
                        fragment?.loadingVisible()
                    }

                    is Resources.Failure -> {
                        fragment?.loadingGone()
                        fragment?.showError(err = value.value)
                    }

                    is Resources.Success -> {
                        fragment?.loadingGone()
                        activity.setResult(value.value)
                    }
                }
            }
    }

    private fun handleActionSignWithEmail(action: IPasswordSignInFragment.Action.ClickContinueWithPhone) {
        SignInputPreferences.setInputEmail(activity, action.email)
        activity.supportFragmentManager.beginTransaction().replace(
            AuthActivity.CONTAINER_ID,
            PhoneSignFragment::class.java,
            null
        ).commit()
    }

    private fun handleActionSignUp(action: IPasswordSignInFragment.Action.ClickSignUp) {
        SignInputPreferences.setInputEmail(activity, action.email)
        activity.supportFragmentManager.beginTransaction().replace(
            AuthActivity.CONTAINER_ID,
            PasswordSignUpFragment::class.java,
            null
        ).commit()
    }

    private fun handleActionSignIn(action: IPasswordSignInFragment.Action.ClickSignIn) {
        SignInputPreferences.setInputEmail(activity, action.email)
        val fragment = activity.getFragment<PasswordSignInFragment>()
        val job = Job()
        val type = SignType.PasswordIn(
            email = action.email,
            password = action.password,
        )
        val liveData = block(type).asLiveData(job)
        val observer = object : Observer<Resources<SignModel>> {
            override fun onChanged(value: Resources<SignModel>) {
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
                        activity.setResult(value.value)
                    }
                }
            }
        }
        liveData.observe(activity, observer)
        fragment?.setSignInOnCancel {
            fragment.loadingGone()
            fragment.showError(err = Throwable("Sign in canceled"))
            liveData.removeObserver(observer)
            job.cancel()
        }
    }

    private fun handleActionForgetPassword(action: IPasswordSignInFragment.Action.ClickForgetPassword) {
        SignInputPreferences.setInputEmail(activity, action.email)
        activity.supportFragmentManager.beginTransaction().add(
            AuthActivity.CONTAINER_ID, PasswordRecoveryFragment::class.java, null
        ).commit()
    }
}