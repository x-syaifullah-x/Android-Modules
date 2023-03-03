package id.xxx.module.auth.repository.source.remote.response

internal data class Header internal constructor(
    val code: Int,
    val date: Long,
    val contentLength: Long
)