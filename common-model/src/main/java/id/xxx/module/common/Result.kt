package id.xxx.module.common

import java.util.concurrent.atomic.AtomicLong

interface Result<out O> {

    data class Loading(
        val progress: Progress? = null
    ) : Result<Nothing> {

        data class Progress(private val count: AtomicLong, private val length: AtomicLong) {

            fun count() = count.get()

            fun length() = length.get()
        }
    }

    data class Success<T>(
        val value: T
    ) : Result<T>

    data class Failure(
        val value: Throwable
    ) : Result<Nothing>
}