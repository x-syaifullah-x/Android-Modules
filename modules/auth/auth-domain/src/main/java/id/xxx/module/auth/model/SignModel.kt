package id.xxx.module.auth.model

data class SignModel(
    val uid: String,
    val token: String,
    val refreshToken: String,
    val expiresInTimeMillis: Long,
    val isNewUser: Boolean,
) : java.io.Serializable