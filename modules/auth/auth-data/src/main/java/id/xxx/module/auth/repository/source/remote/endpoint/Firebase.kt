package id.xxx.module.auth.repository.source.remote.endpoint

internal object Firebase {

    /**
     * Firebase console -> Project settings -> Web API Key
     * or
     * Firebase console -> Project settings -> Android apps -> google-services.json -> api_key -> current_key
     */
    private const val API_KEY = "AIzaSyDVB5Yivc3wAR20o5kMLDfb9gLNQBaUWaM"

    fun apiKey() = API_KEY

    object Auth {

        const val BASE_URL = "https://identitytoolkit.googleapis.com/v1"
//        const val BASE_URL = "http://192.168.43.89:9099/identitytoolkit.googleapis.com/v1"

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

            private val SIGN_WITH_O_AUTH_CREDENTIAL =
                "$BASE_URL/accounts:signInWithIdp?key=${apiKey()}"

            fun signUp() = SIGN_UP

            fun signWithPassword() = SIGN_WITH_PASSWORD

            fun signWithPhoneNumber() = SIGN_WITH_PHONE_NUMBER

            fun signWithCostumeToken() = SIGN_WITH_COSTUME_TOKEN

            fun signWithOAuthCredential() = SIGN_WITH_O_AUTH_CREDENTIAL

            fun sendOobCode() = SEND_OOB_CODE

            fun resetPassword() = RESET_PASSWORD

            fun lookup() = LOOKUP

            fun update() = UPDATE

            fun sendVerificationCode() = SEND_VERIFICATION_CODE
        }
    }
}