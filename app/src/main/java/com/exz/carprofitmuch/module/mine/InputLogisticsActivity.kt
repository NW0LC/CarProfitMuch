package com.exz.carprofitmuch.module.mine

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
    var returnGoodsType = ArrayList<String>()
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

        returnGoodsType.add("退货")
        returnGoodsType.add("退款")
        mPickerView = OptionsPickerView(mContext)
        mPickerView.setPicker(returnGoodsType)
        mPickerView.setCyclic(false)
        mPickerView.setOnoptionsSelectListener(object : OptionsPickerView.OnOptionsSelectListener {
            override fun onOptionsSelect(options1: Int, option2: Int, options3: Int) {
                tv_refund_type.setText(returnGoodsType.get(options1))
            }
        })
        tv_refund_type.setOnClickListener(this)
        tv_submit.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_refund_type -> {
                mPickerView.show()
            }
            R.id.tv_submit -> {
                DataCtrlClassXZW.SubmitLogisticsCompanyData(mContext, "", "", "", {

                    if (it != null) {
                        finish()
                    }
                })
            }
        }
    }
}
