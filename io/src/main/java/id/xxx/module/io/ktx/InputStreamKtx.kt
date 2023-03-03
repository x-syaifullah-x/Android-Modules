@file:JvmName("InputStreamKtx")

package id.xxx.module.io.ktx

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream

suspend fun <O : OutputStream> java.io.InputStream?.read(
    bufferSize: Int = 1024,
    out: O,
    onRead: suspend (count: Long) -> Unit = {},
    onReadComplete: suspend (out: O) -> Unit = {},
    onError: suspend (err: Throwable) -> Unit = {}
) {
    var count = 0L
    read(
        bufferSize = bufferSize,
        onRead = { data ->
            count += data.size
            onRead.invoke(count)
            withContext(Dispatchers.IO) { out.write(data) }
        },
        onReadComplete = {
            onReadComplete.invoke(out)
        },
        onError = onError,
    )
}

suspend fun java.io.InputStream?.read(
    bufferSize: Int = 1024,
    onRead: suspend (data: ByteArray) -> Unit,
    onReadComplete: suspend () -> Unit = {},
    onError: suspend (err: Throwable) -> Unit = {}
) {
    if (this != null) {
        try {
            val buffers = ByteArray(if (bufferSize > 0) bufferSize else 1)
            val buffersSize = buffers.size
            while (true) {
                val readCount =
                    withContext(Dispatchers.IO) { read(buffers, 0, buffers.size) }
                if (readCount != -1) {
                    val data =
                        if (readCount == buffersSize) buffers else buffers.copyOf(readCount)
                    onRead.invoke(data)
                } else {
                    break
                }
            }
            use { close() }
            onReadComplete.invoke()
        } catch (err: Throwable) {
            onError.invoke(err)
        }
    } else {
        onReadComplete.invoke()
    }
}