package id.xxx.template.activities

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import id.xxx.templates.application.R

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(
            this, getString(R.string.app_name), Toast.LENGTH_LONG
        ).show()
    }
}