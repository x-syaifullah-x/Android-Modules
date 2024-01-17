package id.xxx.module.auth.fragment.phone

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.lifecycle.lifecycleScope
import id.xxx.module.auth.fragment.base.BaseFragment
import id.xxx.module.auth.fragment.phone.listener.IRecaptchaFragment
import id.xxx.module.auth.ktx.getListener
import id.xxx.module.auth_presentation.databinding.RecaptchaFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class RecaptchaFragment : BaseFragment<RecaptchaFragmentBinding>() {

    companion object {

        const val KEY_PHONE_NUMBER = "KEY_PHONE_NUMBER"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = viewBinding.webView
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.setLayerType(WebView.LAYER_TYPE_HARDWARE, Paint())
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                viewBinding.progressBar.visibility = View.GONE
            }

            override fun onReceivedError(
                view: WebView, request: WebResourceRequest, error: WebResourceError
            ) {
                val message =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) "${error.description}"
                    else "Error"
                parentFragmentManager.popBackStack()
                getListener<IRecaptchaFragment>()?.onAction(
                    IRecaptchaFragment.Action.Error(
                        Throwable(message)
                    )
                )
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
            fun onSubmit(response: String) {
                lifecycleScope.launch(Dispatchers.Main) {
                    parentFragmentManager.popBackStack()
                    getListener<IRecaptchaFragment>()?.onAction(
                        IRecaptchaFragment.Action.Success(
                            response = response,
                            phoneNumber = phoneNumber,
                        )
                    )
                }
            }
        }, "RecaptchaCallback")

        val phoneNumberFinal = phoneNumber.replace("+", "%2b")
        val uri =
            Uri.Builder().scheme("https").authority("x-recaptcha-x.web.app").path("/index.html")
                .appendQueryParameter("phoneNumber", phoneNumberFinal)
                .appendQueryParameter("languageCode", Locale.getDefault().language).build()
        webView.loadUrl(uri.toString())
    }

    override fun onDestroyView() {
        viewBinding.webView.removeAllViews()
        viewBinding.webView.destroy()
        super.onDestroyView()
    }
}