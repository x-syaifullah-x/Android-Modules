package id.xxx.module.auth.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import id.xxx.module.auth.fragment.listener.ISecurityChallengeDialogFragment
import id.xxx.module.auth.ktx.get
import id.xxx.module.auth.model.SecurityChallengeResult
import id.xxx.module.auth_presentation.R
import id.xxx.module.auth_presentation.databinding.RecaptchaFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class SecurityChallengeDialogFragment : DialogFragment() {

    companion object {
        const val KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view: View = layoutInflater.inflate(R.layout.recaptcha_fragment, null)
        val dialog = AlertDialog.Builder(view.context)
            .setView(view)
            .create()
        val binding = RecaptchaFragmentBinding.bind(view)

        val webView = binding.webView
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.setLayerType(WebView.LAYER_TYPE_HARDWARE, null);
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                val message =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        "${error.description}"
                    else
                        "Error"
                get<ISecurityChallengeDialogFragment>()
                    ?.onResult(SecurityChallengeResult.Error(Throwable(message)))
                dismiss()
            }
        }

        val webSettings = webView.settings
        @SuppressLint("SetJavaScriptEnabled")
        webSettings.javaScriptEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE

        val phoneNumber = arguments?.getString(KEY_PHONE_NUMBER)
            ?: throw Throwable("required argument phone number")

        webView.addJavascriptInterface(object : Any() {
            @Suppress("unused")
            @JavascriptInterface
            fun onRender() {
                lifecycleScope.launch(Dispatchers.Main) {
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }

            @Suppress("unused")
            @JavascriptInterface
            fun onSubmit(isNewUser: Boolean, response: String) {
                lifecycleScope.launch(Dispatchers.Main) {
                    get<ISecurityChallengeDialogFragment>()?.onResult(
                        SecurityChallengeResult.Success(
                            isNewUser = isNewUser,
                            response = response,
                            phoneNumber = phoneNumber,
                        )
                    )
                }
                dismiss()
            }
        }, "RecaptchaCallback")

        val uri = Uri.Builder()
            .scheme("https")
            .authority("x-x-x-test.web.app")
            .path("/recaptcha/index.html")
            .appendQueryParameter("phoneNumber", phoneNumber)
            .appendQueryParameter("languageCode", Locale.getDefault().language)
            .build()
        webView.loadUrl(uri.toString())
        return dialog
    }
}