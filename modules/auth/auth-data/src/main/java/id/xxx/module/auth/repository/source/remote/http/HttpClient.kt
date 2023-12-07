package id.xxx.module.auth.repository.source.remote.http

import id.xxx.module.auth.repository.source.remote.response.Header
import id.xxx.module.auth.repository.source.remote.response.Response
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.internal.headersContentLength
import java.io.ByteArrayInputStream
import java.io.InputStream

internal class HttpClient private constructor(private val client: OkHttpClient) {

    companion object {

        @Volatile
        private var sInstance: HttpClient? = null

        fun getInstance() = sInstance ?: synchronized(this) {
            sInstance ?: run {
                val client = OkHttpClient.Builder()
                    .build()
                HttpClient(client).also { sInstance = it }
            }
        }
    }

    fun execute(
        url: String,
        methode: RequestMethode = RequestMethode.GET,
        requestBody: RequestBody? = null
    ): Response<InputStream> {
        val request = Request.Builder()
            .url(url)
            .method(methode.value, requestBody)
            .build()
        val response: okhttp3.Response = client.newCall(request).execute()
        return Response(
            header = Header(
                code = response.code,
                date = response.headers.getDate("date")?.time ?: 0,
                contentLength = response.headersContentLength()
            ),
            body = response.body?.byteStream() ?: ByteArrayInputStream(byteArrayOf())
        )
    }
}