package com.sealed.app.activity

import android.content.Context
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebViewAssetLoader
import java.io.BufferedInputStream
import java.io.IOException
import java.util.*

class WebVncStreamActivity : AppCompatActivity() {

    private class WebAppInterface internal constructor(
        var mContext: Context,
        @get:JavascriptInterface var getewayURL: String,
        @get:JavascriptInterface var aPIToken: String,
        @get:JavascriptInterface var appName: String
    )

    private val webView by lazy {
        WebView(this)
    }

    private val gatewayURL = "https://a6eab025ed75582ea.awsglobalaccelerator.com"
    private val apiToken = "AgEUYW5ib3gtc3RyZWFtLWdhdGV3YXkCBmRhdmlkMQACFDIwMjItMDQtMDRUMTI6NDY6MTJaAAAGIK90GpF9y64DHwz9vk5TXq1q88-gpDRs1lwquK9bUAuv"
    private val appName = "CNN"

    private val TAG = "WebVncStreamActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(webView)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                url: String
            ): Boolean {
                view.loadUrl(url)
                return true
            }
        }

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.loadWithOverviewMode = true

        webSettings.allowUniversalAccessFromFileURLs = true
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true
        webSettings.allowFileAccessFromFileURLs = true
        webSettings.allowUniversalAccessFromFileURLs = true

        WebView.setWebContentsDebuggingEnabled(true)

        val assetLoader: WebViewAssetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                if (!request.isForMainFrame && Objects.requireNonNull(request.url.path)
                        ?.endsWith(".js") == true
                ) {
                    Log.d(TAG, " js file request need to set mime/type " + request.url.path)
                    try {
                        return WebResourceResponse(
                            "application/javascript", null,
                            BufferedInputStream(
                                view.context.assets.open(
                                    request.url.path!!.replace("/assets/", "")
                                )
                            )
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                return assetLoader.shouldInterceptRequest(request.url)
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                Log.d(TAG, "error: " + request.url)
            }

            override fun onReceivedSslError(
                view: WebView, handler: SslErrorHandler,
                error: SslError
            ) {
                Log.d(TAG, "Ssl error: " + error.primaryError)
                handler.proceed()
            }
        }

        webView.addJavascriptInterface(
            WebAppInterface(
                this,
                gatewayURL,
                apiToken,
                appName
            ), "Android"
        )

        webView.loadUrl("https://appassets.androidplatform.net/assets/index.html")
    }
}