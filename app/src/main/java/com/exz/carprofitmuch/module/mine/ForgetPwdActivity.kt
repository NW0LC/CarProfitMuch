package com.exz.carprofitmuch.module.mine

import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.observer.SmsContentObserver
import com.szw.framelibrary.utils.SZWUtils
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.StringUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_forget_pwd.*
import org.jetbrains.anko.toast

/**
 * Created by 史忠文
 * on 2017/10/18.
 */

class ForgetPwdActivity : BaseActivity(), View.OnClickListener {


    private lateinit var countDownTimer: CountDownTimer
    private val time = 120000//倒计时时间
    private val downKey = "F"
    lateinit var smsContentObserver: SmsContentObserver
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = getString(R.string.forget_pwd_title)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.profile))
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.blurView))
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_forget_pwd
    override fun init() {
        bt_confirm.setOnClickListener(this)


        smsContentObserver = SZWUtils.registerSMS(mContext, SZWUtils.patternCode(mContext, ed_code,4))

        val currentTime = System.currentTimeMillis()
        if (PreferencesService.getDownTimer(mContext, downKey) in 1..(currentTime - 1)) {
            downTimer(time - (currentTime - PreferencesService.getDownTimer(mContext, downKey)))
        }
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
    private fun resetTimer(b: Boolean, millisUntilFinished: Long) {
        if (b) {
            countDownTimer.cancel()
            bt_code.text = getString(R.string.login_hint_get_code)
            bt_code.isClickable = true
            bt_code.setTextColor(ContextCompat.getColor(mContext, R.color.White))
            bt_code.delegate.backgroundColor = ContextCompat.getColor(mContext, R.color.MaterialTealA700)
            PreferencesService.setDownTimer(mContext, downKey, 0)
        } else {
            bt_code.isClickable = false
            bt_code.setTextColor(ContextCompat.getColor(mContext, R.color.White))
            bt_code.delegate.backgroundColor = ContextCompat.getColor(mContext, R.color.MaterialGrey400)
            bt_code.text = String.format(getString(R.string.login_hint_get_reGetCode), millisUntilFinished / 1000)
        }

    }
    override fun onClick(p0: View?) {
        checkRegister()
    }
    private fun checkRegister() {
        when {
            TextUtils.isEmpty(ed_phone.text.toString().trim()) -> ed_phone.setShakeAnimation()
            !StringUtil.isPhone(ed_phone.text.toString()) -> {
                ed_phone.setShakeAnimation()
                toast(getString(R.string.login_error_phone))
            }
            TextUtils.isEmpty(ed_code.text.toString().trim()) -> ed_code.setShakeAnimation()
            TextUtils.isEmpty(ed_pwd.text.toString().trim()) -> {
                ed_pwd.setShakeAnimation()
                toast(getString(R.string.login_error_pwd))
            }
            TextUtils.isEmpty(ed_pwd2.text.toString().trim()) -> {
                ed_pwd.setShakeAnimation()
                toast(getString(R.string.forget_pwd_error_pwd2))
            }
            TextUtils.isEmpty(ed_pwd.text.toString().trim())!=TextUtils.isEmpty(ed_pwd2.text.toString().trim()) -> toast(getString(R.string.forget_pwd_pwd_no_equal))
            else -> DataCtrlClass.forgetPwd(mContext, ed_phone.text.toString(), ed_code.text.toString(), ed_pwd.text.toString(), ed_pwd2.text.toString()) {
                if (it != null) {
                   finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(smsContentObserver)
    }
}
