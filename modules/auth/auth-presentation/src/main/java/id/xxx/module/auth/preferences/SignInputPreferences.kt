package id.xxx.module.auth.preferences

import android.content.Context
import android.content.SharedPreferences

object SignInputPreferences {

    private const val PREFERENCES_NAME = "auth_sign_input_state"

    private const val KEY_INPUT_EMAIL = "email"
    private const val KEY_INPUT_PHONE_NUMBER = "phone_number"

    fun setInputEmail(context: Context?, email: String): Boolean {
        return getPreferences(context)
            ?.edit()
            ?.putString(KEY_INPUT_EMAIL, email)
            ?.commit() ?: false
    }

    fun getInputEmail(context: Context?) =
        getPreferences(context)?.getString(KEY_INPUT_EMAIL, null)

    fun setInputPhone(context: Context?, phone: String): Boolean {
        return getPreferences(context)
            ?.edit()
            ?.putString(KEY_INPUT_PHONE_NUMBER, phone)
            ?.commit() ?: false
    }

    fun getInputPhoneNumber(context: Context?) =
        getPreferences(context)?.getString(KEY_INPUT_PHONE_NUMBER, null)

    private fun getPreferences(context: Context?): SharedPreferences? =
        context?.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
}