package id.xxx.module.auth.data.source.remote.auth.email

import id.xxx.module.auth.repository.source.remote.auth.email.AuthDataSourceRemote
import org.junit.Assert
import org.junit.Test

internal class AuthDataSourceRemoteTest {

//    private val dataSource = AuthDataSourceRemote.getInstance()

    @Test
    fun test() {
        Assert.assertEquals(true, true)
    }

//    @Test
//    fun sendVerificationCode() = runBlocking {

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
//    }

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
//        val idToken =
//            "eyJhbGciOiJSUzI1NiIsImtpZCI6IjdjZjdmODcyNzA5MWU0Yzc3YWE5OTVkYjYwNzQzYjdkZDJiYjcwYjUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20veC14LXgtcHJvamVjdHMiLCJhdWQiOiJ4LXgteC1wcm9qZWN0cyIsImF1dGhfdGltZSI6MTcwNTQ2MDYxOSwidXNlcl9pZCI6Im12ckVzTFdFdVVNTERDSUFtejkyY2JrWk1tbzEiLCJzdWIiOiJtdnJFc0xXRXVVTUxEQ0lBbXo5MmNia1pNbW8xIiwiaWF0IjoxNzA1NDYwNjE5LCJleHAiOjE3MDU0NjQyMTksImVtYWlsIjoieC5zeWFpZnVsbGFoLnhAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7ImVtYWlsIjpbInguc3lhaWZ1bGxhaC54QGdtYWlsLmNvbSJdfSwic2lnbl9pbl9wcm92aWRlciI6InBhc3N3b3JkIn19.WQuqWSlcdRlgjKPZ8-AdnajLcwIQ1ScvIGWmbboDZ0mt0Iwg-fzcaYnrq95NwjcREFG8RVyg1W89ekrkYkweNN3YcSsW03pUGSYDPAVzu8Jh9BYKiIXCikVmaQ8h-qikKe9eOnIdqqEGrcLJYziGwvOy5zH-y4ivS15uZy28mBrthhcopn1Yclb0TwATNJvBNSHRIP5QLWxTUIVbfl_aKWxRVHl7_eIRTwD9IFTJ_N17xyef8zITqGD6n1OnDI5R_IsCOWKUkO8Ikxdzg_354mL1YfZkpTzVgvIg4Z8jtt_WtxlAH1txAjWAh5nC8qIwGr9Q48hYW_7F02Uhz0Fvgw"
//        val response = dataSource.lookup(idToken)
//        val a = dataSource.linkWithOAuthCredential(
//            idToken, idToken, "password"
//        )
//        val result = String(a.body.readAllBytes())
//        println(result)
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

//    @Test
//    fun signUp() = runBlocking<Unit> {
//        val type = SignType.PasswordUp(
//            password = "123456",
//            data = UserData(email = "x.syaifullah.x@gmail.com", phoneNumber = "+628")
//        )
//        val response = dataSource.sign(type)
//        val result = JSONObject(String(response.body.readAllBytes()))
//        println(result)
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
}