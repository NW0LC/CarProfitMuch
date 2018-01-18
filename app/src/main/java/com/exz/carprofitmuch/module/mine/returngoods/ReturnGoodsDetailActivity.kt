package com.exz.carprofitmuch.module.mine.returngoods

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ReturnGoodsAdapter
import com.exz.carprofitmuch.adapter.ReturnGoodsAdapter.Companion.initStateBtn
import com.exz.carprofitmuch.bean.ReturnGoodsBean
import com.exz.carprofitmuch.config.Urls
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_retunr_goods_detail.*
import kotlinx.android.synthetic.main.lay_goods_order_bt.*

/**
 * Created by pc on 2017/11/20.
 * 退款退货订单详情
 */

class ReturnGoodsDetailActivity : BaseActivity(), View.OnClickListener {
    lateinit var data: ReturnGoodsBean
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        mTitle.text = mContext.getString(R.string.mine_return_goods_detail)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        toolbar.setNavigationOnClickListener { finish() }
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_retunr_goods_detail

    override fun init() {
        data = intent.getSerializableExtra(ReturnGoodsDetail_Intent_orderData) as ReturnGoodsBean
        initView()
        initEvent()
        myWeb.loadUrl(Urls.url + "/Mobile/OrderDetail.aspx?id=" + data.returnOrderId)
    }


    private fun initView() {
        initStateBtn(data.returnOrderState, data.returnOrderSubState, data.returnOrderType, TextView(this), tv_mid, tv_right)
    }

    private fun initEvent() {
        tv_left.setOnClickListener(this)
        tv_mid.setOnClickListener(this)
        tv_right.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        ReturnGoodsAdapter.setOnClick(this, v.id, data) {
            setResult(Activity.RESULT_OK)
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==RESULT_OK){
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    companion object {
        var ReturnGoodsDetail_Intent_orderData = "ReturnGoodsDetail_Intent_orderData"
    }

}
