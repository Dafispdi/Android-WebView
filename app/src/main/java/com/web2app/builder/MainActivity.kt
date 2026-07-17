package com.web2app.builder

import android.app.Activity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
// IMPORT ONESIGNAL (Akan diproses otomatis oleh GitHub Actions jika ada ID)
import com.onesignal.OneSignal

class MainActivity : Activity() {
    private lateinit var webView: WebView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // --- 1. INJEKSI ONESIGNAL (PUSH NOTIFICATION) ---
        val oneSignalAppId = "ONESIGNAL_PLACEHOLDER"
        if (oneSignalAppId != "ONESIGNAL_PLACEHOLDER" && oneSignalAppId.isNotEmpty()) {
            OneSignal.initWithContext(this)
            OneSignal.setAppId(oneSignalAppId)
        }
        
        webView = WebView(this)
        setContentView(webView)
        
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        
        // --- 2. MODE OFFLINE (CUSTOM NO INTERNET PAGE) ---
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                
                // Pastikan yang gagal adalah halaman utama, bukan cuma error load gambar
                if (request?.isForMainFrame == true) {
                    // Desain halaman offline keren ala Obsidian Forge
                    val offlineHtml = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <style>
                                body { background-color: #0a0a0c; color: #dcb8ff; display: flex; flex-direction: column; justify-content: center; align-items: center; height: 90vh; font-family: sans-serif; text-align: center; padding: 20px; }
                                .box { background: rgba(255,255,255,0.05); padding: 30px; border-radius: 20px; border: 1px solid rgba(255,255,255,0.1); }
                                h2 { margin-bottom: 10px; color: #8a2be2; }
                                p { color: #cfc2d7; font-size: 14px; margin-bottom: 20px; }
                                button { background: linear-gradient(135deg, #8A2BE2, #6A00FF); color: white; border: none; padding: 12px 24px; border-radius: 12px; font-weight: bold; cursor: pointer; transition: 0.3s; }
                                button:active { transform: scale(0.95); }
                            </style>
                        </head>
                        <body>
                            <div class="box">
                                <h2>Koneksi Terputus</h2>
                                <p>Pastikan Anda terhubung ke internet<br>lalu coba lagi.</p>
                                <button onclick="window.location.reload()">Muat Ulang Halaman</button>
                            </div>
                        </body>
                        </html>
                    """.trimIndent()
                    
                    view?.loadDataWithBaseURL(null, offlineHtml, "text/html", "UTF-8", null)
                }
            }
        }
        
        webView.webChromeClient = WebChromeClient()
        
        // --- 3. TARGET URL INJEKSI ---
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
