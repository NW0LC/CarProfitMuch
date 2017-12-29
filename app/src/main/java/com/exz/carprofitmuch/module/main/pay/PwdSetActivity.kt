package com.exz.carprofitmuch.module.main.pay

import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.blankj.utilcode.util.EncryptUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.config.Urls
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import com.szw.framelibrary.view.pwd.widget.OnPasswordInputFinish
import com.szw.framelibrary.view.pwd.widget.PasswordView
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.pwd_activity_set.*
import org.jetbrains.anko.toast

/**
 * Created by 史忠文
 * on 2017/5/4.
 */

class PwdSetActivity : BaseActivity(), OnPasswordInputFinish {
    var type = ""
    var phoneNum = ""
    private var oldPwd: String = ""
    private var newPwd: String = ""
    private var url: String = ""
    lateinit var mPasswordView: PasswordView
    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        mTitle.textSize = 16f
        mTitle.text = ""
        toolbar.setNavigationOnClickListener { finish() }
        return true
    }

    override fun setInflateId(): Int = R.layout.pwd_activity_set

    override fun init() {
        super.init()
        phoneNum = PreferencesService.getAccountKey(this) ?: ""
        type = if (TextUtils.isEmpty(type)) intent.getStringExtra(PwdSetActivity_Type) else type
        mPasswordView = findViewById(R.id.mPasswordView)
        mPasswordView.virtualKeyboardView.layoutBack.visibility = View.GONE
        mPasswordView.setInputFinish(this)
        passReset()
    }

    private fun passReset() {
        when (type) {
            PwdSetActivity_Type_1 -> {
                info.text = "输入支付密码，以验证身份"
                url = Urls.PayPwdVerify
            }
            PwdSetActivity_Type_2 -> {
                info.text = "输入新密码"
            }
            PwdSetActivity_Type_3 -> {
                info.text = "重新输入新密码"
                url = Urls.ModifyPayPwd
            }
        }

        mPasswordView.reset()
    }

    override fun inputFinish(password: String) {
        when (type) {
            PwdSetActivity_Type_1 -> setPayPwd(password, "", "")
            PwdSetActivity_Type_2 -> if (TextUtils.isEmpty(intent.getStringExtra(PwdSetActivity_Security_Code))) {
                newPwd = password
                type = PwdSetActivity_Type_3
                passReset()
            } else {
                url = Urls.SetPayPwd
                setPayPwd(password, "", "")
            }
            PwdSetActivity_Type_3 -> if (newPwd == password) {
                setPayPwd("", oldPwd, newPwd)
            } else {
                Toast.makeText(mContext, "两次输入密码不一致", Toast.LENGTH_SHORT).show()
                newPwd = ""
                type = PwdSetActivity_Type_2
                passReset()
            }
        }

    }

    /**
     * 设置支付密码
     */
    private fun setPayPwd(payPwd: String, oldPayPwd: String, newPayPwd: String) {
        val map = HashMap<String, String>()
        map.put("userId", MyApplication.loginUserId)
        if (!TextUtils.isEmpty(payPwd)) {
            if (TextUtils.isEmpty(intent.getStringExtra(PwdSetActivity_Security_Code))) {
                //验证身份
                map.put("payPwd", payPwd)
                map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + payPwd, salt).toLowerCase())
            } else {//设置密码
                map.put("payPwd", payPwd)
                map.put("code", intent.getStringExtra(PwdSetActivity_Security_Code))
                map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId  + intent.getStringExtra(PwdSetActivity_Security_Code)+ payPwd, salt).toLowerCase())
            }

        } else {//重置密码
            map.put("oldPayPwd", oldPayPwd)
            map.put("newPayPwd", newPayPwd)
            map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + oldPayPwd + newPayPwd, salt).toLowerCase())
        }
        OkGo.post<NetEntity<String>>(url).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<String>>(this) {

                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        when (type) {
                            PwdSetActivity_Type_1 -> {
                                if (response.body().getCode() == Constants.NetCode.SUCCESS&&response.body().data=="1") {
                                    oldPwd = payPwd
                                    type = PwdSetActivity_Type_2
                                } else {
                                    Toast.makeText(mContext, "支付密码错误请重试", Toast.LENGTH_SHORT).show()
                                }
                                init()
                            }
                            PwdSetActivity_Type_2, PwdSetActivity_Type_3 -> {
                                Toast.makeText(mContext, response.body().message, Toast.LENGTH_SHORT).show()
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        toast(response.exception.message.toString())
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                })

    }

    companion object {
        val PwdSetActivity_Type = "PwdSetActivity_Type"
        val PwdSetActivity_Type_1 = "PwdSetActivity_Type_1"//原密码
        val PwdSetActivity_Type_2 = "PwdSetActivity_Type_2"//新密码
        val PwdSetActivity_Type_3 = "PwdSetActivity_Type_3"//确认密码
        val PwdSetActivity_Security_Code = "PwdSetActivity_Security_Code"//验证码
    }


}
