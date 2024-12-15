package id.xxx.module.authentication

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import id.xxx.module.autentication.AuthenticationFragment
import id.xxx.module.autentication.IAuthentication
import id.xxx.module.auth.domain.model.AuthenticationType
import id.xxx.module.common.Resources
import kotlinx.coroutines.flow.map

class DemoActivity : AppCompatActivity(), IAuthentication {

    private val viewModel: DemoViewModel by viewModels { DemoViewModel.Factory }
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() = finishAfterTransition()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, AuthenticationFragment::class.java, null)
                .commit()
    }

    override fun onAuthentication(type: AuthenticationType) =
        viewModel.authentication(type).map { res ->
            if (res is Resources.Success)
                supportFragmentManager.beginTransaction().replace(
                    android.R.id.content,
                    HomeFragment::class.java,
                    bundleOf(HomeFragment.KEY_DATA_EXTRA_USER to res.value)
                ).commit()
            return@map res
        }
}