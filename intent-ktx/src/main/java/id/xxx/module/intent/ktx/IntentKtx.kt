package id.xxx.module.intent.ktx

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.io.Serializable

inline fun <reified T : Serializable> Intent?.getSerializableExtra(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this?.getSerializableExtra(
            key, T::class.java
        )

        else -> @Suppress("DEPRECATION") this?.getSerializableExtra(key) as? T
    }

inline fun <reified T : Parcelable> Intent?.getParcelableExtra(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this?.getParcelableExtra(
            key, T::class.java
        )

        else -> @Suppress("DEPRECATION") this?.getParcelableExtra(key) as? T
    }