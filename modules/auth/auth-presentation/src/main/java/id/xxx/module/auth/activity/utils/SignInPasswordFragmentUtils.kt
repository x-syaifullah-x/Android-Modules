package id.xxx.module.auth.activity.utils

import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.fragment.ForgetPasswordFragment
import id.xxx.module.auth.fragment.SignInPasswordFragment
import id.xxx.module.auth.fragment.SignInPhoneFragment
import id.xxx.module.auth.fragment.SignUpPasswordFragment
import id.xxx.module.auth.fragment.listener.ISignInPasswordFragment
import id.xxx.module.auth.ktx.getFragment
import id.xxx.module.auth.model.parms.SignInType
import id.xxx.module.auth.model.SignModel
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.common.Resources
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

class SignInPasswordFragmentUtils(
    private val activity: AuthActivity,
    action: ISignInPasswordFragment.Action,
    private val block: (SignInType) -> Flow<Resources<SignModel>>,
) {

    init {
        when (action) {
            is ISignInPasswordFragment.Action.ClickForgetPassword ->
                handleActionForgetPassword(action)

            is ISignInPasswordFragment.Action.ClickSignIn ->
                handleActionSignIn(action)

            is ISignInPasswordFragment.Action.ClickSignUp ->
                handleActionSignUp(action)

            is ISignInPasswordFragment.Action.ClickSignInWithPhone ->
                handleActionSignWithEmail(action)

            is ISignInPasswordFragment.Action.ClickSignInWithGoogle ->
                handleActionSignWithGoogle(action)
        }
    }

    private fun handleActionSignWithGoogle(action: ISignInPasswordFragment.Action.ClickSignInWithGoogle) {
        val signInType = SignInType.Google(
            token = action.token
        )
        block(signInType)
            .asLiveData()
            .observe(activity) { value ->
                val fragment = activity.getFragment<SignInPasswordFragment>()
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
                        activity.result(value.value)
                    }
                }
            }
    }

    private fun handleActionSignWithEmail(action: ISignInPasswordFragment.Action.ClickSignInWithPhone) {
        SignInputPreferences.setInputEmail(activity, action.email)
        activity.supportFragmentManager.beginTransaction().replace(
            AuthActivity.CONTAINER_ID,
            SignInPhoneFragment::class.java,
            null
        ).commit()
    }

    private fun handleActionSignUp(action: ISignInPasswordFragment.Action.ClickSignUp) {
        SignInputPreferences.setInputEmail(activity, action.email)
        activity.supportFragmentManager.beginTransaction().replace(
            AuthActivity.CONTAINER_ID,
            SignUpPasswordFragment::class.java,
            null
        ).commit()
    }

    private fun handleActionSignIn(action: ISignInPasswordFragment.Action.ClickSignIn) {
        SignInputPreferences.setInputEmail(activity, action.email)
        val fragment = activity.getFragment<SignInPasswordFragment>()
        val job = Job()
        val type = SignInType.Password(
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
                        activity.result(value.value)
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

    private fun handleActionForgetPassword(action: ISignInPasswordFragment.Action.ClickForgetPassword) {
        SignInputPreferences.setInputEmail(activity, action.email)
        activity.supportFragmentManager.beginTransaction().add(
            AuthActivity.CONTAINER_ID, ForgetPasswordFragment::class.java, null
        ).commit()
    }
}