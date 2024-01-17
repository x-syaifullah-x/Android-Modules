package id.xxx.module.auth.activity.utils

import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.fragment.OTPPhoneFragment
import id.xxx.module.auth.fragment.SignInPasswordFragment
import id.xxx.module.auth.fragment.SignInPhoneFragment
import id.xxx.module.auth.fragment.listener.ISignInPhoneFragment
import id.xxx.module.auth.model.PhoneVerificationModel
import id.xxx.module.auth.model.SignModel
import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.model.parms.SignType
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.auth.viewmodel.AuthViewModel
import id.xxx.module.common.Resources
import id.xxx.module.fragment.ktx.getFragment
import kotlinx.coroutines.Job

class SignInPhoneFragmentUtils(
    private val activity: AuthActivity,
    action: ISignInPhoneFragment.Action,
    private val viewModel: AuthViewModel
) {

    init {
        when (action) {
            is ISignInPhoneFragment.Action.ClickNext ->
                actionNext(action)

            is ISignInPhoneFragment.Action.ClickSignInWithEmail ->
                actionSignInWithEmail(action)

            is ISignInPhoneFragment.Action.ClickSignInWithGoogle ->
                actionSignInWithGoogle(action)
        }
    }

    private fun actionSignInWithGoogle(action: ISignInPhoneFragment.Action.ClickSignInWithGoogle) {
        val job = Job()
        val liveData = viewModel.sign(SignType.Google(action.token)).asLiveData(job)
        val fragment = activity.getFragment<SignInPhoneFragment>()
        val observer = object : Observer<Resources<SignModel>> {
            override fun onChanged(value: Resources<SignModel>) {
                when (value) {
                    is Resources.Loading -> fragment?.loadingVisible()
                    is Resources.Success -> {
                        activity.result(value.value)
                        liveData.removeObserver(this)
                        job.cancel()
                    }

                    is Resources.Failure -> {
                        fragment?.loadingGone()
                        fragment?.showError(err = value.value)
                        liveData.removeObserver(this)
                        job.cancel()
                    }
                }
            }
        }
        liveData.observe(activity, observer)
        fragment?.setSignInOnCancel {
            liveData.removeObserver(observer)
            job.cancel()
            fragment.loadingGone()
            fragment.showError(Throwable("Sign in Canceled"))
        }

    }

    private fun actionSignInWithEmail(action: ISignInPhoneFragment.Action.ClickSignInWithEmail) {
        SignInputPreferences.setInputPhone(activity, action.phoneNumber)
        activity.supportFragmentManager.beginTransaction()
            .replace(AuthActivity.CONTAINER_ID, SignInPasswordFragment::class.java, null)
            .commit()
    }

    private fun actionNext(action: ISignInPhoneFragment.Action.ClickNext) {
        SignInputPreferences.setInputPhone(activity, action.phoneNumber)
        val fragment = activity.getFragment<SignInPhoneFragment>()
        val job = Job()
        val code = Code.PhoneVerification(
            phoneNumber = action.phoneNumber, recaptchaResponse = action.recaptchaResponse
        )
        val liveData = viewModel.sendCode(code).asLiveData(job)
        val observer = object : Observer<Resources<PhoneVerificationModel>> {
            override fun onChanged(value: Resources<PhoneVerificationModel>) {
                when (value) {
                    is Resources.Loading -> fragment?.loadingVisible()
                    is Resources.Success -> {
                        val sessionInfo = value.value.sessionInfo
                        val bundle = bundleOf(
                            OTPPhoneFragment.KEY_SESSION_INFO to sessionInfo,
                            OTPPhoneFragment.KEY_IS_NEW_USER to false
                        )
                        val transaction = activity.supportFragmentManager
                            .beginTransaction()
                            .add(AuthActivity.CONTAINER_ID, OTPPhoneFragment::class.java, bundle)
                        fragment?.loadingGone()
                        transaction.commit()
                        liveData.removeObserver(this)
                        job.cancel()
                    }

                    is Resources.Failure -> {
                        fragment?.loadingGone()
                        fragment?.showError(err = value.value)
                        liveData.removeObserver(this)
                        job.cancel()
                    }
                }
            }
        }
        liveData.observe(activity, observer)
        fragment?.setSignInOnCancel {
            liveData.removeObserver(observer)
            job.cancel()
            fragment.loadingGone()
            fragment.showError(Throwable("Sign in Canceled"))
        }
    }
}