package com.exz.carprofitmuch.module.main.pay


import android.app.Activity
import android.content.Intent
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.blankj.utilcode.util.EncryptUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.config.Urls.PayPwdCodeVerify
import com.exz.carprofitmuch.utils.SZWUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.observer.SmsContentObserver
import com.szw.framelibrary.utils.StringUtil
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.pwd_activity_get_code.*
import org.jetbrains.anko.toast

class PwdGetCodeActivity : BaseActivity(), View.OnClickListener {


    private lateinit var countDownTimer: CountDownTimer
    private val time = 120000//倒计时时间
    private val downKey = "R"
    private var phoneNum =""
    private lateinit var smsContentObserver: SmsContentObserver

    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        mTitle.textSize = 16f
        mTitle.text = "验证手机"
        return true
    }

    override fun init() {
        super.init()
        phoneNum=PreferencesService.getAccountKey(this)?:""
        ed_phone.setText(SZWUtils.hideMidPhone(phoneNum))
        val l = System.currentTimeMillis()
        if (PreferencesService.getDownTimer(this, downKey) in 1..(l - 1)) {
            downTimer(time - (l - PreferencesService.getDownTimer(this, downKey)))
        }
        // 先判断是否有权限。
        PermissionSMSWithCheck(null, false)
        // 注册读取短信Observer
        smsContentObserver = com.szw.framelibrary.utils.SZWUtils.registerSMS(mContext, com.szw.framelibrary.utils.SZWUtils.patternCode(mContext, get_code,4))
        toolbar.setNavigationOnClickListener({finish()})
        get_code.setOnClickListener(this)
        bt_next.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 解除注册读取短信Observer
        contentResolver.unregisterContentObserver(smsContentObserver)

    }

    private fun downTimer(l: Long) {
        countDownTimer = object : CountDownTimer(l, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resetTimer(false, millisUntilFinished)
            }

            override fun onFinish() {
                resetTimer(true, java.lang.Long.MIN_VALUE)
            }
        }
        countDownTimer.start()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.mLeftImg -> finish()
            R.id.get_code -> getSecurityCode()
            R.id.bt_next -> check()
        }
    }

    private fun resetTimer(b: Boolean, millisUntilFinished: Long) {
        if (b) {
            countDownTimer.cancel()
            get_code.text = getString(R.string.login_hint_get_code)
            get_code.isClickable = true
            get_code.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
            get_code.delegate.strokeColor = ContextCompat.getColor(mContext, R.color.colorPrimary)
            PreferencesService.setDownTimer(mContext, downKey, 0)
        } else {
            get_code.isClickable = false
            get_code.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey400))
            get_code.delegate.strokeColor = ContextCompat.getColor(mContext, R.color.MaterialGrey400)
            get_code.text = String.format(getString(R.string.login_hint_get_reGetCode), millisUntilFinished / 1000)
        }

    }

    override fun setInflateId(): Int = R.layout.pwd_activity_get_code


    private fun check() {
        if (TextUtils.isEmpty(ed_phone.text.toString().trim { it <= ' ' })) {
            //            phone.setShakeAnimation();
        } else if (!StringUtil.isPhone(phoneNum)) {
            //            phone.setShakeAnimation();
            Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(ed_code.text.toString().trim { it <= ' ' })) {
            ed_code.setShakeAnimation()
        } else {
            checkCode()

        }
    }

    /**
     * 验证验证码
     */
    private fun checkCode() {
//        phone	string	必填	手机号
//                code	string	必填	短信验证码
//                requestCheck	string	必填	验证请求

        val map = HashMap<String, String>()
        map.put("phone", phoneNum)
        map.put("code", ed_code.text.toString().trim { it <= ' ' })
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(phoneNum+ed_code.text.toString().trim { it <= ' ' }, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(PayPwdCodeVerify).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<String>>(this) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            if (response.body().data=="1") {
                                val intent = Intent(mContext, PwdSetActivity::class.java)
                                intent.putExtra(PwdSetActivity.PwdSetActivity_Type, PwdSetActivity.PwdSetActivity_Type_2)
                                intent.putExtra(PwdSetActivity.PwdSetActivity_Security_Code, ed_code.text.toString().trim { it <= ' ' })
                                startActivityForResult(intent, 100)
                            }else{
                                toast(response.body().message)
                            }
                        }

                    }
                })

    }

    private fun getSecurityCode() {
        if (TextUtils.isEmpty(ed_phone.text.toString().trim { it <= ' ' }) || !StringUtil.isPhone(phoneNum)) {
            //            phone.setShakeAnimation();
        } else {
            downTimer(time.toLong())
            PreferencesService.setDownTimer(this, downKey, System.currentTimeMillis())
            DataCtrlClass.getSecurityCode(this,phoneNum, "3"){
                if (it != null) {
                    ed_code.setText(it)
                } else {
                    resetTimer(true, java.lang.Long.MIN_VALUE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            finish()
        }
    }
}