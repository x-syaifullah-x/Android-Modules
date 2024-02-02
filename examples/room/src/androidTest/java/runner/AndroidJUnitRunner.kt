package runner

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import id.xxx.example.room.App

class AndroidJUnitRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader, className: String, context: Context
    ): Application = super.newApplication(cl, App::class.java.name, context)
}