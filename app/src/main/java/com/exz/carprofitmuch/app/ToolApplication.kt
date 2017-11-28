package com.exz.carprofitmuch.app

import android.app.Application
import com.exz.carprofitmuch.bean.MyObjectBox
import com.szw.framelibrary.app.MyApplication
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

    }
    companion object {
        fun getAPP(app:Application):ToolApplication = app as ToolApplication
    }
}
