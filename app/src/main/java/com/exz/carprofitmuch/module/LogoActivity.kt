package com.exz.carprofitmuch.module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.blankj.utilcode.util.EncryptUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.User
import com.exz.carprofitmuch.config.Urls
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import kotlinx.android.synthetic.main.activity_logo.*
import java.util.*


class LogoActivity : BaseActivity() {

    lateinit var autoLoginToken: String
    private var type = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        autoLoginToken = PreferencesService.getAutoLoginToken(this)?:""
        val anim = AnimationUtils.loadAnimation(this, R.anim.logo_fade_in)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (!TextUtils.isEmpty(autoLoginToken)) {
                    login()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                val preferences = getSharedPreferences("CPM",
                        Context.MODE_PRIVATE)
                val flag = preferences.getBoolean("FirstRun", false)
                if (flag) {
                    val editor = preferences.edit()
                    editor.putBoolean("FirstRun", false)
                    editor.apply()
                    //                    LogoActivity.this.startActivity(new Intent(
                    //                            LogoActivity.this, FirstRunActivity.class));
                    //                    finish();
                } else {

                    if (TextUtils.isEmpty(autoLoginToken)) {
                        MyApplication.user=null
                        type = 1
                        jump(type)
                    }
                }
            }
        })
        img_logo.animation = anim

    }


    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_logo


    /**
     * 登录
     * */
    fun login() {

        val params = HashMap<String, String>()
        params.put("loginDeviceType", "0")
        params.put("autoLoginToken", autoLoginToken)
//      params.put("jpushToken", JPushInterface.getRegistrationID(this))
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(autoLoginToken, salt).toLowerCase())
        OkGo.post<NetEntity<User>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<User>>(this) {
                    override fun onSuccess(response: Response<NetEntity<User>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            MyApplication.user=response.body().data
                        }else{
                            MyApplication.user=null
                        }
                    }

                    override fun onError(response: Response<NetEntity<User>>) {
                        super.onError(response)
                        type = 1
                        jump(type)
                    }

                })
    }
    /**
     * @param type 0 主界面，1， 登录
     */
    private fun jump(type: Int) {
        var intent: Intent? = null
        if (type == 0) {
            intent = Intent(this@LogoActivity, MainActivity::class.java)
        } else {
            startActivity(Intent(this@LogoActivity, MainActivity::class.java))
            //            intent = new Intent(LogoActivity.this, LoginActivity.class);
        }
        if (intent != null)
            startActivity(intent)
        this@LogoActivity.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}

