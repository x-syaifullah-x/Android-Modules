package id.xxx.module.auth.repository.source.remote.auth.email

import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.model.parms.SignType
import id.xxx.module.auth.model.parms.UpdateType
import id.xxx.module.auth.repository.ktx.toRequestBody
import id.xxx.module.auth.repository.source.remote.endpoint.Firebase
import id.xxx.module.auth.repository.source.remote.http.HttpClient
import id.xxx.module.auth.repository.source.remote.http.RequestMethode
import id.xxx.module.auth.repository.source.remote.response.Response
import org.json.JSONObject
import java.io.InputStream

internal class AuthDataSourceRemote private constructor(private val client: HttpClient) {

    companion object {

        @Volatile
        private var sInstance: AuthDataSourceRemote? = null

        fun getInstance() =
            sInstance ?: synchronized(this) {
                sInstance ?: AuthDataSourceRemote(HttpClient.getInstance())
                    .also { sInstance = it }
            }
    }

    fun update(type: UpdateType): Response<InputStream> {
        val payload = JSONObject()
        when (type) {
            is UpdateType.ConfirmEmailVerification -> {
                payload.put("oobCode", type.oobCode)
            }

            else -> throw NotImplementedError("Please see the documentation for $type")
        }
        return client.execute(
            url = Firebase.Auth.Endpoint.update(),
            methode = RequestMethode.POST,
            requestBody = payload.toRequestBody()
        )
    }

    fun resetPassword(oobCode: String, newPassword: String): Response<InputStream> {
        val payload = JSONObject()
        payload.put("oobCode", oobCode)
        payload.put("newPassword", newPassword)
        return client.execute(
            url = Firebase.Auth.Endpoint.resetPassword(),
            methode = RequestMethode.POST,
            payload.toRequestBody()
        )
    }

    fun sendOobCode(code: Code): Response<InputStream> {
        val payload = JSONObject()
        payload.put("requestType", code.requestType)
        var url = Firebase.Auth.Endpoint.sendOobCode()
        when (code) {
            is Code.PasswordReset -> {
                payload.put("email", code.email)
            }

            is Code.VerifyEmail -> {
                payload.put("idToken", code.idToken)
            }

            is Code.PhoneVerification -> {
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
                payload.put("phoneNumber", code.phoneNumber)
                payload.put("recaptchaToken", code.recaptchaResponse)
                url = Firebase.Auth.Endpoint.sendVerificationCode()
            }
        }
        return client.execute(
            url = url,
            methode = RequestMethode.POST,
            payload.toRequestBody()
        )
    }

    fun sign(type: SignType): Response<InputStream> {
        val payload = JSONObject()
        payload.put("returnSecureToken", true)
        val endpoint = when (type) {
            is SignType.PasswordUp -> {
                val email = type.data.email
                val password = type.password
                payload.put("email", email)
                payload.put("password", password)
                payload.put("returnSecureToken", true)
                Firebase.Auth.Endpoint.signUp()
            }

            is SignType.Google -> {
                payload.put("requestUri", "http://localhost")
                payload.put("postBody", "id_token=${type.token}&providerId=google.com")
                Firebase.Auth.Endpoint.signWithOAuthCredential()
            }

            is SignType.Phone -> {
                payload.put("sessionInfo", type.sessionInfo)
                payload.put("code", type.otp)
                Firebase.Auth.Endpoint.signWithPhoneNumber()
            }

            is SignType.CostumeToken -> {
                payload.put("token", type.token)
                Firebase.Auth.Endpoint.signWithCostumeToken()
            }

            is SignType.PasswordIn -> {
                payload.put("email", type.email)
                payload.put("password", type.password)
                Firebase.Auth.Endpoint.signWithPassword()
            }
        }
        return client.execute(
            url = endpoint,
            methode = RequestMethode.POST,
            requestBody = payload.toRequestBody()
        )
    }

    fun lookup(idToken: String): Response<InputStream> {
        val payload = JSONObject()
        payload.put("idToken", idToken)
        return client.execute(
            url = Firebase.Auth.Endpoint.lookup(),
            methode = RequestMethode.POST,
            requestBody = payload.toRequestBody()
        )
    }

    fun linkWithOAuthCredential(
        idToken: String, providerToken: String, providerId: String
    ): Response<InputStream> {
        val payload = JSONObject()
        payload.put("idToken", idToken)
        payload.put("requestUri", "http://localhost")
        payload.put("postBody", "id_token=$providerToken&providerId=$providerId")
        payload.put("returnSecureToken", true)
        payload.put("returnIdpCredential", true)
        return client.execute(
            url = Firebase.Auth.Endpoint.signWithOAuthCredential(),
            methode = RequestMethode.POST,
            requestBody = payload.toRequestBody()
        )
    }

    fun linkWithEmailOrPassword(
        idToken: String, email: String, password: String
    ): Response<InputStream> {
        val payload = JSONObject()
        payload.put("idToken", idToken)
        payload.put("email", email)
        payload.put("password", password)
        payload.put("returnSecureToken", true)
        return client.execute(
            url = Firebase.Auth.Endpoint.update(),
            methode = RequestMethode.POST,
            requestBody = payload.toRequestBody()
        )
    }
}