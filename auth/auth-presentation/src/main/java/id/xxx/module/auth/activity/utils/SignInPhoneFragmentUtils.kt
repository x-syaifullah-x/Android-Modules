package id.xxx.module.auth.activity.utils

import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.fragment.OTPFragment
import id.xxx.module.auth.fragment.SignInPasswordFragment
import id.xxx.module.auth.fragment.SignInPhoneFragment
import id.xxx.module.auth.fragment.SignUpPhoneFragment
import id.xxx.module.auth.fragment.listener.ISignInPhoneFragment
import id.xxx.module.auth.ktx.getFragment
import id.xxx.module.auth.model.VerificationCodeResult
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.common.Resources
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

class SignInPhoneFragmentUtils(
    private val activity: AuthActivity,
    action: ISignInPhoneFragment.Action,
    private val block: (phoneNumber: String, recaptchaToken: String) -> Flow<Resources<VerificationCodeResult>>,
) {

    init {
        when (action) {
            is ISignInPhoneFragment.Action.ClickNext ->
                actionNext(action)
            is ISignInPhoneFragment.Action.ClickSignUp ->
                actionSignUp(action)
            is ISignInPhoneFragment.Action.ClickSignInWithEmail ->
                actionSignInWithEmail(action)
        }
    }

    private fun actionSignInWithEmail(action: ISignInPhoneFragment.Action.ClickSignInWithEmail) {
        SignInputPreferences.setInputPhone(activity, action.phoneNumber)
        activity.supportFragmentManager.beginTransaction()
            .replace(AuthActivity.CONTAINER_ID, SignInPasswordFragment::class.java, null)
            .commit()
    }

    private fun actionSignUp(action: ISignInPhoneFragment.Action.ClickSignUp) {
        SignInputPreferences.setInputPhone(activity, action.phoneNumber)
        activity.supportFragmentManager.beginTransaction()
            .replace(AuthActivity.CONTAINER_ID, SignUpPhoneFragment::class.java, null)
            .commit()
    }

    private fun actionNext(action: ISignInPhoneFragment.Action.ClickNext) {
        SignInputPreferences.setInputPhone(activity, action.phoneNumber)
        val fragment = activity.getFragment<SignInPhoneFragment>()
        val job = Job()
        val liveData = block(
            action.phoneNumber, action.recaptchaResponse
        ).asLiveData(job)
        val observer = object : Observer<Resources<VerificationCodeResult>> {
            override fun onChanged(value: Resources<VerificationCodeResult>) {
                when (value) {
                    is Resources.Loading -> fragment?.loadingVisible()
                    is Resources.Success -> {
                        val sessionInfo = value.value.sessionInfo
                        val bundle = bundleOf(
                            OTPFragment.KEY_SESSION_INFO to sessionInfo,
                            OTPFragment.KEY_IS_NEW_USER to false
                        )
                        val transaction = activity.supportFragmentManager
                            .beginTransaction()
                            .add(AuthActivity.CONTAINER_ID, OTPFragment::class.java, bundle)
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