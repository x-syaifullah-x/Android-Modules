package id.xxx.module.auth.activity

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import id.xxx.module.auth.AuthActivityForResult
import id.xxx.module.auth.activity.impl.OnBackPressedCallbackImpl
import id.xxx.module.auth.application.databinding.MainActivityBinding
import id.xxx.module.auth.repository.AuthRepositoryImpl
import id.xxx.module.auth.usecase.AuthUseCaseImpl
import id.xxx.module.viewbinding.ktx.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding<MainActivityBinding>()

    private val sharedPreferences by lazy { getSharedPreferences("auth", Context.MODE_PRIVATE) }

    private val authActivityResultLauncher =
        registerForActivityResult(AuthActivityForResult()) { result ->
            if (result != null) {
                binding.tvUidValue.text = result.uid
                binding.tvTokenValue.text = result.token
                binding.tvRefreshTokenValue.text = result.refreshToken
                val date = Date(result.expiresInTimeMillis)
                binding.tvExpiresInValue.text = "$date"
                binding.tvIsNewUserValue.text = "${result.isNewUser}"

                lifecycleScope.launch(Dispatchers.IO) {
                    val repo = AuthUseCaseImpl.getInstance(
                        AuthRepositoryImpl.getInstance()
                    )
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val onBackPressedCallbackImpl = OnBackPressedCallbackImpl(this)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallbackImpl)

        binding.btnTestSign.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, 0, 0)
            } else {
                @Suppress("DEPRECATION")
                overridePendingTransition(0, 0)
            }
            authActivityResultLauncher.launch(null)
        }
    }
}