package id.xxx.module.auth.data.source.remote.model

internal data class SignResult(
    val uid: String,
    val isNewUser: Boolean,
    val signProvider: String
)