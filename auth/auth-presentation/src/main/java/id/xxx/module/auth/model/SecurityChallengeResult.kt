package id.xxx.module.auth.model

sealed interface SecurityChallengeResult {
    data class Error(
        val err: Throwable
    ) : SecurityChallengeResult

    data class Success(
        val isNewUser: Boolean,
        val response: String,
        val phoneNumber: String
    ) : SecurityChallengeResult
}