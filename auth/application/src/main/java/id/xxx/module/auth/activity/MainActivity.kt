package id.xxx.module.auth.activity

//class MainActivity : id.xxx.module.auth.MainActivity()

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, id.xxx.module.auth.MainActivity::class.java))
    }
}