package id.xxx.module.auth.activity

//class MainActivity : id.xxx.module.auth.MainActivity()

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import id.xxx.module.auth.R
import id.xxx.module.auth.activity.impl.OnBackPressedCallbackImpl
import id.xxx.module.auth.model.User

class MainActivity : AppCompatActivity() {

    private val authActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val data = activityResult.data
            val result =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    data?.getParcelableExtra(
                        id.xxx.module.auth.MainActivity.RESULT_USER,
                        User::class.java
                    )
                } else {
                    @Suppress("DEPRECATION")
                    data?.getSerializableExtra(id.xxx.module.auth.MainActivity.RESULT_USER) as? User
                }
            println(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(id.xxx.module.auth.application.R.layout.main_activity)

        val onBackPressedCallbackImpl = OnBackPressedCallbackImpl(this)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallbackImpl)

        val b = findViewById<Button>(id.xxx.module.auth.application.R.id.btn_test_sign)
        b.setOnClickListener {
            val i = Intent(this, id.xxx.module.auth.MainActivity::class.java)
            authActivityResultLauncher.launch(i)
        }
    }
}