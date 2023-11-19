@file:JvmName("ActivityKtx")

package id.xxx.module.activity.ktx

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager

fun Activity.hideSystemUI() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.insetsController?.let { windowInsetsController ->
            windowInsetsController.systemBarsBehavior =
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(WindowInsets.Type.systemBars())
        }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        @Suppress("DEPRECATION")
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
    }
}

inline fun Activity.setResult(block: Intent.() -> Unit = {}) {
    val intent = Intent()
    block(intent)
    setResult(Activity.RESULT_OK, intent)
    finish()
}

/**
 * EXAMPLE: startActivity<MainActivity>() or startActivity<MainActivity>{ intent -> }
 */
inline fun <reified T : Activity> Activity.startActivity(
    block: Intent.() -> Unit = {}
) = startActivity(T::class.java.name, block)

inline fun Activity.startActivity(clazzName: String, block: Intent.() -> Unit = {}) {
    val activityClass = Class.forName(clazzName)
    val intent = Intent(this, activityClass)
    block(intent)
    startActivity(intent)
}