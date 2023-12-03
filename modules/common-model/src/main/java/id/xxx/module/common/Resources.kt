package id.xxx.module.common

import java.util.concurrent.atomic.AtomicLong

interface Resources<out O> {

    data class Loading(
        private val progress: AtomicLong? = null,
        private val length: AtomicLong? = null,
    ) : Resources<Nothing> {

        fun getProgress() = progress?.get()

        fun getLength() = length?.get()
    }

    data class Success<T>(
        val value: T
    ) : Resources<T>

    data class Failure(
        val value: Throwable
    ) : Resources<Nothing>
}