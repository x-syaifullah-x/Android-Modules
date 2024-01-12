package runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class AndroidJUnitRunner : AndroidJUnitRunner() {

    class MockApplication : Application()

    override fun newApplication(
        classLoader: ClassLoader, className: String, context: Context
    ): Application = super.newApplication(classLoader, MockApplication::class.java.name, context)
}