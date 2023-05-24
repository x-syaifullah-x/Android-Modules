package id.xxx.module.auth.activity.utils

import androidx.core.os.bundleOf
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.fragment.*
import id.xxx.module.auth.fragment.listener.ISignUpPhoneFragment
import id.xxx.module.auth.ktx.getFragment
import id.xxx.module.auth.model.VerificationCodeResult
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.common.Resources
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

class SignUpPhoneFragmentUtils(
    private val activity: AuthActivity,
    action: ISignUpPhoneFragment.Action,
    private val block: (phoneNumber: String, recaptchaToken: String) -> Flow<Resources<VerificationCodeResult>>,
) {

    init {
        when (action) {
            is ISignUpPhoneFragment.Action.ClickNext -> actionClickNext(action)
            is ISignUpPhoneFragment.Action.ClickSignIn -> actionClickSignIn(action)
            is ISignUpPhoneFragment.Action.ClickSignUpWithEmail -> actionClickSignUpWithEmail(action)
        }
    }

    private fun actionClickSignUpWithEmail(action: ISignUpPhoneFragment.Action.ClickSignUpWithEmail) {
        SignInputPreferences.setInputPhone(activity, action.phoneNumber)
        activity.supportFragmentManager.beginTransaction()
            .replace(AuthActivity.CONTAINER_ID, SignUpPasswordFragment::class.java, null)
            .commit()
    }

    private fun actionClickSignIn(action: ISignUpPhoneFragment.Action.ClickSignIn) {
        SignInputPreferences.setInputPhone(activity, action.phoneNumber)
        activity.supportFragmentManager.beginTransaction()
            .replace(AuthActivity.CONTAINER_ID, SignInPhoneFragment::class.java, null)
            .commit()
    }

    private fun actionClickNext(action: ISignUpPhoneFragment.Action.ClickNext) {
        val fragment = activity.getFragment<SignUpPhoneFragment>()
        val job = Job()
        val liveData = block(action.phoneNumber, action.recaptchaResponse)
            .asLiveData(job)
        liveData.observe(activity) {
            when (it) {
                is Resources.Loading -> {
                    fragment?.loadingVisible()
                }
                is Resources.Success -> {
                    fragment?.loadingGone()
                    val bundle = bundleOf(
                        OTPFragment.KEY_SESSION_INFO to it.value.sessionInfo,
                        OTPFragment.KEY_IS_NEW_USER to true,
                    )
                    activity.supportFragmentManager.beginTransaction()
                        .add(AuthActivity.CONTAINER_ID, OTPFragment::class.java, bundle)
                        .commit()
                }
                is Resources.Failure -> {
                    fragment?.loadingGone()
                    fragment?.showError(it.value)
                }
            }
        }
        fragment?.setSignUpOnCancel {
            job.cancel()
            fragment.loadingGone()
            fragment.showError(Throwable("Canceled"))
        }
    }
}