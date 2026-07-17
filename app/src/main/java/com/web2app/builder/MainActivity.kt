package com.web2app.builder

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : Activity() {
    private lateinit var webView: WebView
    
    // Simpan URL terakhir buat fitur Muat Ulang
    private var currentUrl = "TARGET_URL_PLACEHOLDER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = WebView(this)
        setContentView(webView)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowContentAccess = true
            allowFileAccess = true
            setSupportMultipleWindows(false) 
            javaScriptCanOpenWindowsAutomatically = true
        }

        webView.webViewClient = object : WebViewClient() {
            
            // Tangani link khusus agar tidak dianggap Error
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()

                if (url.startsWith("http://") || url.startsWith("https://")) {
                    currentUrl = url
                    return false 
                }

                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    view?.context?.startActivity(intent)
                } catch (e: Exception) {
                    // Abaikan jika aplikasi tujuan tidak ada
                }
                return true
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                if (request?.isForMainFrame == true) {
                    
                    if (error?.errorCode != ERROR_UNSUPPORTED_SCHEME) {
                        // Desain keren Obsidian Forge - Dikonversi ke Pure CSS agar tahan Offline
                        val offlineHtml = """
                            <!DOCTYPE html>
                            <html lang="id">
                            <head>
                                <meta charset="UTF-8">
                                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                <style>
                                    * { box-sizing: border-box; margin: 0; padding: 0; }
                                    body {
                                        background-color: #0a0a0c;
                                        color: #e5e1e4;
                                        font-family: sans-serif;
                                        min-height: 100vh;
                                        display: flex;
                                        align-items: center;
                                        justify-content: center;
                                        overflow: hidden;
                                        position: relative;
                                        padding: 24px;
                                    }
                                    .ambient-glow {
                                        position: absolute;
                                        top: 50%;
                                        left: 50%;
                                        transform: translate(-50%, -50%);
                                        width: 500px;
                                        height: 500px;
                                        background-color: rgba(138, 43, 226, 0.1);
                                        border-radius: 50%;
                                        filter: blur(120px);
                                        animation: pulse-slow 4s cubic-bezier(0.4, 0, 0.6, 1) infinite;
                                        z-index: 0;
                                        pointer-events: none;
                                    }
                                    .glass-panel {
                                        background: rgba(255, 255, 255, 0.03);
                                        backdrop-filter: blur(20px);
                                        -webkit-backdrop-filter: blur(20px);
                                        border: 1px solid rgba(255, 255, 255, 0.1);
                                        max-width: 384px;
                                        width: 100%;
                                        padding: 32px;
                                        border-radius: 2rem;
                                        text-align: center;
                                        position: relative;
                                        z-index: 10;
                                        display: flex;
                                        flex-direction: column;
                                        align-items: center;
                                        gap: 24px;
                                        box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
                                    }
                                    .icon-container {
                                        width: 80px;
                                        height: 80px;
                                        border-radius: 50%;
                                        background: rgba(255, 255, 255, 0.05);
                                        border: 1px solid rgba(255, 255, 255, 0.1);
                                        display: flex;
                                        align-items: center;
                                        justify-content: center;
                                        margin-bottom: 8px;
                                    }
                                    .icon-container svg {
                                        width: 36px;
                                        height: 36px;
                                        fill: #dcb8ff;
                                    }
                                    .text-container h2 {
                                        font-size: 30px;
                                        font-weight: 700;
                                        letter-spacing: -0.025em;
                                        color: #ffffff;
                                        margin-bottom: 8px;
                                    }
                                    .text-container p {
                                        color: #cfc2d7;
                                        font-size: 16px;
                                        line-height: 1.625;
                                    }
                                    .btn-primary {
                                        background: linear-gradient(135deg, #8A2BE2, #6A00FF);
                                        transition: all 0.3s ease;
                                        width: 100%;
                                        padding: 16px 24px;
                                        border-radius: 1rem;
                                        color: #ffffff;
                                        font-weight: 700;
                                        font-size: 18px;
                                        border: none;
                                        cursor: pointer;
                                        box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
                                    }
                                    .btn-primary:hover {
                                        box-shadow: 0 0 20px rgba(138, 43, 226, 0.4);
                                        transform: translateY(-1px);
                                    }
                                    .btn-primary:active {
                                        transform: translateY(0);
                                    }
                                    .version-text {
                                        color: rgba(255, 255, 255, 0.2);
                                        font-size: 12px;
                                        text-transform: uppercase;
                                        letter-spacing: 0.2em;
                                        font-weight: 500;
                                    }
                                    @keyframes pulse-slow {
                                        0%, 100% { opacity: 0.3; transform: translate(-50%, -50%) scale(1); }
                                        50% { opacity: 0.5; transform: translate(-50%, -50%) scale(1.05); }
                                    }
                                </style>
                            </head>
                            <body>
                                <div class="ambient-glow"></div>
                                <div class="glass-panel">
                                    <div class="icon-container">
                                        <!-- SVG WiFi Off langsung di-embed agar tidak butuh internet -->
                                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><path d="M23.64 7C20.46 4.3 16.42 2.68 12 2.68c-1.39 0-2.73.18-4.01.5L9.67 4.87c.76-.12 1.54-.19 2.33-.19 3.86 0 7.37 1.39 10.09 3.7l1.55-1.38zM20.35 10.29l1.55-1.38c-.37-.31-.76-.6-1.16-.88l-1.5 1.5c.38.23.75.49 1.11.76zm-17.7 12.3l4.08-4.08C5.07 18 3.51 16.94 2 15.65l1.55-1.38c1.37 1.18 3.01 2.1 4.79 2.63l2.84-2.84C9.69 13.9 8.35 13.56 7 12.91L5.5 14.41c1.61.91 3.39 1.5 5.31 1.69l2.09-2.09c-.31-.05-.62-.12-.93-.19L14.7 11.1c.36.06.7.13 1.04.22l2.64-2.64c-1.92-1.34-4.22-2.12-6.68-2.12-.51 0-1.02.04-1.52.12L7.49 3.9C8.92 3.31 10.42 3 12 3c4.1 0 7.9 1.48 10.87 3.96L24 5.86c-3.23-2.69-7.44-4.36-12-4.36-1.81 0-3.56.33-5.18.94L1.39.04.04 1.39l22.58 22.58 1.34-1.35-6.31-6.31z"/></svg>
                                    </div>
                                    <div class="text-container">
                                        <h2>Koneksi Terputus</h2>
                                        <p>Pastikan Anda terhubung ke internet dan coba lagi.</p>
                                    </div>
                                    <!-- FIX: Tombol langsung ngarah ke variabel Kotlin -->
                                    <button onclick="window.location.href='$currentUrl'" class="btn-primary">
                                        Muat Ulang
                                    </button>
                                    <p class="version-text">Obsidian Forge v2.4</p>
                                </div>
                            </body>
                            </html>
                        """.trimIndent()
                        view?.loadDataWithBaseURL(null, offlineHtml, "text/html", "UTF-8", null)
                    }
                }
            }
        }

        webView.webChromeClient = WebChromeClient()
        webView.loadUrl(currentUrl)
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
