@file:JvmName("JSONObjectKtx")

package id.xxx.module.auth.repository.ktx

import org.json.JSONObject

internal fun JSONObject.getString(
    name: String, defaultValue: String
): String =
    try {
        getString(name)
    } catch (err: Throwable) {
        defaultValue
    }