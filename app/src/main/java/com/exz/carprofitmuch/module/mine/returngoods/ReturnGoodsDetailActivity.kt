package com.exz.carprofitmuch.module.mine.returngoods

import android.content.Intent
import android.view.View
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.module.mine.InputLogisticsActivity
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_retunr_goods_detail.*
import kotlinx.android.synthetic.main.lay_goods_order_bt.*

/**
 * Created by pc on 2017/11/20.
 * 退款退货订单详情
 */

class ReturnGoodsDetailActivity : BaseActivity(), View.OnClickListener {
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
        super.init()
        initView()
        initEvent()
        myWeb.loadUrl(Urls.url+"/Mobile/OrderDetail.aspx?id="+intent.getStringExtra(ReturnGoodsDetail_Intent_orderId))
    }


    private fun initView() {
        tv_left.visibility = View.VISIBLE
        when (intent.getStringExtra(ReturnGoodsDetail_Intent_state)?:"") {
            "1" -> {//待处理
                tv_left.text = "联系卖家"
                tv_mid.text = "平台申诉"
                tv_right.text = "取消退货"
            }
            "2" -> {//处理中 卖家已同意申请
                tv_left.text = "联系卖家"
                tv_mid.text = "填写物流"
                tv_right.text = "取消退货"
            }
            "3" -> {//处理中 卖家拒绝申请
                tv_left.text = "联系卖家"
                tv_mid.text = "平台申诉"
                tv_right.text = "取消退货"
            }
            "4" -> {//处理中 买家已发物流
                tv_left.text = "联系卖家"
                tv_mid.text = "填写物流"
                tv_right.text = "取消退货"
            }
            "5" -> {//处理中 买家已发物流 卖家拒绝退货
                tv_mid.visibility = View.GONE
                tv_left.text = "联系卖家"
                tv_right.text = "取消退货"
            }
            "6" -> {//已完成
                llLay.visibility = View.GONE
            }
        }


    }

    private fun initEvent() {
        tv_left.setOnClickListener(this)
        tv_mid.setOnClickListener(this)
        tv_right.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_mid -> {
                when (intent.getStringExtra(ReturnGoodsDetail_Intent_state)?:"") {
                    "1" -> {//联系商家
                        DialogUtils.Call(this, intent.getStringExtra(ReturnGoodsDetail_Intent_phone)?:"")
                    }

                    "2"->{

                        when (intent.getStringExtra(ReturnGoodsDetail_Intent_returnOrderSubState)?:"") {
                            "1","5" -> {// 平台申诉
                            }
                            "2" ,"4","6","7"-> {//联系商家
                                DialogUtils.Call(this, intent.getStringExtra(ReturnGoodsDetail_Intent_phone)?:"")
                            }
                            "3"->{//填写物流
                                startActivity(Intent(this, InputLogisticsActivity::class.java).putExtra(InputLogisticsActivity.InputLogistics_Intent_OrderId,intent.getStringExtra(ReturnGoodsDetail_Intent_orderId)?:""))
                            }

                        }

                    }
                    "3" -> {   //联系商家

                        DialogUtils.Call(this, intent.getStringExtra(ReturnGoodsDetail_Intent_phone)?:"")
                    }

                }
            }
            R.id.tv_right -> {
                when (intent.getStringExtra(ReturnGoodsDetail_Intent_state)?:"") {
                    "1","2"  -> {//取消退货
                        com.exz.carprofitmuch.utils.DialogUtils.cancel(this){
                            DataCtrlClassXZW.returnEditOrderData(this, intent.getStringExtra(ReturnGoodsDetail_Intent_orderId)?:"","0", {
                                if (it != null) {
                                    finish()
                                }
                            })
                        }

                    }
                    "3" -> {    //删除订单
                        com.exz.carprofitmuch.utils.DialogUtils.delete(this){
                            DataCtrlClassXZW.returnEditOrderData(this, intent.getStringExtra(ReturnGoodsDetail_Intent_orderId)?:"","1", {
                                if (it != null) {
                                    finish()
                                }
                            })
                        }
                    }

                }
            }




    }

    }
    companion object {
        var ReturnGoodsDetail_Intent_state="ReturnGoodsDetail_Intent_state"
        var ReturnGoodsDetail_Intent_returnOrderSubState="ReturnGoodsDetail_Intent_returnOrderSubState"
        var ReturnGoodsDetail_Intent_phone="ReturnGoodsDetail_Intent_phone"
        var ReturnGoodsDetail_Intent_orderId="ReturnGoodsDetail_Intent_orderId"
    }

}
