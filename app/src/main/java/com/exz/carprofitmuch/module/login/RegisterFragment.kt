package com.exz.carprofitmuch.module.login

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.User
import com.exz.carprofitmuch.config.Urls.UserAgreement
import com.exz.carprofitmuch.widget.CustomViewpager
import com.exz.carprofitmuch.widget.MyWebActivity
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Title
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Url
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.observer.SmsContentObserver
import com.szw.framelibrary.utils.SZWUtils
import com.szw.framelibrary.utils.StringUtil
import com.szw.framelibrary.view.ClearWriteEditText
import kotlinx.android.synthetic.main.fragment_register.*
import org.jetbrains.anko.support.v4.toast


/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class RegisterFragment : MyBaseFragment(), View.OnFocusChangeListener, TextWatcher, View.OnClickListener {

    lateinit var viewpager: CustomViewpager
    private var countDownTimer: CountDownTimer?=null
    private val time = 120000//倒计时时间
    private val downKey = "R"
    lateinit var smsContentObserver: SmsContentObserver

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_register, container, false)
        viewpager.setObjectForPosition(rootView, 1)
        return rootView
    }

    override fun initView() {
        ed_phone.onFocusChangeListener = this
        ed_pwd.onFocusChangeListener = this
        ed_code.onFocusChangeListener = this
        ed_referrer.onFocusChangeListener = this
        ed_phone.addTextChangedListener(this)
        ed_pwd.addTextChangedListener(this)
        ed_code.addTextChangedListener(this)
        ed_referrer.addTextChangedListener(this)
        bt_register.setOnClickListener(this)
        bt_code.setOnClickListener(this)
        bt_protocol.setOnClickListener(this)

        smsContentObserver = SZWUtils.registerSMS(context!!, SZWUtils.patternCode(context!!, ed_code,4))

        val currentTime = System.currentTimeMillis()
        if (PreferencesService.getDownTimer(context!!, downKey) in 1..(currentTime - 1)) {
            downTimer(time - (currentTime - PreferencesService.getDownTimer(context!!, downKey)))
        }
    }

    private fun getSecurityCode() {
        if (TextUtils.isEmpty(ed_phone.text.toString().trim()) || !StringUtil.isPhone(ed_phone.text.toString())) {
            ed_phone.setShakeAnimation()
        } else {
            downTimer(time.toLong())
            PreferencesService.setDownTimer(context!!, downKey, System.currentTimeMillis())
            DataCtrlClass.getSecurityCode(context, ed_phone.text.toString(), "1") {
                if (it != null) {
                    ed_code.setText(it)
                } else {
                    resetTimer(true, java.lang.Long.MIN_VALUE)
                }
            }
        }
    }

    private fun checkRegister() {
        if (TextUtils.isEmpty(ed_phone.text.toString().trim())) {
            ed_phone.setShakeAnimation()
        } else if (!StringUtil.isPhone(ed_phone.text.toString())) {
            ed_phone.setShakeAnimation()
            toast(getString(R.string.login_error_phone))
        } else if (TextUtils.isEmpty(ed_code.text.toString().trim())) {
            ed_code.setShakeAnimation()
        } else if (TextUtils.isEmpty(ed_weChat.text.toString().trim())) {
            ed_code.setShakeAnimation()
        } else if (TextUtils.isEmpty(ed_pwd.text.toString().trim())) {
            ed_pwd.setShakeAnimation()
            toast(getString(R.string.login_error_pwd))
        } else{
            DataCtrlClass.register(context, ed_phone.text.toString(), ed_code.text.toString(), ed_pwd.text.toString(), ed_weChat.text.toString(), ed_referrer.text.toString()) {
                if (it != null){
                    ed_phone.postDelayed({
                        LoginActivity.loginSuccess(activity, ed_phone.text.toString(), ed_pwd.text.toString(), User(it))
                    },500)
                }
            }
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
        countDownTimer?.start()
    }

    private fun resetTimer(b: Boolean, millisUntilFinished: Long) {
        if (b) {
            countDownTimer?.cancel()
            bt_code.text = getString(R.string.login_hint_get_code)
            bt_code.isClickable = true
            bt_code.setTextColor(ContextCompat.getColor(context!!, R.color.MaterialTealA700))
            bt_code.delegate.strokeColor = ContextCompat.getColor(context!!, R.color.MaterialTealA700)
            PreferencesService.setDownTimer(context!!, downKey, 0)
        } else {
            bt_code.isClickable = false
            bt_code.setTextColor(ContextCompat.getColor(context!!, R.color.MaterialGrey400))
            bt_code.delegate.strokeColor = ContextCompat.getColor(context!!, R.color.MaterialGrey400)
            bt_code.text = String.format(getString(R.string.login_hint_get_reGetCode), millisUntilFinished / 1000)
        }

    }

    override fun onDetach() {
        super.onDetach()
        countDownTimer?.cancel()
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when {
            ed_phone.hasFocus() -> {
                if (ed_pwd.text.toString().isNotEmpty())
                    ed_pwd.setText("")
                ed_phone.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        if (StringUtil.isPhone(ed_phone.text.toString())) ContextCompat.getDrawable(context!!, R.mipmap.icon_login_phone_on)
                        else ContextCompat.getDrawable(context!!, R.mipmap.icon_login_phone_off), null, null, null)
            }
            ed_pwd.hasFocus() -> {
                ed_pwd.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        if (p0.toString().isNotEmpty() && p0?.length ?: 0 >= 6) ContextCompat.getDrawable(context!!, R.mipmap.icon_login_pwd_on)
                        else ContextCompat.getDrawable(context!!, R.mipmap.icon_login_pwd_off), null, null, null)
            }
            ed_code.hasFocus() -> {
                ed_code.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        if (p0.toString().isNotEmpty() && p0?.length ?: 0 >= 4) ContextCompat.getDrawable(context!!, R.mipmap.icon_login_pwd_on)
                        else ContextCompat.getDrawable(context!!, R.mipmap.icon_login_pwd_off), null, null, null)
            }
            ed_referrer.hasFocus() -> {
                ed_referrer.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        if (StringUtil.isPhone(ed_referrer.text.toString())) ContextCompat.getDrawable(context!!, R.mipmap.icon_login_invite_on)
                        else ContextCompat.getDrawable(context!!, R.mipmap.icon_login_invite_off), null, null, null)
            }
        }


    }

    override fun onClick(p0: View?) {
        when (p0) {
            bt_register -> {//注册
                checkRegister()
            }
            bt_code -> {//获取验证码
                getSecurityCode()
            }
            bt_protocol -> {
                //协议
                val intent = Intent(context, MyWebActivity::class.java)
                intent.putExtra(Intent_Url, UserAgreement)
                intent.putExtra(Intent_Title, getString(R.string.user_protocol))
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    override fun onFocusChange(p0: View?, p1: Boolean) {
        if (p1) {
            (p0 as ClearWriteEditText).setClearIconVisible(p0.text.isNotEmpty())
        } else {
            (p0 as ClearWriteEditText).setClearIconVisible(false)
        }
        if (p0.id == R.id.ed_code)
            lay_code.setBackgroundResource(if (p1) R.mipmap.icon_login_edit_text_white_bg else R.mipmap.icon_login_edit_text_gray_bg)
        else
            p0.setBackgroundResource(if (p1) R.mipmap.icon_login_edit_text_white_bg else R.mipmap.icon_login_edit_text_gray_bg)
    }

    companion object {
        fun newInstance(viewpager: CustomViewpager): RegisterFragment {

            val bundle = Bundle()
            val fragment = RegisterFragment()
            fragment.viewpager = viewpager
            fragment.arguments = bundle
            return fragment
        }
    }
}