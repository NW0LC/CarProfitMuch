package com.exz.carprofitmuch.module.login

import android.content.Intent
import android.os.Bundle
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
import com.exz.carprofitmuch.module.mine.ForgetPwdActivity
import com.exz.carprofitmuch.widget.CustomViewpager
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.utils.StringUtil
import com.szw.framelibrary.view.ClearWriteEditText
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class LoginFragment : MyBaseFragment(), View.OnFocusChangeListener, View.OnClickListener, TextWatcher {

    lateinit var viewpager: CustomViewpager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_login, container, false)
        viewpager.setObjectForPosition(rootView, 0)
        return rootView
    }

    override fun initView() {
        bt_login.isFocusable = true
        bt_login.requestFocus()
        ed_phone.setText(PreferencesService.getAccountKey(context))
        ed_pwd.setText(PreferencesService.getAccountValue(context))
        ed_phone.onFocusChangeListener = this
        ed_pwd.onFocusChangeListener = this
        ed_phone.addTextChangedListener(this)
        ed_pwd.addTextChangedListener(this)
        bt_forgetPwd.setOnClickListener(this)
        bt_login.setOnClickListener(this)

        (activity as BaseActivity).PermissionSMSWithCheck(null, false)
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when {
            ed_phone.hasFocus() -> {
                if (ed_pwd.text.toString().isNotEmpty()) {
                    ed_pwd.setText("")
                    ed_pwd.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, R.mipmap.icon_login_pwd_off), null, null, null)
                }
                ed_phone.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        if (StringUtil.isPhone(ed_phone.text.toString())) ContextCompat.getDrawable(context, R.mipmap.icon_login_phone_on)
                        else ContextCompat.getDrawable(context, R.mipmap.icon_login_phone_off), null, null, null)
            }
            ed_pwd.hasFocus() -> {
                ed_pwd.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        if (p0.toString().isNotEmpty() && p0?.length ?: 0 >= 6) ContextCompat.getDrawable(context, R.mipmap.icon_login_pwd_on)
                        else ContextCompat.getDrawable(context, R.mipmap.icon_login_pwd_off), null, null, null)
            }
        }


    }

    override fun onClick(p0: View?) {
        when (p0) {
            bt_login -> {
                checkLogin()
            }
            bt_forgetPwd -> {
                //忘记密码
                startActivity(Intent(context, ForgetPwdActivity::class.java))
            }
            else -> {
            }
        }
    }

    override fun onFocusChange(p0: View, p1: Boolean) {
        if (p1) {
            (p0 as ClearWriteEditText).setClearIconVisible(p0.text.isNotEmpty())
        } else {
            (p0 as ClearWriteEditText).setClearIconVisible(false)
        }
        p0.setBackgroundResource(if (p1) R.mipmap.icon_login_edit_text_white_bg else R.mipmap.icon_login_edit_text_gray_bg)
    }

    private fun checkLogin() {
        if (TextUtils.isEmpty(ed_phone.text.toString().trim())) {
            ed_phone.setShakeAnimation()
        } else if (!StringUtil.isPhone(ed_phone.text.toString())) {
            ed_phone.setShakeAnimation()
            toast(getString(R.string.login_error_phone))
        } else if (TextUtils.isEmpty(ed_pwd.text.toString())) {
            ed_pwd.setShakeAnimation()
            toast(getString(R.string.login_error_pwd))
        } else {
            DataCtrlClass.login(context, ed_phone.text.toString(), ed_pwd.text.toString()) {
                if (it != null) {
                    ed_phone.postDelayed({
                        LoginActivity.loginSuccess(activity, ed_phone.text.toString(), ed_pwd.text.toString(), User(it))
                    }, 500)
                }

            }
        }
    }

    companion object {
        fun newInstance(viewpager: CustomViewpager): LoginFragment {
            val bundle = Bundle()
            val fragment = LoginFragment()
            fragment.viewpager = viewpager
            fragment.arguments = bundle
            return fragment
        }
    }
}
