package com.exz.carprofitmuch.module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.User
import com.exz.carprofitmuch.module.login.LoginActivity
import com.exz.carprofitmuch.service.LocationService
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_logo.*


class LogoActivity : BaseActivity() {

    private var type = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionLocationWithCheck(Intent(this, LocationService::class.java),true)
        val anim = AnimationUtils.loadAnimation(this, R.anim.logo_fade_in)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                login()
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

//                    type = 1
//                    jump(type)
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
        DataCtrlClass.loginNoDialog(PreferencesService.getAccountKey(this) ?: "", PreferencesService.getAccountValue(this) ?: "") {
            if (it != null) {
                LoginActivity.loginSuccess(this, PreferencesService.getAccountKey(this) ?: "", PreferencesService.getAccountValue(this) ?: "", User(it))
            }else{
                MyApplication.user = null
            }
            jump(type)
        }
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

