package runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.umn.iptv.App

class AndroidJUnitRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader, className: String, context: Context
    ): Application = super.newApplication(cl, App::class.java.name, context)
}