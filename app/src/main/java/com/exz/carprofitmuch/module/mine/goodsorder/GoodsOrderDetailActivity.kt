package com.exz.carprofitmuch.module.mine.goodsorder

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
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
import com.exz.carprofitmuch.module.mine.RefundActivity
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.widget.MyWebActivity
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_my_order_detail.*
import kotlinx.android.synthetic.main.lay_goods_detail_text.view.*
import kotlinx.android.synthetic.main.lay_goods_order_bt.*
import kotlinx.android.synthetic.main.lay_return_goods_num.*
import kotlinx.android.synthetic.main.layout_address.*
import java.util.*

/**
 * Created by pc on 2017/11/15.
 * 订单详情
 */

class GoodsOrderDetailActivity : BaseActivity(), View.OnClickListener {

    private var orderState = "1"
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

    private val arrayList2Item = ArrayList<GoodsBean>()
    override fun init() {
        super.init()
        initView()
        iniData()
        initClick()
    }

    private fun initClick() {
        tv_left.setOnClickListener(this)
        tv_mid.setOnClickListener(this)
        tv_right.setOnClickListener(this)

    }

    @SuppressLint("SetTextI18n")
    private fun initView() {

        adapter = ItemGoodsOrderAdapter()
        arrayList2Item.add(GoodsBean())
        GoodsOrderAdapter.initStateBtn(orderState, tv_order_type, tv_left, tv_mid, tv_right)
        adapter = ItemGoodsOrderAdapter()
        adapter.setNewData(arrayList2Item)
        adapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 15, ContextCompat.getColor(mContext, R.color.app_bg)))
    }


    private fun iniData() {
        DataCtrlClassXZW.OrderDetailData(mContext, orderId) {
            refreshLayout?.finishRefresh()
            if (it != null) {
                entity = it
                tv_userName.text = it.addressInfo!!.name
                tv_userPhone.text = it.addressInfo!!.phone
                tv_address.text = it.addressInfo!!.detailAddress
                tv_shope_name.text = it.shopName
                tv_order_type.text = GoodsOrderAdapter.getState(it.orderState!!)
                ll_lay1.removeAllViews()
                for (info in it.moneyInfo!!) {
                    val lay1 = LayoutInflater.from(mContext).inflate(R.layout.lay_goods_detail_text, RelativeLayout(mContext), false)
                    lay1.tv_key.width = 1
                    ll_lay1.addView(lay1)
                    lay1.tv_key.setText(info.key)
                    lay1.tv_value.setText(info.value)
                }
                ll_lay2.removeAllViews()
                for (info in it.dateInfo!!) {
                    val lay2 = LayoutInflater.from(mContext).inflate(R.layout.lay_goods_detail_text, RelativeLayout(mContext), false)
                    ll_lay2.addView(lay2)
                    lay2.tv_key.setText(info.key)
                    lay2.tv_value.setText(info.value)
                }
                tv_goods_total.text = String.format(mContext.getString(R.string.CNY), it.actualMoney)
                adapter.setNewData(it.goodsInfo)
                adapter.loadMoreEnd()

                GoodsOrderAdapter.initStateBtn(it.orderState!!, tv_my_order, TextView(mContext), tv_mid, tv_right)
            }
        }
    }

    /**         btLeft    btMid     btRight
     * 1待付款     【联系商家   取消订单  支付订单】
     * 2待发货     【         联系商家  申请退款】
     * 3待收货     【联系商家   查看物流  确认收货】
     * 4待评价     【联系商家   申请退货  评价订单】
     * 5已结束     【联系商家   申请退货  删除订单】
     * 其他
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_left -> {
                DialogUtils.Call(this, entity.shopPhone!!)
            }
            R.id.tv_mid -> {
                when (orderState) {
                    "1" -> {//取消订单
                        DataCtrlClassXZW.EditOrderData(mContext, entity.orderId!!, "0", {
                            if (it != null) {
                                finish()
                            }
                        })
                    }

                    "2" -> {  //联系商家
                        DialogUtils.Call(this, entity.shopPhone!!)
                    }
                    "3" -> {//查看物流
                        startActivity(Intent(this, MyWebActivity::class.java).putExtra(MyWebActivity.Intent_Url, "http://m.kuaidi100.com/result.jsp?nu=" + entity.logisticsNum).putExtra(MyWebActivity.Intent_Title, "查看物流"))
                    }
                    "4", "5" -> {    //删除订单

                        startActivity(Intent(this, RefundActivity::class.java).putExtra(RefundActivity.OrderId, entity.orderId))


                    }

                }


            }
            R.id.tv_right -> {
                when (orderState) {
                    "1" -> {//支付订单
                        startActivity(Intent(this, PayMethodsActivity::class.java).putExtra(Pay_Intent_OrderId, entity.orderId).putExtra(Pay_Intent_Finish_Type, Intent_Finish_Type_2))

                    }

                    "2" -> {
                        com.exz.carprofitmuch.utils.DialogUtils.refund(mContext, orderId, {
                            if (it != null) {

                                DataCtrlClassXZW.ApplyReturnMoney(mContext, orderId, it, {
                                    if (it != null) {
                                        finish()
                                    }
                                })
                            }

                        })
                    }
                    "3" -> {//确认收货
                        DataCtrlClassXZW.EditOrderData(mContext, entity.orderId!!, "2", {
                            if (it != null) {
                                finish()
                            }
                        })
                    }
                    "4" -> {    //评价订单
                        GoodsOrderCommentActivity.shopId = entity.shopId!!
                        GoodsOrderCommentActivity.orderId = entity.orderId!!
                        GoodsOrderCommentActivity.json = JSON.toJSONString(entity.goodsInfo)
                        startActivity(Intent(this, GoodsOrderCommentActivity::class.java))

                    }
                    "5" -> {// 删除订单
                        DataCtrlClassXZW.EditOrderData(mContext, entity.orderId!!, "1", {
                            if (it != null) {
                                finish()
                            }
                        })
                    }

                }
            }
        }
    }

    override fun onBackPressed() {
        orderId = ""
        super.onBackPressed()
    }

    companion object {
        var orderId = ""
    }
}
