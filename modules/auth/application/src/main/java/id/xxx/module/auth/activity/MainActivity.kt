package id.xxx.module.auth.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import id.xxx.module.auth.AuthActivityForResult
import id.xxx.module.auth.activity.impl.OnBackPressedCallbackImpl
import id.xxx.module.auth.application.databinding.MainActivityBinding
import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.model.parms.UpdateType
import id.xxx.module.auth.repository.AuthRepositoryImpl
import id.xxx.module.auth.usecase.AuthUseCaseImpl
import id.xxx.module.viewbinding.ktx.viewBinding
import kotlinx.coroutines.flow.collect
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

                lifecycleScope.launch {
                    val repo = AuthUseCaseImpl.getInstance(
                        AuthRepositoryImpl.getInstance()
                    )
                    repo.lookup(result.token).collect {}
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val onBackPressedCallbackImpl = OnBackPressedCallbackImpl(this)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallbackImpl)

        binding.btnTestSign.setOnClickListener {
            authActivityResultLauncher.launch(null)
        }
    }
}