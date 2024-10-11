package id.xxx.module.auth.activity.utils

import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.fragment.password.PasswordSignInFragment
import id.xxx.module.auth.fragment.phone.PhoneSignFragment
import id.xxx.module.auth.fragment.password.PasswordSignUpFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordSignUpFragment
import id.xxx.module.auth.model.SignModel
import id.xxx.module.auth.model.parms.SignType
import id.xxx.module.auth.model.parms.UserData
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.viewmodel.AuthViewModel
import id.xxx.module.common.Resources
import id.xxx.module.fragment.ktx.getFragment
import kotlinx.coroutines.Job

class SignUpPasswordFragmentUtils(
    private val activity: AuthActivity,
    action: IPasswordSignUpFragment.Action,
    private val viewModel: AuthViewModel,
) {

    init {
        when (action) {
            is IPasswordSignUpFragment.Action.ClickSignUp -> onClickSignUp(action)
            is IPasswordSignUpFragment.Action.ClickSignIn -> onClickSignIn(action)
            is IPasswordSignUpFragment.Action.ClickSignUpWithPhone -> onClickSignUpWithPhone()
            is IPasswordSignUpFragment.Action.ClickSignInWithGoogle -> handleActionSignWithGoogle(
                action
            )
        }
    }

    private fun handleActionSignWithGoogle(action: IPasswordSignUpFragment.Action.ClickSignInWithGoogle) {
        val signInType = SignType.Google(
            token = action.token
        )
        viewModel.sign(signInType)
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

    private fun onClickSignUpWithPhone() {
        activity.supportFragmentManager.beginTransaction()
            .replace(AuthActivity.CONTAINER_ID, PhoneSignFragment::class.java, null)
            .commit()
    }

    private fun onClickSignIn(action: IPasswordSignUpFragment.Action.ClickSignIn) {
        SignInputPreferences.setInputEmail(activity, action.email)
        activity.supportFragmentManager.beginTransaction()
            .replace(AuthActivity.CONTAINER_ID, PasswordSignInFragment::class.java, null)
            .commit()
    }

    private fun onClickSignUp(action: IPasswordSignUpFragment.Action.ClickSignUp) {
        val fragment = activity.getFragment<PasswordSignUpFragment>()
        val job = Job()
        val type = SignType.PasswordUp(
            password = action.password,
            data = UserData(
                email = action.email,
                phoneNumber = ""
            )
        )
        val liveData = viewModel.sign(type).asLiveData(job)
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
        fragment?.setSignUpOnCancel {
            fragment.loadingGone()
            fragment.showError(err = Throwable("Sign in canceled"))
            liveData.removeObserver(observer)
            job.cancel()
        }
    }
}