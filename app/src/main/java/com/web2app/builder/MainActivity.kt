package com.web2app.builder

import android.app.Activity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

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
        
        // 2. Setup WebViewClient (Menangani loading dan error)
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
                            <div style="background:rgba(255,255,255,0.05); padding:20px; border-radius:15px; border: 1px solid rgba(255,255,255,0.1);">
                                <h2 style="color:#8a2be2; margin-bottom:10px;">Koneksi Terputus</h2>
                                <p style="color:#cfc2d7; font-size:14px; margin-bottom:20px;">Pastikan Anda terhubung ke internet.</p>
                                <button onclick="window.location.reload()" style="background: linear-gradient(135deg, #8A2BE2, #6A00FF); color: white; border: none; padding: 12px 24px; border-radius: 12px; font-weight: bold; cursor: pointer;">Muat Ulang</button>
                            </div>
                        </body>
                        </html>
                    """.trimIndent()
                    view?.loadDataWithBaseURL(null, offlineHtml, "text/html", "UTF-8", null)
                }
            }
        }
        
        webView.webChromeClient = WebChromeClient()
        
        // 3. Load URL Target
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
