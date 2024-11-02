package id.xxx.module.auth.repository.source.remote.response

internal class Response<T> internal constructor(
    val header: Header,
    val body: T
)