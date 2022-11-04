package runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class AndroidJUnitRunner : AndroidJUnitRunner() {

    class AppAndroidTest : Application()

    override fun newApplication(
        cl: ClassLoader?, className: String?, context: Context?
    ): Application = super.newApplication(cl, AppAndroidTest::class.java.name, context)
}