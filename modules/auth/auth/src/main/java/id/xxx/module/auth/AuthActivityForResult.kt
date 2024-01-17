package id.xxx.module.auth

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.contract.ActivityResultContract
import id.xxx.module.auth.model.SignModel

class AuthActivityForResult : ActivityResultContract<Intent?, SignModel?>() {

    override fun createIntent(context: Context, input: Intent?): Intent {
        return Intent(context, AuthActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): SignModel? {
        val result =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent?.getParcelableExtra(
                    AuthActivity.RESULT_USER,
                    SignModel::class.java
                )
            } else {
                @Suppress("DEPRECATION")
                intent?.getSerializableExtra(AuthActivity.RESULT_USER) as? SignModel
            }
        return result
    }
}