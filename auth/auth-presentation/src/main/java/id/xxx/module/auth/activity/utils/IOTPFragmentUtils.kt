package id.xxx.module.auth.activity.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import id.xxx.module.auth.activity.AuthActivity
import id.xxx.module.auth.fragment.OTPFragment
import id.xxx.module.auth.fragment.listener.IOTPFragment
import id.xxx.module.auth.ktx.getFragment
import id.xxx.module.auth.model.User
import id.xxx.module.common.Resources
import kotlinx.coroutines.Job

class IOTPFragmentUtils(
    private val activity: AuthActivity,
    action: IOTPFragment.Action,
    val block: (IOTPFragment.Action.ClickNext) -> LiveData<Resources<User>>
) {
    init {
        when (action) {
            is IOTPFragment.Action.ClickNext -> {
                val job = Job()
                val fragment = activity.getFragment<OTPFragment>()
                val observer = Observer<Resources<User>> {
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