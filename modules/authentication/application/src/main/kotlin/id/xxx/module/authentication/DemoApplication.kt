package id.xxx.module.authentication

import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

class DemoApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        val options = FirebaseOptions.Builder()
            .setDatabaseUrl(null)
            .setGcmSenderId("1098413132051")
            .setApiKey("AIzaSyBHv3ZOEpUYTtBNv1lwJbbjpEe20sQfR20")
            .setApplicationId("1:1098413132051:android:75e4145217b22569cc72e6")
            .setStorageBucket("x-x-x-projects.appspot.com")
            .setProjectId("x-x-x-projects")
            .build()
        FirebaseApp.initializeApp(base, options)
    }
}