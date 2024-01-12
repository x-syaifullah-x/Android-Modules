package id.xxx.module.auth.activity

//class MainActivity : id.xxx.module.auth.MainActivity()

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import id.xxx.module.viewbinding.ktx.viewBinding
import id.xxx.module.auth.MainActivity
import id.xxx.module.auth.activity.impl.OnBackPressedCallbackImpl
import id.xxx.module.auth.application.databinding.MainActivityBinding
import id.xxx.module.auth.model.SignModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding<MainActivityBinding>()

    private val authActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val data = activityResult.data
            val result =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    data?.getParcelableExtra(MainActivity.RESULT_USER, SignModel::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    data?.getSerializableExtra(MainActivity.RESULT_USER) as? SignModel
                }
            binding.tvUidValue.text = result?.uid
            binding.tvTokenValue.text = result?.token
            binding.tvRefreshTokenValue.text = result?.refreshToken
            val date = Date(result?.expiresInTimeMillis ?: 0)
            binding.tvExpiresInValue.text = "$date"
            binding.tvIsNewUserValue.text = "${result?.isNewUser}"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val onBackPressedCallbackImpl = OnBackPressedCallbackImpl(this)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallbackImpl)

        binding.btnTestSign.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            authActivityResultLauncher.launch(i)
        }
    }
}