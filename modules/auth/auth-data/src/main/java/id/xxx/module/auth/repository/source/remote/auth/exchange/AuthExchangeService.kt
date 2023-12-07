package id.xxx.module.auth.repository.source.remote.auth.exchange

import id.xxx.module.auth.repository.source.remote.http.HttpClient
import id.xxx.module.auth.repository.source.remote.http.RequestMethode
import id.xxx.module.auth.repository.source.remote.endpoint.Firebase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

internal class AuthExchangeService {

    fun token(refreshToken: String) = HttpClient.getInstance().execute(
        url = "https://securetoken.googleapis.com/v1/token?key=${Firebase.apiKey()}",
        methode = RequestMethode.POST,
        requestBody = "grant_type=refresh_token&refresh_token=$refreshToken".toRequestBody("application/x-www-form-urlencoded".toMediaType())
    )
}