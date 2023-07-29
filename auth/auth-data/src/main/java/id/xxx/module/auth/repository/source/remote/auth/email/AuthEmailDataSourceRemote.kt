package id.xxx.module.auth.repository.source.remote.auth.email

import id.xxx.module.auth.model.OobType
import id.xxx.module.auth.model.SignInType
import id.xxx.module.auth.model.SignUpType
import id.xxx.module.auth.model.UpdateType
import id.xxx.module.auth.repository.source.remote.client.HttpClient
import id.xxx.module.auth.repository.source.remote.client.RequestMethode
import id.xxx.module.auth.repository.source.remote.constant.Firebase
import id.xxx.module.auth.repository.source.remote.response.Response
import okhttp3.MediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.InputStream

internal class AuthEmailDataSourceRemote private constructor(private val client: HttpClient) {

    companion object {

        @Volatile
        private var sInstance: AuthEmailDataSourceRemote? = null

        fun getInstance() =
            sInstance ?: synchronized(this) {
                sInstance ?: AuthEmailDataSourceRemote(HttpClient.getInstance())
                    .also { sInstance = it }
            }
    }

    fun update(type: UpdateType): Response<InputStream> {
        val payload = JSONObject()
        when (type) {
            is UpdateType.ConfirmEmailVerification -> {
                payload.put("oobCode", type.oobCode)
            }
            else -> throw NotImplementedError("please see the documentation for $type")
        }
        return client.execute(
            URL = Firebase.Auth.Endpoint.update(),
            methode = RequestMethode.POST,
            requestBody = payload.toRequestBody()
        )
    }

//    fun lookup(idToken: String): Response<InputStream> {
//        return client.execute(
//            URL = Firebase.Auth.Endpoint.lookup(),
//            methode = RequestMethode.POST,
//            requestBody = "{\"idToken\":\"$idToken\"}".toRequestBody()
//        )
//    }

    fun resetPassword(oobCode: String, newPassword: String): Response<InputStream> {
        val payload = JSONObject()
        payload.put("oobCode", oobCode)
        payload.put("newPassword", newPassword)
        return client.execute(
            URL = Firebase.Auth.Endpoint.resetPassword(),
            methode = RequestMethode.POST,
            payload.toRequestBody()
        )
    }

    fun sendOobCode(oobType: OobType): Response<InputStream> {
        val payload = JSONObject()
        payload.put("requestType", oobType.requestType)
        when (oobType) {
            is OobType.PasswordReset -> {
                payload.put("email", oobType.email)
            }
            is OobType.VerifyEmail -> {
                payload.put("idToken", oobType.idToken)
            }
        }
        return client.execute(
            URL = Firebase.Auth.Endpoint.sendOobCode(),
            methode = RequestMethode.POST,
            payload.toRequestBody()
        )
    }

    fun signUp(type: SignUpType): Response<InputStream> {
        when (type) {
            is SignUpType.Password -> {
                val email = type.data.email
                val password = type.password
                val payload = JSONObject()
                payload.put("email", email)
                payload.put("password", password)
                payload.put("returnSecureToken", true)
                return client.execute(
                    URL = Firebase.Auth.Endpoint.signUp(),
                    methode = RequestMethode.POST,
                    payload.toRequestBody()
                )
            }
            is SignUpType.Phone -> {
                val payload = JSONObject()
                payload.put("sessionInfo", type.sessionInfo)
                payload.put("code", type.otp)
                return client.execute(
                    URL = Firebase.Auth.Endpoint.signWithPhoneNumber(),
                    methode = RequestMethode.POST,
                    payload.toRequestBody()
                )
            }
        }
    }

    fun sendVerificationCode(
        phoneNumber: String,
        recaptchaResponse: String
    ): Response<InputStream> {
//        {
//            "phoneNumber": string,
//            "iosReceipt": string,
//            "iosSecret": string,
//            "recaptchaToken": string,
//            "tenantId": string,
//            "autoRetrievalInfo": {
//                  "appSignatureHash": {
//                      "00:4c:8d:56:fa:27:5d:b3:63:3a:8e:0e:86:d3:12:9b:a5:1c:a1:cf:f7:21:a1:1f:bd:a1:c8:ce:d0:08:c8:32"
//                  }
//        },
//            "safetyNetToken": string
//        }
        val payload = JSONObject()
        payload.put("phoneNumber", phoneNumber)
        payload.put("recaptchaToken", recaptchaResponse)
        return client.execute(
            URL = Firebase.Auth.Endpoint.sendVerificationCode(),
            methode = RequestMethode.POST,
            requestBody = payload.toRequestBody()
        )
    }

    fun signIn(type: SignInType): Response<InputStream> {
        val payload = JSONObject()
        payload.put("returnSecureToken", true)
        val url = when (type) {
            is SignInType.Phone -> {
                payload.put("sessionInfo", type.sessionInfo)
                payload.put("code", type.otp)
                val result = Firebase.Auth.Endpoint.signWithPhoneNumber()
                result
            }
            is SignInType.CostumeToken -> {
                payload.put("token", type.token)
                Firebase.Auth.Endpoint.signWithCostumeToken()
            }
            is SignInType.Password -> {
                payload.put("email", type.email)
                payload.put("password", type.password)
                Firebase.Auth.Endpoint.signWithPassword()
            }
        }
        return client.execute(
            URL = url,
            methode = RequestMethode.POST,
            requestBody = payload.toRequestBody()
        )
    }

    private fun JSONObject.toRequestBody(
        contentType: MediaType? = null
    ) = toString().toRequestBody(contentType)
}