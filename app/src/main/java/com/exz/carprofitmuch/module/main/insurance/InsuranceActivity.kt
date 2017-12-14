//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.exz.carprofitmuch.module.main.insurance

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.support.v4.content.ContextCompat
import android.webkit.*
import com.exz.carprofitmuch.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import im.delight.android.webview.AdvancedWebView.Listener
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_insuance_web.*

class InsuranceActivity : BaseActivity(), Listener {

    override fun initToolbar(): Boolean {
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mWebView)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_insuance_web

    override fun init() {
        mWebView.setListener(this, this)
        mWebView.loadUrl(this.intent.getStringExtra(Intent_Url))
        mWebView.settings.javaScriptEnabled = true
        //启用数据库
        val webSettings = mWebView.settings
        webSettings.databaseEnabled = true
        val dir = this.applicationContext.getDir("database", Context.MODE_PRIVATE).path

        //启用地理定位
        webSettings.setGeolocationEnabled(true)
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir)

        //最重要的方法，一定要设置，这就是出不来的主要原因

        webSettings.domStorageEnabled = true


        mWebView.setBackgroundColor(ContextCompat.getColor(this.mContext, R.color.app_bg))

        
        mWebView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
                //handler.cancel(); 默认的处理方式，WebView变成空白页
                handler.proceed()
                //handleMessage(Message msg); 其他处理
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return true
            }

        }
        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {}

            //配置权限（同样在WebChromeClient中实现）
            override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
                callback.invoke(origin, true, false)
                super.onGeolocationPermissionsShowPrompt(origin, callback)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mWebView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mWebView.onPause()
    }

    override fun onDestroy() {
        mWebView.onDestroy()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)
        mWebView.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onBackPressed() {
        if (mWebView.onBackPressed()) {
            super.onBackPressed()
        }
    }

    override fun onPageStarted(url: String, favicon: Bitmap) {}

    override fun onPageFinished(url: String) {}

    override fun onPageError(errorCode: Int, description: String, failingUrl: String) {}

    override fun onDownloadRequested(url: String, suggestedFilename: String, mimeType: String, contentLength: Long, contentDisposition: String, userAgent: String) {}

    override fun onExternalPageRequest(url: String) {}

    companion object {
        var Intent_Url = "info"
        var Intent_Title = "name"
    }
}
