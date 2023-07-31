package id.xxx.module.auth.model.parms

sealed interface Code {

    val requestType: String

    data class PasswordReset(val email: String) : Code {
        override val requestType = "PASSWORD_RESET"
    }

    data class VerifyEmail(val idToken: String) : Code {
        override val requestType = "VERIFY_EMAIL"
    }

    data class PhoneVerification(
        val phoneNumber: String,
        val recaptchaResponse: String
    ) : Code {
        override val requestType = "PHONE_VERIFICATION"
    }
}