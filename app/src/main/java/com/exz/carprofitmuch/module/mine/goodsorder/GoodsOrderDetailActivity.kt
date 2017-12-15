package com.exz.carprofitmuch.module.mine.goodsorder

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsOrderAdapter
import com.exz.carprofitmuch.adapter.ItemGoodsOrderAdapter
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.GoodsOrderDetailEntity
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Intent_Finish_Type_2
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Pay_Intent_Finish_Type
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Pay_Intent_OrderId
import com.exz.carprofitmuch.module.mine.GoodsOrderCommentActivity
import com.exz.carprofitmuch.module.mine.goodsorder.RefundActivity.Companion.Refund_Intent_OrderId
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.widget.MyWebActivity
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_my_order_detail.*
import kotlinx.android.synthetic.main.lay_goods_detail_text.view.*
import kotlinx.android.synthetic.main.lay_goods_order_bt.*
import kotlinx.android.synthetic.main.layout_address.*

/**
 * Created by pc on 2017/11/15.
 * 订单详情
 */

class GoodsOrderDetailActivity : BaseActivity(), View.OnClickListener {

    lateinit var adapter: ItemGoodsOrderAdapter<GoodsBean>
    lateinit var entity: GoodsOrderDetailEntity
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.mine_my_order_detail)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        toolbar.setNavigationOnClickListener { finish() }
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_my_order_detail

    override fun init() {
        tv_next.visibility = View.GONE
        initView()
        iniData()
        initClick()
    }

    private fun initClick() {
        tv_left.setOnClickListener(this)
        tv_mid.setOnClickListener(this)
        tv_right.setOnClickListener(this)

    }

    private fun initView() {
        adapter = ItemGoodsOrderAdapter()
        adapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 15, ContextCompat.getColor(mContext, R.color.app_bg)))
    }


    private fun iniData() {
        DataCtrlClassXZW.orderDetailData(mContext, orderId) {
            if (it != null) {
                entity = it
                tv_userName.text = it.addressInfo?.name
                tv_userPhone.text = it.addressInfo?.phone
                val scoreConfirmAddressDetail = getString(R.string.score_confirm_address_detail)
                val msp = SpannableString(scoreConfirmAddressDetail + it.addressInfo.toString())
                msp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.MaterialGrey700)), 0, scoreConfirmAddressDetail.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_address.text = msp
                tv_shop_name.text = it.shopName
                tv_msg.visibility = if (it.buyerMessage?.isEmpty()==true) View.GONE else View.VISIBLE
                tv_msg.text = String.format(getString(R.string.goods_confirm_msg) + "%s", it.buyerMessage)
                tv_order_type.text = GoodsOrderAdapter.getState(it.orderState ?: "")
                ll_lay1.removeAllViews()
                for (info in it.moneyInfo ?: ArrayList()) {
                    val lay1 = LayoutInflater.from(mContext).inflate(R.layout.lay_goods_detail_text, RelativeLayout(mContext), false)
                    lay1.tv_key.width = 1
                    ll_lay1.addView(lay1)
                    lay1.tv_key.text = info.key
                    if (it.payMark=="1"){
                        lay1.tv_value.text=String.format("%s${mContext.getString(R.string.SCORE)}",info.value)
                    }else{
                        lay1.tv_value.text=String.format(mContext.getString(R.string.CNY)+"%s",info.value)
                    }
                }
                ll_lay2.removeAllViews()
                val numberLay = LayoutInflater.from(mContext).inflate(R.layout.lay_goods_detail_text, RelativeLayout(mContext), false)
                numberLay.tv_key.text=String.format(getString(R.string.goods_order_orderNum),it.orderNum)
                ll_lay2.addView(numberLay)
                for (info in it.dateInfo ?: ArrayList()) {
                    val lay2 = LayoutInflater.from(mContext).inflate(R.layout.lay_goods_detail_text, RelativeLayout(mContext), false)
                    ll_lay2.addView(lay2)
                    lay2.tv_key.text = String.format(info.key+"：%s",info.value)
                }
                if (it.payMark=="1"){
                    tv_goods_total.text =String.format("%s${mContext.getString(R.string.SCORE)}",it.actualMoney)
                }else{
                    tv_goods_total.text = String.format(mContext.getString(R.string.CNY) + "%s", it.actualMoney)
                }

                adapter.setNewData(it.goodsInfo)
                adapter.loadMoreEnd()
                GoodsOrderAdapter.initStateBtn(it.orderState ?: "", false,tv_order_type, tv_left, tv_mid, tv_right)
            }
        }
    }

    override fun onClick(v: View) {
        /**         btLeft        btMid     btRight
         * 1待付款 【联系商家   取消订单   支付订单】
         * 2待发货 【联系商家              申请退款】
         * 3待收货 【申请退货   查看物流   确认收货】
         * 4待评价 【联系商家              评价订单】
         * 5已结束 【联系商家              删除订单】
         * 6已取消 【                      删除订单】
         * 其他
         */
        when (v.id) {
            R.id.tv_left -> {
                if (entity.orderState=="3") {
                    //申请退货
                    startActivityForResult(Intent(this,RefundActivity::class.java).putExtra(Refund_Intent_OrderId,entity.orderId),100)
                }else
                    com.szw.framelibrary.utils.DialogUtils.Call(this, entity.shopPhone ?: "")
            }
            R.id.tv_mid -> {
                when (entity.orderState) {
                    "1" -> {//取消订单
                        DataCtrlClassXZW.editOrderData(this, entity.orderId ?: "", "0", {
                            if (it != null) {
                                iniData()
                            }
                        })
                    }
                    "3" -> {//查看物流
                        startActivity(Intent(this, MyWebActivity::class.java).putExtra(MyWebActivity.Intent_Url, "http://m.kuaidi100.com/result.jsp?nu=" + entity.logisticsNum).putExtra(MyWebActivity.Intent_Title, "查看物流"))
                    }
                }


            }
            R.id.tv_right -> {
                when (entity.orderState) {
                    "1" -> {//支付订单
                        startActivityForResult(Intent(this, PayMethodsActivity::class.java).putExtra(Pay_Intent_OrderId, entity.orderId).putExtra(Pay_Intent_Finish_Type, Intent_Finish_Type_2), 100)
                    }
                    "2" -> {//申请退款
                        com.exz.carprofitmuch.utils.DialogUtils.refund(this, entity.orderId ?: "", {
                            DataCtrlClassXZW.applyReturnMoney(this, entity.orderId ?: "", it, {
                                if (it != null) {
                                    iniData()
                                }
                            })

                        })

                    }
                    "3" -> {//确认收货
                        DataCtrlClassXZW.editOrderData(this, entity.orderId?:"", "2", {
                            if (it != null) {
                                iniData()
                            }
                        })
                    }
                    "4" -> {    //评价订单
                        GoodsOrderCommentActivity.shopId = entity.shopId ?: ""
                        GoodsOrderCommentActivity.orderId = entity.orderId ?: ""
                        startActivityForResult(Intent(this, GoodsOrderCommentActivity::class.java), 100)

                    }
                    "5", "6" -> {    //删除订单
                        DataCtrlClassXZW.editOrderData(this, entity.orderId ?: "", "1", {
                            if (it != null) {
                                finish()
                            }
                        })
                    }

                }
            }
        }
        setResult(Activity.RESULT_OK)
    }

    override fun onDestroy() {
        super.onDestroy()
        orderId = ""
    }

    companion object {
        var orderId = ""
    }
}
