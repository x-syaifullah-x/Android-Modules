package id.xxx.module.auth.activity

//class MainActivity : id.xxx.module.auth.MainActivity()

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
                    data?.getSerializableExtra(id.xxx.module.auth.MainActivity.RESULT_USER) as? User
                }
            println(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val a = Intent(this, id.xxx.module.auth.MainActivity::class.java)
        authActivityResultLauncher.launch(a)
    }
}