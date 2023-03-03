package id.xxx.module.auth.model

data class VerificationCodeResult(
    /**
     *[sessionInfo] session info for sign_in or sign_up with phone
     * */
    val sessionInfo: String,
)