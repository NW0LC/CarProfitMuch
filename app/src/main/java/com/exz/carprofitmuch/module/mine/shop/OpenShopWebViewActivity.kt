package com.exz.carprofitmuch.module.mine.shop

import android.content.Context
import android.webkit.WebSettings
import com.exz.carprofitmuch.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_open_shop_webview.*


/**
 * Created by pc on 2017/11/24.
 */

class OpenShopWebViewActivity : BaseActivity() {
    override fun setInflateId(): Int {
        return R.layout.activity_open_shop_webview
    }

    override fun initToolbar(): Boolean {
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mWebView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        mTitle.text = mContext.getString(R.string.main_open_shop_map_marker)
        return false
    }

    var wz_json: String = ""
    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
        //状态栏透明和间距处理
//        mWebView.addJavascriptInterface(JavaScriptObject(this), "myObj")
        mWebView.getSettings().setJavaScriptEnabled(true);

        val dir = this.applicationContext.getDir("database", Context.MODE_PRIVATE).path

        //支持缩放，默认为true。
        mWebView.settings.setSupportZoom(true);
//调整图片至适合webview的大小
        mWebView.settings.setUseWideViewPort(true);
// 缩放至屏幕的大小
        mWebView.settings.setLoadWithOverviewMode(true);
//设置默认编码
        mWebView.settings.setDefaultTextEncodingName("utf-8");
////设置自动加载图片
        mWebView.settings.setLoadsImagesAutomatically(true);
//启用地理定位
        mWebView.settings.setGeolocationEnabled(true)
//设置定位的数据库路径
        mWebView.settings.setGeolocationDatabasePath(dir)

//最重要的方法，一定要设置，这就是出不来的主要原因
//多窗口
        mWebView.settings.supportMultipleWindows();
//获取触摸焦点
        mWebView.requestFocusFromTouch();
//允许访问文件
        mWebView.settings.setAllowFileAccess(true);
//开启javascript
        mWebView.settings.setJavaScriptEnabled(true);
        //支持通过JS打开新窗口
        mWebView.settings.setJavaScriptCanOpenWindowsAutomatically(true);
//提高渲染的优先级
        //支持内容重新布局
        mWebView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN;
//关闭webview中缓存
        mWebView.clearHistory()
        mWebView.settings.setDomStorageEnabled(true)
//        mWebView.loadUrl("file:///android_asset/address.html")
        mWebView.loadUrl("http://apis.map.qq.com/tools/locpicker?search=1&type=1&key=LEBBZ-2LCWS-3DKOF-63GND-KS6OE-R5FGS&referer=myapp")



    }


}
