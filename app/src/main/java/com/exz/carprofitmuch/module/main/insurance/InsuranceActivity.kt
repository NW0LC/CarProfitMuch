//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.exz.carprofitmuch.module.main.insurance

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.webkit.*
import com.blankj.utilcode.util.EncryptUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.config.Urls.SendIntegral
import com.exz.carprofitmuch.utils.RC4Util
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import im.delight.android.webview.AdvancedWebView.Listener
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_insuance_web.*
import org.jetbrains.anko.toast


class InsuranceActivity : BaseActivity(), Listener {

    private var isAnimStart = false
    private var currentProgress: Int = 0
    override fun initToolbar(): Boolean {
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, lay_content)
        toolbar.setNavigationOnClickListener {
            if (mWebView.onBackPressed()) {
                finish()
            }
        }
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_insuance_web

    override fun init() {
        mWebView.setListener(this, this)
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
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = View.VISIBLE
                progressBar.alpha = 1.0f
            }
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError?) {
                //handler.cancel(); 默认的处理方式，WebView变成空白页
                handler.proceed()
                //handleMessage(Message msg); 其他处理
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url?.contains("weixin://")==true) { // 打开微信
                    val uri = Uri.parse(url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    try {
                        startActivity(intent)
                    } catch (e: Exception) {
                        AlertDialog.Builder(this@InsuranceActivity)
                                .setMessage("未检测到微信客户端，请安装后重试。")
                                .setPositiveButton("知道了", null).show()
                    }

                    return true
                } else if (url?.startsWith("alipays:")==true || url?.startsWith("alipay")==true) { // 支付宝 调出APP
                    try {
                        startActivity(Intent("android.intent.action.VIEW", Uri.parse(url)))
                    } catch (e: Exception) {
                        AlertDialog.Builder(this@InsuranceActivity)
                                .setMessage("未检测到支付宝客户端，请安装后重试。")
                                .setPositiveButton("立即安装") { _, _ ->
                                    val alipayUrl = Uri.parse("https://d.alipay.com")
                                    startActivity(Intent("android.intent.action.VIEW", alipayUrl))
                                }.setNegativeButton("取消", null).show()
                    }
                    return true
                }else {
                    view?.loadUrl(url)
                }
                return true
            }

        }
        mWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                currentProgress = progressBar.progress
                if (newProgress >= 100 && !isAnimStart) {
                    isAnimStart = true
                    progressBar.progress = newProgress
                    startDismissAnimation(progressBar.progress)
                } else {
                   startProgressAnimation(newProgress)
                }
            }
            //配置权限（同样在WebChromeClient中实现）
            override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {
                callback.invoke(origin, true, false)
                super.onGeolocationPermissionsShowPrompt(origin, callback)
            }
        }

        editServiceState {
            val url = String.format(getString(R.string.main_Insurance),it,RC4Util.encryRC4String("{\"userID\"=\"${MyApplication.loginUserId}\"}","open20160501"))
            if (url.isNotEmpty()){
                mWebView.loadUrl(url)
            }else{
                DialogUtils.WarningWithListener(this,"该功能暂不可用", View.OnClickListener {
                    finish()
                })
            }
        }
    }
    /**
     * 买车险送积分
     * */
    private fun editServiceState(listener: (addressBean: String?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(SendIntegral)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(this) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            context.toast(response.body().message)
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }


    private fun startProgressAnimation(newProgress: Int) {
        val animator = ObjectAnimator.ofInt(progressBar, "progress", this.currentProgress, newProgress)
        animator.duration = 300L
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    private fun startDismissAnimation(progress: Int) {
        val anim = ObjectAnimator.ofFloat(progressBar, "alpha", 1.0f, 0.0f)
        anim.duration = 1500L
        anim.interpolator = DecelerateInterpolator()
        anim.addUpdateListener { valueAnimator ->
            val fraction = valueAnimator.animatedFraction
            val offset = 100 - progress
            progressBar.progress = (progress.toFloat() + offset.toFloat() * fraction).toInt()
        }
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                progressBar.progress = 0
                progressBar.visibility = View.GONE
                isAnimStart = false
            }
        })
        anim.start()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mWebView.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (mWebView.onBackPressed()) {
            super.onBackPressed()
        }
    }
    override fun onPageFinished(url: String?) {

    }

    override fun onPageError(errorCode: Int, description: String?, failingUrl: String?) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDownloadRequested(url: String?, suggestedFilename: String?, mimeType: String?, contentLength: Long, contentDisposition: String?, userAgent: String?) {
    }

    override fun onExternalPageRequest(url: String?) {
    }

    override fun onPageStarted(url: String?, favicon: Bitmap?) {
    }
}
