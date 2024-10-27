package id.xxx.module.auth

import android.app.Application
import id.xxx.module.auth.data.source.remote.helpers.MyFirebase

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        MyFirebase.initialize(this)
    }
}