package runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import id.xxx.module.authentication.DemoApplication

class AndroidJUnitRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader, className: String, context: Context
    ): Application = super.newApplication(cl, DemoApplication::class.java.name, context)
}