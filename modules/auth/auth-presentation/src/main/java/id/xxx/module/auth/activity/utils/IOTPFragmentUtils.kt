package id.xxx.module.auth.activity.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.fragment.phone.PhoneSignOTPFragment
import id.xxx.module.auth.fragment.phone.listener.IPhoneSignOTPFragment
import id.xxx.module.fragment.ktx.getFragment
import id.xxx.module.auth.model.SignModel
import id.xxx.module.common.Resources
import kotlinx.coroutines.Job

class IOTPFragmentUtils(
    private val activity: AuthActivity,
    action: IPhoneSignOTPFragment.Action,
    val block: (IPhoneSignOTPFragment.Action.ClickNext) -> LiveData<Resources<SignModel>>
) {
    init {
        when (action) {
            is IPhoneSignOTPFragment.Action.ClickNext -> {
                val job = Job()
                val fragment = activity.getFragment<PhoneSignOTPFragment>()
                val observer = Observer<Resources<SignModel>> {
                    when (it) {
                        is Resources.Loading -> {
                            fragment?.loadingVisible()
                        }

                        is Resources.Success -> {
                            fragment?.loadingGone()
                            job.cancel()
                            activity.result(it.value)
                        }

                        is Resources.Failure -> {
                            fragment?.loadingGone()
                            fragment?.showError(it.value)
                            job.cancel()
                        }
                    }
                }
                block.invoke(action).observe(activity, observer)
                fragment?.setCancelProcess {
                    fragment.loadingGone()
                    fragment.showError(Throwable("Cancel"))
                    job.cancel()
                }
            }
        }
    }
}