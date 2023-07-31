package id.xxx.module.auth.model

data class SignModel(
    val uid: String,
    val token: String,
    val refreshToken: String,
    val expiresIn: Long
) : java.io.Serializable