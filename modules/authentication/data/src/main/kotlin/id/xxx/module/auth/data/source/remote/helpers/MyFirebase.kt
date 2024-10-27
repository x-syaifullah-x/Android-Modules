package id.xxx.module.auth.data.source.remote.helpers

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth

object MyFirebase {

    @Volatile
    private var _sFirebaseApp: FirebaseApp? = null

    fun initialize(context: Context) {
        synchronized(this) {
            val options = FirebaseOptions.Builder()
//            .setDatabaseUrl(nul)
                .setGcmSenderId("1098413132051")
                .setApiKey("AIzaSyBHv3ZOEpUYTtBNv1lwJbbjpEe20sQfR20")
                .setApplicationId("1:1098413132051:android:958b4f05318804a5cc72e6")
                .setStorageBucket("x-x-x-projects.appspot.com")
                .setProjectId("x-x-x-projects")
                .build()
            if (_sFirebaseApp != null)
                _sFirebaseApp?.delete()
            _sFirebaseApp = FirebaseApp.initializeApp(context, options)
        }
    }

    fun getFirebaseApp() = _sFirebaseApp
        ?: throw NullPointerException("Please call firebaseInit before call this methode")

    fun getFirebaseAuth() =
        FirebaseAuth.getInstance(getFirebaseApp())
}