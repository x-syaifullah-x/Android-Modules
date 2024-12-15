package id.xxx.module.auth.domain.model

import java.io.Serializable

data class UserModel(
    val uid: String,
    val isNewUser: Boolean,
    val signProvider: String
) : Serializable