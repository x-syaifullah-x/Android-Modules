@file:Suppress("DEPRECATION")

package id.xxx.module.autentication.google

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import id.xxx.modules.authentication.presentation.R

class GoogleAccountResultContract : ActivityResultContract<Intent?, GoogleSignInAccount?>() {

    companion object {
        const val DATA_EXTRA_IS_CLEAR_USER = "data_extra_is_clear_user"
    }

    override fun createIntent(context: Context, input: Intent?): Intent {
        val googleWebClientId = context.getString(R.string.google_web_client_id)
        if (googleWebClientId.isBlank())
            throw NullPointerException("Please set R.string.google_web_client_id ... !!!")
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleWebClientId)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        val isClearUser = input?.getBooleanExtra(DATA_EXTRA_IS_CLEAR_USER, true) ?: true
        if (isClearUser)
            googleSignInClient.signOut()
        return googleSignInClient.signInIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): GoogleSignInAccount? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        var result: GoogleSignInAccount? = null
        if (task.isSuccessful)
            result = task.result
        return result
    }
}