package id.xxx.module.bundle.ktx

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

inline fun <reified T : Serializable> Bundle?.getSerializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this?.getSerializable(
        key, T::class.java
    )

    else -> @Suppress("DEPRECATION") this?.getSerializable(key) as? T
}

inline fun <reified T : Parcelable> Bundle?.getParcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> this?.getParcelable(
        key, T::class.java
    )

    else -> @Suppress("DEPRECATION") this?.getParcelable(key) as? T
}