import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.fragment.SignInPasswordFragment
import id.xxx.module.auth.fragment.SignInPhoneFragment
import id.xxx.module.auth.fragment.SignUpPasswordFragment
import id.xxx.module.auth.fragment.listener.ISignUpPasswordFragment
import id.xxx.module.auth.model.SignModel
import id.xxx.module.auth.model.parms.SignType
import id.xxx.module.auth.model.parms.UserData
import id.xxx.module.auth.preferences.SignInputPreferences
import id.xxx.module.common.Resources
import id.xxx.module.fragment.ktx.getFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

class SignUpPasswordFragmentUtils(
    private val activity: AuthActivity,
    action: ISignUpPasswordFragment.Action,
    private val block: (SignType) -> Flow<Resources<SignModel>>,
) {

    init {
        when (action) {
            is ISignUpPasswordFragment.Action.ClickSignUp -> onClickSignUp(action)
            is ISignUpPasswordFragment.Action.ClickSignIn -> onClickSignIn(action)
            is ISignUpPasswordFragment.Action.ClickSignUpWithPhone -> onClickSignUpWithPhone()
            is ISignUpPasswordFragment.Action.ClickSignInWithGoogle -> handleActionSignWithGoogle(
                action
            )
        }
    }

    private fun handleActionSignWithGoogle(action: ISignUpPasswordFragment.Action.ClickSignInWithGoogle) {
        val signInType = SignType.Google(
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

    private fun onClickSignUpWithPhone() {
        activity.supportFragmentManager.beginTransaction()
            .replace(AuthActivity.CONTAINER_ID, SignInPhoneFragment::class.java, null)
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
        val type = SignType.PasswordUp(
            password = action.password,
            data = UserData(
                email = action.email,
                phoneNumber = ""
            )
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
        fragment?.setSignUpOnCancel {
            fragment.loadingGone()
            fragment.showError(err = Throwable("Sign in canceled"))
            liveData.removeObserver(observer)
            job.cancel()
        }
    }
}