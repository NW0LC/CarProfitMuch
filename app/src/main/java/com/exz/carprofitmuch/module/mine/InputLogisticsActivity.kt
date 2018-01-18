package com.exz.carprofitmuch.module.mine

import android.app.Activity
import android.text.TextUtils
import android.view.View
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_input_logistcs.*

class InputLogisticsActivity : BaseActivity(), View.OnClickListener {
    override fun initToolbar(): Boolean {
        mTitle.text = mContext.getString(R.string.mine_input_logistics)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_input_logistcs

    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {

        tv_submit.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_submit -> {
                val logisticsName = ed_logisticsName.text.toString().trim()
                if (TextUtils.isEmpty(logisticsName)) {
                    ed_logisticsName.setShakeAnimation()
                    return
                }
                val logisticsNum = ed_logistics_num.text.toString().trim()
                if (TextUtils.isEmpty(logisticsNum)) {
                    ed_logistics_num.setShakeAnimation()
                    return
                }

                DataCtrlClassXZW.submitLogisticsCompanyData(mContext, intent.getStringExtra(InputLogistics_Intent_OrderId), logisticsName, logisticsNum, {
                    if (it != null) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                })
            }
        }

    }


    companion object {
        var InputLogistics_Intent_OrderId = "InputLogistics_Intent_OrderId"
    }

}