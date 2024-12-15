package id.xxx.module.autentication.phone

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import id.xxx.module.ktx.hideSoftInput
import id.xxx.module.viewbinding.ktx.viewBinding
import id.xxx.modules.authentication.presentation.databinding.FragmentRacaptchaBinding
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Locale

class PhoneOTPSanderFragment : Fragment() {

    companion object {

        const val DATA_EXTRA_PHONE_NUMBER = "KEY_PHONE_NUMBER"
    }

    private val vBinding by viewBinding<FragmentRacaptchaBinding>()

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (vBinding.webView.canGoBack()) {
                vBinding.webView.goBack(); return
            }
            isEnabled = false
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = vBinding.root.apply {
        container?.hideSoftInput()
        if (background == null)
            background = activity?.window?.decorView?.background
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.onBackPressedDispatcher
            ?.addCallback(viewLifecycleOwner, onBackPressedCallback)

        val phoneNumber = arguments?.getString(DATA_EXTRA_PHONE_NUMBER)
            ?: throw Throwable("required argument phone number")

        val webView = vBinding.webView
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.setLayerType(WebView.LAYER_TYPE_HARDWARE, Paint())
        webView.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                println(error.description)
            }
        }

        val webSettings = webView.settings
        @SuppressLint("SetJavaScriptEnabled")
        webSettings.javaScriptEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE

        webView.addJavascriptInterface(object : Any() {
            @Suppress("unused")
            @JavascriptInterface
            fun onRender() {
                lifecycleScope.launch {
                    vBinding.progressBar.visibility = View.GONE
                }
            }

            @Suppress("unused")
            @JavascriptInterface
            fun onSend(response: String) {
                lifecycleScope.launch {
                    val j = JSONObject(response)
                    try {
                        val sessionInfo = j.getString("sessionInfo")
                        val args = bundleOf(
                            PhoneOTPVerifierFragment.DATA_EXTRA_PHONE_NUMBER to phoneNumber,
                            PhoneOTPVerifierFragment.DATA_EXTRA_SESSION_INFO to sessionInfo,
                        )
                        parentFragmentManager.beginTransaction()
                            .add(id, PhoneOTPVerifierFragment::class.java, args, null)
                            .remove(this@PhoneOTPSanderFragment)
                            .commit()
                    } catch (t: Throwable) {
                        val err = j.getJSONObject("error")
                        val errMessage = err.getString("message")
                        Toast.makeText(vBinding.root.context, errMessage, Toast.LENGTH_LONG).show()
                        parentFragmentManager.beginTransaction()
                            .remove(this@PhoneOTPSanderFragment)
                            .commit()
                    }
                }
            }
        }, "SendVerificationCode")

        val uri = Uri.Builder()
            .scheme("https")
            .authority("send-verification-code.web.app")
            .appendQueryParameter("phoneNumber", phoneNumber)
            .appendQueryParameter("languageCode", Locale.getDefault().language)
            .build()
        webView.loadUrl(uri.toString())
    }

    override fun onDestroyView() {
        val webView = vBinding.webView
        webView.removeAllViews()
//        webView.destroy()
        super.onDestroyView()
    }
}