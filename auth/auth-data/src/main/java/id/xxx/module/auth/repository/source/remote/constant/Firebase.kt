package id.xxx.module.auth.repository.source.remote.constant

internal object Firebase {

    private const val API_KEY = "AIzaSyB2xm59WDvXEQDyWxqiDVwZExt3OOmlMh8"

    fun apiKey() = API_KEY

    object Auth {

        const val BASE_URL = "https://identitytoolkit.googleapis.com/v1"
//        const val BASE_URL = "http://localhost:9099/identitytoolkit.googleapis.com/v1"

        object Endpoint {

            private val SIGN_UP = "$BASE_URL/accounts:signUp?key=${apiKey()}"

            private val SIGN_WITH_PASSWORD =
                "$BASE_URL/accounts:signInWithPassword?key=${apiKey()}"

            private val SIGN_WITH_COSTUME_TOKEN =
                "$BASE_URL/accounts:signInWithCustomToken?key=${apiKey()}"

            private val SEND_OOB_CODE = "$BASE_URL/accounts:sendOobCode?key=${apiKey()}"

            private val RESET_PASSWORD = "$BASE_URL/accounts:resetPassword?key=${apiKey()}"

            private val UPDATE = "$BASE_URL/accounts:update?key=${apiKey()}"

            private val LOOKUP = "$BASE_URL/accounts:lookup?key=${apiKey()}"

            private val SEND_VERIFICATION_CODE =
                "$BASE_URL/accounts:sendVerificationCode?key=${apiKey()}"

            private val SIGN_WITH_PHONE_NUMBER =
                "$BASE_URL/accounts:signInWithPhoneNumber?key=${apiKey()}"

            fun signUp() = SIGN_UP

            fun signWithPassword() = SIGN_WITH_PASSWORD

            fun signWithPhoneNumber() = SIGN_WITH_PHONE_NUMBER

            fun signWithCostumeToken() = SIGN_WITH_COSTUME_TOKEN

            fun sendOobCode() = SEND_OOB_CODE

            fun resetPassword() = RESET_PASSWORD

            fun lookup() = LOOKUP

            fun update() = UPDATE

            fun sendVerificationCode() = SEND_VERIFICATION_CODE
        }
    }
}