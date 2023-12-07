package id.xxx.example.chat.data.model

import java.util.concurrent.atomic.AtomicLong

sealed interface Result<out R> {

    data class Loading(
        val progress: AtomicLong? = null,
        val length: Long? = null,
    ) : Result<Nothing>

    data class Success<V>(
        val value: V
    ) : Result<V>

    data class Error(
        val value: Throwable
    ) : Result<Nothing>
}