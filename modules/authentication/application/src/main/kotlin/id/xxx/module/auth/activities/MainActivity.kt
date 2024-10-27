package id.xxx.module.auth.activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import id.xxx.module.auth.activities.fragment.HomeFragment
import id.xxx.module.auth.domain.model.TypeSign
import id.xxx.module.auth.presentation.ISign
import id.xxx.module.auth.presentation.SignInFragment
import id.xxx.module.auth.presentation.SignResult
import id.xxx.module.common.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : FragmentActivity(), ISign {

    private val viewModel: MainViewModel by viewModels { MainViewModel.Factory }
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val fragments = supportFragmentManager.fragments
            if (fragments.size > 1) {
                supportFragmentManager.beginTransaction().remove(
                    fragments.last()
                ).commitNow()
            } else
                finishAfterTransition()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        if (savedInstanceState == null) {
            lifecycleScope.launch {
                when (val currentUser = viewModel.currentUser.asFlow().drop(1).first()) {
                    is Resources.Loading -> throw NotImplementedError()
                    is Resources.Success -> {
                        val user = currentUser.value
                        val data =
                            if (user == null)
                                SignInFragment::class.java to null
                            else
                                HomeFragment::class.java to bundleOf(HomeFragment.KEY_DATA_EXTRA_USER to user)
                        supportFragmentManager.beginTransaction()
                            .replace(android.R.id.content, data.first, data.second)
                            .commitNow()
                    }

                    is Resources.Failure -> {}
                }
            }
        }
    }

    override suspend fun onSign(type: TypeSign): SignResult {
        return when (val res = viewModel.sign(type).lastOrNull()) {
            is Resources.Success -> {
                withContext(Dispatchers.Main) {
                    supportFragmentManager.beginTransaction().replace(
                        android.R.id.content,
                        HomeFragment::class.java,
                        bundleOf(HomeFragment.KEY_DATA_EXTRA_USER to res.value)
                    ).commitNow()
                }
                SignResult.Success()
            }

            is Resources.Failure ->
                SignResult.Error(res.value)

            else ->
                throw NotImplementedError()
        }
    }
}