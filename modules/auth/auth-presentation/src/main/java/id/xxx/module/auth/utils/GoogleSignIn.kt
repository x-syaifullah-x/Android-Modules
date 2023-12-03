package id.xxx.module.auth.utils

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task

object GoogleSignIn {

    /**
     * serverClientId from (Authentication -> Sign-in method -> Sign-in providers -> Google -> Web SDK configuration -> Web client ID)
     * and add (Project settings -> Your apps -> Add app -> Android)
     * and add fingerprint SHA-1
     */
    private const val serverClientId =
        "1098413132051-pj231cvpoedpc07ll1tghasb2b6fi3j7.apps.googleusercontent.com"

    private val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(serverClientId)
        .requestEmail()
        .build()

    fun getClient(context: Context): GoogleSignInClient {
        return GoogleSignIn.getClient(context, options)
    }

    fun getSignedInAccountFromIntent(data: Intent): Task<GoogleSignInAccount> {
        return GoogleSignIn.getSignedInAccountFromIntent(data)
    }
}