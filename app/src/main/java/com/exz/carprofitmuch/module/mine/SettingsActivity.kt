package com.exz.carprofitmuch.module.mine

import android.content.Intent
import android.view.View
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.module.login.LoginActivity
import com.exz.carprofitmuch.widget.MyWebActivity
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * Created by 史忠文
 * on 2017/10/18.
 */

class SettingsActivity : BaseActivity(), View.OnClickListener {
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = getString(R.string.settings_name)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun init() {
//     bt_personInfo.setOnClickListener(this)
     bt_security.setOnClickListener(this)
     bt_aboutUs.setOnClickListener(this)
     bt_exit.setOnClickListener(this)
    }

    override fun setInflateId(): Int = R.layout.activity_settings


    override fun onClick(p0: View?) {
        when (p0) {
            bt_security-> {
                startActivity(Intent(this,AccountSafeActivity::class.java))
            }
            bt_aboutUs-> {
                val intent = Intent(this, MyWebActivity::class.java)
//            intent.putExtra(MyWebActivity.Intent_Title, response.body().info[it].title)
//            intent.putExtra(MyWebActivity.Intent_Url, response.body().info[it].url)
                startActivity(intent)
            }
            bt_exit-> {
                PreferencesService.saveAutoLoginToken(this,"")
                MyApplication.user=null
                setResult(LoginActivity.RESULT_LOGIN_CANCELED)
                onBackPressed()
            }
            else -> {
            }
        }
    }

}