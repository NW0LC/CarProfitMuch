package com.exz.carprofitmuch.module.mine

import android.text.TextUtils
import android.view.View
import com.bigkoo.pickerview.OptionsPickerView
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_input_logistcs.*

/**
 * Created by pc on 2017/11/22.
 */

class InputLogisticsActivity : BaseActivity(), View.OnClickListener {


    lateinit var mPickerView: OptionsPickerView<String>
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

    override fun setInflateId(): Int {
        return R.layout.activity_input_logistcs
    }

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
                var logisticsName = ed_logisticsName.text.toString().trim()
                if (TextUtils.isEmpty(logisticsName)) {
                    ed_logisticsName.setShakeAnimation()
                    return
                }
                var logistics_num = ed_logistics_num.text.toString().trim()
                if (TextUtils.isEmpty(logistics_num)) {
                    ed_logistics_num.setShakeAnimation()
                    return
                }

                DataCtrlClassXZW.SubmitLogisticsCompanyData(mContext, intent.getStringExtra(OrderId), logisticsName, logistics_num, {

                    if (it != null) {
                        finish()
                    }
                })
            }
        }

    }


    companion object {
        var OrderId = "OrderId"
    }

}