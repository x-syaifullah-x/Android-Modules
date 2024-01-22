package id.xxx.module.auth.activity.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.fragment.password.PasswordRecoveryFragment
import id.xxx.module.auth.fragment.password.listener.IPasswordRecoveryFragment
import id.xxx.module.auth.model.PasswordResetModel
import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.viewmodel.AuthViewModel
import id.xxx.module.common.Resources
import id.xxx.module.fragment.ktx.getFragment

class PasswordRecoveryFragmentUtils(
    private val activity: AuthActivity,
    private val viewModel: AuthViewModel,
    action: IPasswordRecoveryFragment.Action,
) {

    private var observerForgetPassword: Observer<Resources<PasswordResetModel>>? = null
    private var liveDataForgetPassword: LiveData<Resources<PasswordResetModel>>? = null

    init {
        when (action) {
            is IPasswordRecoveryFragment.Action.Next -> actionNext(action)
            is IPasswordRecoveryFragment.Action.Cancel -> actionCancel()
        }
    }

    private fun actionNext(action: IPasswordRecoveryFragment.Action.Next) {
        liveDataForgetPassword = viewModel.sendCode(
            Code.PasswordReset(email = action.email)
        ).asLiveData()
        observerForgetPassword = Observer { resources ->
            val passwordRecoveryFragment = activity.getFragment<PasswordRecoveryFragment>()
            when (resources) {
                is Resources.Loading -> passwordRecoveryFragment?.onLoading()
                is Resources.Failure -> passwordRecoveryFragment?.onError(resources.value)
                is Resources.Success -> {
                    passwordRecoveryFragment?.onSuccess()
                    actionCancel()
                }
            }
        }
        observerForgetPassword?.apply {
            liveDataForgetPassword?.observe(activity, this)
        }
    }

    private fun actionCancel() {
        observerForgetPassword?.apply { liveDataForgetPassword?.removeObserver(this) }
        liveDataForgetPassword = null
        observerForgetPassword = null
    }
}