package com.exz.carprofitmuch.module.mine

import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_score_order_detail.*
import kotlinx.android.synthetic.main.layout_address.*
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout


/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ScoreOrderDetailActivity : BaseActivity(), View.OnClickListener {

    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.score_confirm_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun setInflateId(): Int= R.layout.activity_score_order_detail

    override fun init() {
        val scoreConfirmAddressDetail = getString(R.string.score_confirm_address_detail)
        val msp = SpannableString(scoreConfirmAddressDetail+"更换当前号码将从手机发送一条普通短信进行验证")
        msp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.MaterialGrey700)), 0, scoreConfirmAddressDetail.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_address.text = msp



        initEvent()
        setData()
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        bt_service.setOnClickListener(this)
    }
    private fun setData() {
        DataCtrlClass.scoreOrderDetailData(mContext, intent.getStringExtra(Intent_Score_Order_Id)?:""){
            lay_time.addView(with(lay_time.context){
                verticalLayout{
                    textView ( "${getString(R.string.goods_order_orderNum)}${it?.goodsNum}"){
                        textSize = 14f
                        setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey400))
                    }
                    for (dateBean in it?.dates?:ArrayList()) {
                        textView ( "${dateBean.key}:${dateBean.value}"){
                            textSize = 14f
                            setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey400))
                        }
                    }
                }
            })
        }
    }
    override fun onClick(p0: View?) {
        DialogUtils.Call(this@ScoreOrderDetailActivity,"13810711666")
    }
    companion object {
        val Intent_Score_Order_Id = "Intent_Score_Order_Id"
    }
}