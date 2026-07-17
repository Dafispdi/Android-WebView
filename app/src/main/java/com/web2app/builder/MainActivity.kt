package com.web2app.builder

import android.app.Activity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.onesignal.OneSignal
import android.Manifest
import android.os.Build
import androidx.core.app.ActivityCompat

class MainActivity : Activity() {
    private lateinit var webView: WebView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. Inisialisasi WebView dulu dan pasang ke Layout
        webView = WebView(this)
        setContentView(webView)
        
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowContentAccess = true
            allowFileAccess = true
        }
        
        // 2. Injeksi OneSignal
        val oneSignalAppId = "ONESIGNAL_PLACEHOLDER"
        if (oneSignalAppId != "ONESIGNAL_PLACEHOLDER" && oneSignalAppId.isNotEmpty()) {
            OneSignal.initWithContext(this)
            OneSignal.setAppId(oneSignalAppId)
            
            // Minta izin notifikasi (Android 13+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
            }
        }
        
        // 3. Setup WebViewClient
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                if (request?.isForMainFrame == true) {
                    val offlineHtml = """
                        <!DOCTYPE html>
                        <html>
                        <body style="background:#0a0a0c; color:#dcb8ff; display:flex; justify-content:center; align-items:center; height:100vh; font-family:sans-serif; text-align:center;">
                            <div style="background:rgba(255,255,255,0.05); padding:20px; border-radius:15px;">
                                <h2>Koneksi Terputus</h2>
                                <p>Pastikan Anda terhubung ke internet.</p>
                                <button onclick="window.location.reload()" style="padding:10px 20px; cursor:pointer;">Muat Ulang</button>
                            </div>
                        </body>
                        </html>
                    """.trimIndent()
                    view?.loadDataWithBaseURL(null, offlineHtml, "text/html", "UTF-8", null)
                }
            }
        }
        
        webView.webChromeClient = WebChromeClient()
        
        // 4. Load URL
        webView.loadUrl("TARGET_URL_PLACEHOLDER")
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
