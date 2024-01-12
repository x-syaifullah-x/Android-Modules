package id.xxx.example.chat.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.xxx.module.google_sign.GoogleAccountContract

class MainActivity : AppCompatActivity() {


    private val activityResultLauncher =
        registerForActivityResult(GoogleAccountContract()) { result ->
            if (result != null) {
//                auth.signInWithCredential(
//                    GoogleAuthProvider.getCredential("${result.idToken}", null)
//                ).addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        println(it.result.user)
//                    } else {
//                        println(it.exception?.message)
//                    }
//                }
            } else {
                println("Canceled")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultLauncher.launch(null)
    }
}