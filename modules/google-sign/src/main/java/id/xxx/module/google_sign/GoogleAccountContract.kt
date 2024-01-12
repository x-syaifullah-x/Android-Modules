package id.xxx.module.google_sign

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleAccountContract : ActivityResultContract<Intent?, GoogleSignInAccount?>() {

    companion object {

        const val DATA_EXTRA_IS_CLEAR_USER = "data_extra_is_clear_user"
    }

    override fun createIntent(context: Context, input: Intent?): Intent {
        val info: ApplicationInfo = context.packageManager.getApplicationInfo(
            context.packageName, PackageManager.GET_META_DATA
        )
        val bundle = info.metaData
        val serverClientId = bundle.getString("${context.packageName}.google_sign.server_client_id")
        if (serverClientId.isNullOrBlank()) {
            throw IllegalArgumentException(
                "google sign requires server_client_id, please check https://github.com/x-syaifullah-x/android-modules/blob/master/modules/google-sign/README.md"
            )
        }
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context, options)
        val isClearUser = input?.getBooleanExtra(DATA_EXTRA_IS_CLEAR_USER, true) ?: true
        if (isClearUser) {
            googleSignInClient.signOut()
        }
        return googleSignInClient.signInIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): GoogleSignInAccount? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
        var result: GoogleSignInAccount? = null
        if (task.isSuccessful) {
            result = task.result
        }
        return result
    }
}