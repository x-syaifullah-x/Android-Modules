package id.xxx.module.google_sign

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleAccountResultContract : ActivityResultContract<Intent?, GoogleSignInAccount?>() {

    companion object {

        private const val DATA_EXTRA_SERVER_CLIENT_ID = "data_extra_web_client_id"
        private const val DATA_EXTRA_IS_CLEAR_USER = "data_extra_is_clear_user"

        fun createIntent(webClientId: String, isClearUser: Boolean? = null) = Intent().apply {
            putExtra(DATA_EXTRA_SERVER_CLIENT_ID, webClientId)
            putExtra(DATA_EXTRA_IS_CLEAR_USER, isClearUser)
        }
    }

    override fun createIntent(context: Context, input: Intent?): Intent {
        input ?: throw NullPointerException("input")
        val serverClientId = input.getStringExtra(DATA_EXTRA_SERVER_CLIENT_ID)
        if (serverClientId.isNullOrBlank()) {
            throw IllegalArgumentException("serverClientId")
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        val isClearUser = input.getBooleanExtra(DATA_EXTRA_IS_CLEAR_USER, true)
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