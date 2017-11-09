package com.exz.carprofitmuch.pop

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.blankj.utilcode.util.ScreenUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.module.main.pay.PwdGetCodeActivity
import com.exz.carprofitmuch.module.main.pay.PwdSetActivity
import com.szw.framelibrary.view.pwd.widget.OnPasswordInputFinish
import com.szw.framelibrary.view.pwd.widget.PasswordPopView
import razerdp.basepopup.BasePopupWindow


class PwdPop(context: Activity,inputFinish:()->OnPasswordInputFinish) : BasePopupWindow(context) {

    lateinit var mPasswordView: PasswordPopView

    init {
        mPasswordView.setInputFinish(inputFinish.invoke())
        mPasswordView.virtualKeyboardView.layoutBack.visibility = View.GONE
        mPasswordView.viewForgetPwd.setOnClickListener {
            dismiss()
            PwdSetActivity.IsSetPwd=false
            this@PwdPop.context.startActivity(Intent(this@PwdPop.context, PwdGetCodeActivity::class.java))
        }
        mPasswordView.imgCancel.setOnClickListener { dismiss() }
    }

    fun setPrice(price: String) {
        mPasswordView.viewPrice.text = price
    }


    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View {
        val inflate = View.inflate(context, R.layout.pwd_pop, null)
        mPasswordView =inflate.findViewById(R.id.mPasswordView)
        return inflate
    }

    override fun initAnimaView(): View = findViewById(R.id.mPasswordView)

    override fun initShowAnimation(): Animation {
        val shakeAnimate = TranslateAnimation(0f, 0f, ScreenUtils.getScreenHeight().toFloat(), 0f)
        shakeAnimate.duration = 300
        return shakeAnimate
    }

    override fun initExitAnimation(): Animation {
        val shakeAnimate = TranslateAnimation(0f, 0f, 0f, ScreenUtils.getScreenHeight().toFloat())
        shakeAnimate.duration = 300
        return shakeAnimate
    }

    override fun onDismiss() {
        mPasswordView.reset()
        super.onDismiss()
    }
}