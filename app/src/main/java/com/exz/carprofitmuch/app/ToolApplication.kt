package com.exz.carprofitmuch.app

import android.app.Application
import com.exz.carprofitmuch.bean.MyObjectBox
import com.szw.framelibrary.app.MyApplication
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.Config
import com.umeng.socialize.PlatformConfig
import io.objectbox.BoxStore


/**
 * Created by 史忠文
 * on 2017/10/16.
 */

class ToolApplication : MyApplication() {
    lateinit var boxStore: BoxStore//数据库入口

    override fun getSaltStr(): String? = "9E127DFDDA4F0BAB43B3"

    override fun onCreate() {
        super.onCreate()
        init()
        //数据库初始化
        boxStore = MyObjectBox.builder().androidContext(this).build()

        UMConfigure.setLogEnabled(true)
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        UMConfigure.init(this, "5a3385fcf43e48403b000383", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "")
        //开启ShareSDK debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true
        // PushSDK初始化(如使用推送SDK，必须调用此方法)
        initUpush()

    }

    private fun initUpush() {
        //各个平台的配置，建议放在全局Application或者程序入口
        PlatformConfig.setWeixin("wxc1c40b1c65f34382", "3a5965cf6f028323fd377f688f5a7f0a")
        PlatformConfig.setQQZone("1106614642", "WaztDIwsIZ03bgCL")
    }


    companion object {
        fun getAPP(app:Application):ToolApplication = app as ToolApplication
    }
}
