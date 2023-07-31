package id.xxx.module.auth.data.source.remote.auth.email

import id.xxx.module.auth.model.parms.Code
import id.xxx.module.auth.model.parms.SignInType
import id.xxx.module.auth.model.parms.SignUpType
import id.xxx.module.auth.model.parms.UserData
import id.xxx.module.auth.repository.source.remote.auth.email.AuthEmailDataSourceRemote
import id.xxx.module.auth.repository.source.remote.response.Response
import id.xxx.module.io.ktx.read
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.InputStream

internal class AuthEmailDataSourceRemoteTest {

    private val dataSource = AuthEmailDataSourceRemote.getInstance()

    @Test
    fun sendVerificationCode() = runBlocking {

//        val response = dataSource.sendVerificationCode(
////                "+111111111111",
//            "+6281295677453",
//            "03AFY_a8VfeZY8JjZuKQ8gsdCsKSXXYGYcdpS9rkS2PrSpiAoy6mE7ys1DBAB2NiXyMuUY4fd94mIfpYJ_iL2zn-QErLyzesQybpZjah678-V3Y2y2kQLwuVcbHdPW2I5n_PjwU7-Wxdnmkwe5KDuFE0rjQm9VrKHGPT8pVIxA6Brc7vcVhchuFoHFMdClIWsSr8p_DY3xcTnEHMbF2vzBNZYNsGhk5uMwemhLeUzuFRLkOXRR8T1PuH6Qz-qIZ2Q8gEgSNS5GQPrNZ7xOs58fofakwURZUcO9lBDXw9Y-JaH3ilGdgQVvB-EQENiP9E2m-nOM-POSd5im0vdqM3KnXMUCR6IbYMPKigdvCasD3W16AzfSbNOXJRGNhTr_awasKL-_ogvcCyzYbN7dbNZZjVQVCn6dPnSZioIYrq8chAqR80NINb_NR4ttfGtSwa3R_yCTf0stKN5-6CCDRuc3Z-IDq15JuFZ6m4Lk2Pxx9ygNz42B5_7lWqlnh5UmlnrcgFUN9zlbnEcE8z8qxLKacUkZPJOxMRFIpg"
//        )
//        val read = read(response)
//        val j = JSONObject(read)
//        val sessionInfo = j.getString("sessionInfo")
//        println(sessionInfo)
//        signInWithPhoneNumber(sessionInfo, "123456")
    }

    //    @Suppress("SameParameterValue")
//    private fun signInWithPhoneNumber(
//        sessionInfo: String,
//        code: String
//    ) = runBlocking<Unit> {
//        val type = SignInType.Phone(sessionInfo, code)
//        val response = dataSource.signIn(type)
//        read(response)
//    }
//
//    @Test
//    fun verifyEmail() = runBlocking<Unit> {
//        val oobCode = "cdq-ZYcE5SmjdUQE8MgUNkAI1HVgpEdlvIRjd_lAGY0AAAGELudZUg"
//        val type = UpdateType.ConfirmEmailVerification(oobCode)
//        val response = dataSource.update(type)
//        read(response)
//    }


//    @Test
//    fun lookup() = runBlocking<Unit> {
//        val response = dataSource.lookup(
//            "eyJhbGciOiJSUzI1NiIsImtpZCI6IjYyM2YzNmM4MTZlZTNkZWQ2YzU0NTkyZTM4ZGFlZjcyZjE1YTBmMTMiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiU3lhaWZ1bGxhaCIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQWNIVHRleHlMMWtKZ1U3UXZuSHBMYzR4OWY5MlF3VDdMd244Y01YQzNucEhjay1mQT1zOTYtYyIsImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS94LXgteC10ZXN0IiwiYXVkIjoieC14LXgtdGVzdCIsImF1dGhfdGltZSI6MTY5MDc4ODQ4MywidXNlcl9pZCI6Ilh5UEx0b05IQVBPV2t3Y1dNNE5zbVg4UnBNRjMiLCJzdWIiOiJYeVBMdG9OSEFQT1drd2NXTTROc21YOFJwTUYzIiwiaWF0IjoxNjkwNzg4NDgzLCJleHAiOjE2OTA3OTIwODMsImVtYWlsIjoieC5zeWFpZnVsbGFoLnhAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZW1haWwiOlsieC5zeWFpZnVsbGFoLnhAZ21haWwuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.od-w_SG7OtJsWmXY7CT8XCSHRvuGVopMq2QHEZl4KrvhQKIYnQHu_NQOvYwxaiJ7fVIRPXT2FcGjnItl9G0hKn7PxCWP246DYO45Z4zCyqkF5EpADsQPl7aSF6LPzKeQ6BMKudiFhvKmTTjpE18X5ZnRgRAqyAtO94WqIuLwfgzP3RQ2BRK7Ll7orqNwMl54IaiLn4agHJRsBUaYdT-ZVXev_QAQKIMeCWZvF4eJQMmzLkGdyGNnvgDZdwISOh9YlP3C-L-U5BzkAfS4T9D-ndMF3kjw_dJdH7NG2Nd1pga5nOcmaoWOUiXbXF2kp48DBHR4T74JSo5nIaydncr-HQ"
//        )
//        read(response)
//    }

    //
//    @Test
//    fun confirmResetPassword() = runBlocking<Unit> {
//        val uri =
//            "https://auth-a22e6.firebaseapp.com/__/auth/action?mode=resetPassword&oobCode=82Kd3y-QjE02vJbjWfT7E_blNMVpuXDv9VRZD1SSOhkAAAGEG0bkwg&apiKey=AIzaSyAMGBhmVsmDHfBgM3bdfp-K72zdpPd9kHs&lang=en"
//                .toHttpUrl()
//        val oobCode = uri.queryParameter("oobCode") ?: throw Throwable("not oob code")
//        val response = dataSource.resetPassword(oobCode, "123456")
//        read(response)
//    }
//
//    @Test
//    fun sendOobCodeVerifyEmail() = runBlocking<Unit> {
//        val token =
//            "eyJhbGciOiJSUzI1NiIsImtpZCI6IjYyM2YzNmM4MTZlZTNkZWQ2YzU0NTkyZTM4ZGFlZjcyZjE1YTBmMTMiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiU3lhaWZ1bGxhaCIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQWNIVHRleHlMMWtKZ1U3UXZuSHBMYzR4OWY5MlF3VDdMd244Y01YQzNucEhjay1mQT1zOTYtYyIsImlzcyI6Imh0dHBzOi8vc2VjdXJldG9rZW4uZ29vZ2xlLmNvbS94LXgteC10ZXN0IiwiYXVkIjoieC14LXgtdGVzdCIsImF1dGhfdGltZSI6MTY5MDc4MjA4MywidXNlcl9pZCI6IklTM1RId3BxVFRRa1gxYjF2Q2FXd3VtUEs0YTIiLCJzdWIiOiJJUzNUSHdwcVRUUWtYMWIxdkNhV3d1bVBLNGEyIiwiaWF0IjoxNjkwNzgyMDgzLCJleHAiOjE2OTA3ODU2ODMsImVtYWlsIjoieC5zeWFpZnVsbGFoLnhAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImZpcmViYXNlIjp7ImlkZW50aXRpZXMiOnsiZ29vZ2xlLmNvbSI6WyIxMTczNzY2NDkwNjY2OTkyMDU5MDMiXSwiZW1haWwiOlsieC5zeWFpZnVsbGFoLnhAZ21haWwuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoicGFzc3dvcmQifX0.KtxHq-rPNKeLTxSizX5W1ovYrNynFd7fph3heH3zMErs2lSY1aokeHg70yVqSc4akixGKDc80DWXADxesLAaWdOABSfL_bRKmAyT7FQtOXyQRxMlA5M9vF6QGaWEoU3FkYtKWk78buBK7XlTodcv96cE80ehEk7NVEy57fKy8BdCp1aVa1tQAqEkqnWoyOqYYZr80FBiW2TXN2DY-z6YjHlt4FNBLKHg8pnn8ycgWC4bl9FvTjAafg8Kw9mGj2yh2MS8Uzce_bwnw78P1ut_LvejdjCdrvqrBAdxunnqa4lfjpHfiXwnwC1AAdiLP4xyurPzepA7kNTn969NUYOIvw"
//        val response = dataSource.sendOobCode(Code.VerifyEmail(token))
//        read(response)
//    }

    //
//    @Test
//    fun sendOobCodePasswordReset() = runBlocking<Unit> {
//        val response = dataSource
//            .sendOobCode(OobType.PasswordReset("roottingandroid@gmail.com"))
//        read(response)
//    }
//
//    @Test
//    fun signUp() = runBlocking<Unit> {
//        val type = SignUpType.Password(
//            password = "123456", data = UserData(email = "x.syaifullah.x@gmail.com", phoneNumber = "+628")
//        )
//        val response = dataSource.signUp(type)
//        read(response)
//    }

    //
//    @Test
//    fun signInWithCostumeToken() = runBlocking<Unit> {
//        val token =
//            "eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL2lkZW50aXR5dG9vbGtpdC5nb29nbGVhcGlzLmNvbS9nb29nbGUuaWRlbnRpdHkuaWRlbnRpdHl0b29sa2l0LnYxLklkZW50aXR5VG9vbGtpdCIsImV4cCI6MTY2NzE0NzMwMywiaWF0IjoxNjY3MTQzNzAzLCJpc3MiOiJmaXJlYmFzZS1hZG1pbnNkay01MXdvYUBhdXRoLWEyMmU2LmlhbS5nc2VydmljZWFjY291bnQuY29tIiwic3ViIjoiZmlyZWJhc2UtYWRtaW5zZGstNTF3b2FAYXV0aC1hMjJlNi5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsInVpZCI6IjEifQ.OXe4beecyOSovN-E9JqLEHOVxPw9haiiMcd9A4udHbR1H6PvFJ_ujAh6gmaIG7SYNZR_SIJxCQeCXiskGVWNcItilS_e6tjw5a9dXdBuPZLRriqEpgnwKIT_kBXm-O8goyLyPWCUYUCEZRrxbtc7wYbkgja3oGuSXQaBkEpIthOuiqBdnjGnrxDm1CFcU_OeUCpKIYIk6hvg2uzli3Cqe69zzsuVfuNr2iq2iX8n2WHh8S6ewlG_l3sAj0t8NucP95IX8gTCQUMxJ26830AxxkZ-hX1sKqBQ7A4Y76GU19Wwu-HTVeobWAcdyKtum6zuUV7yTIJYnSGk5Vdr_Td7Wg"
//        val response = dataSource.signIn(SignInType.CostumeToken(token))
//        read(response)
//    }

//    @Test
//    fun signInWithPassword() = runBlocking<Unit> {
//        val response = dataSource.signIn(
//            SignInType.Password("x.syaifullah.x@gmail.com", "123456")
//        )
//        read(response)
//    }

    private suspend fun read(response: Response<InputStream>): String {
        println(response.header)

        val out = ByteArrayOutputStream()

        response.body.read(bufferSize = 10, onRead = { bytes ->
            out.write(bytes, 0, bytes.size)
        }, onReadComplete = {
            println("onComplete: $out")
        }, onError = { err ->
            err.printStackTrace()
        })
        return out.toString()
    }
}