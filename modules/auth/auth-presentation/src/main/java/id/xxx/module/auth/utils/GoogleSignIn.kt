package id.xxx.module.auth.utils

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task

object GoogleSignIn {

    private val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("560568355737-o57c6516bk1uj0s9l5fhv391kf76cafb.apps.googleusercontent.com")
        .requestEmail()
        .build()

    fun getClient(activity: Activity): GoogleSignInClient {
        return GoogleSignIn.getClient(activity, options)
    }

    fun getSignedInAccountFromIntent(data: Intent): Task<GoogleSignInAccount> {
        return GoogleSignIn.getSignedInAccountFromIntent(data)
    }
}