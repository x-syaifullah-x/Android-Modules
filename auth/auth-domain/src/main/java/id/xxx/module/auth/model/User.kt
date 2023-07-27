package id.xxx.module.auth.model

data class User(
    val uid: String,
    val isRemember: Boolean = false,
) : java.io.Serializable