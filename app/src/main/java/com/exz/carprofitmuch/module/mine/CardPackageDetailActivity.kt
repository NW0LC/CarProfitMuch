package com.exz.carprofitmuch.module.mine

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.blankj.utilcode.util.SizeUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.CardPackageListAdapter.Companion.initStateBtn
import com.exz.carprofitmuch.adapter.ItemCardPackageAdapter
import com.exz.carprofitmuch.bean.ServiceGoodsBean
import com.exz.carprofitmuch.bean.ServiceOrderBean
import com.google.gson.Gson
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_card_package_detail.*
import kotlinx.android.synthetic.main.lay_goods_detail_text.view.*
import kotlinx.android.synthetic.main.layout_card_package_detail.view.*
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class CardPackageDetailActivity : BaseActivity(), View.OnClickListener {
    private var data: ServiceOrderBean? = null
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }
        mTitle.text = getString(R.string.card_package_detail_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_card_package_detail

    override fun init() {
        initEvent()
        initData()
    }

    private fun initEvent() {
        bt_left.setOnClickListener(this)
        bt_mid.setOnClickListener(this)
        bt_right.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        /**         btLeft    btMid     btRight
         * 1未使用            联系商家 申请退款
         * 2已使用  删除订单  联系商家  评价服务
         * 3已过期  删除订单  联系商家
         * 4已退款  删除订单
         * 其他
         */
        if (data != null)
            when (p0.id) {
                R.id.bt_left -> {
                    //删除
                    DataCtrlClass.editServiceState(this, data?.orderId ?: "", "1") {
                        if (it != null)
                            finish()
                    }
                }
                R.id.bt_mid -> {
                    DialogUtils.Call(this, data?.shopPhone ?: "")
                }
                R.id.bt_right -> {
                    when (data?.orderState) {
                        "1" -> {
                            //申请退款
                            DataCtrlClass.editServiceState(this, data?.orderId ?: "", "2") {
                                if (it != null)
                                    initData()
                            }
                        }
                        "2" -> {
                            //评价订单
                            GoodsOrderCommentActivity.shopId = data?.shopId ?: ""
                            GoodsOrderCommentActivity.orderId = data?.orderId ?: ""
                            GoodsOrderCommentActivity.json = Gson().toJson(data?.goodsInfo)
                            startActivityForResult(Intent(this, GoodsOrderCommentActivity::class.java), 100)

                        }
                        else -> {

                        }
                    }
                }
            }
        setResult(Activity.RESULT_OK)

    }

    private fun initData() {
        DataCtrlClass.cardPackageDetailData(this, intent.getStringExtra(CardPackageDetail_Intent_OrderId) ?: "") {
            if (it != null) {
                data = it
                tv_service_goodsName.text = it.shopName
                tv_service_goodsCount.text = it.goodsInfo.size.toString()
                tv_service_time.text = String.format(mContext.getString(R.string.card_package_list_time), it.ticketDate)

                lay_service_code.removeAllViews()
                for (ticketInfoBean in it.ticketInfo) {
                    val layout = View.inflate(mContext, R.layout.layout_card_package_detail, null)

                    layout.tv_service_codeName.text = String.format(mContext.getString(R.string.service_code_code), if (it.ticketInfo.size == 1) "" else it.ticketInfo.indexOf(ticketInfoBean) + 1)
                    layout.tv_service_code.text = ticketInfoBean.ticket
                    layout.tv_service_isUsed.text = when (ticketInfoBean.ticketState) {
                        "1" -> {
                            mContext.getString(R.string.cardPackage_use)
                        }
                        "2" -> {
                            mContext.getString(R.string.cardPackage_used)
                        }
                        "3" -> {
                            mContext.getString(R.string.cardPackage_pass)
                        }
                        "4" -> {
                            mContext.getString(R.string.cardPackage_return)
                        }
                        else -> {
                            ""
                        }
                    }

                    lay_service_code.addView(layout)
                }
                tv_service_shop_name.text = it.shopName

                val mAdapter = ItemCardPackageAdapter<ServiceGoodsBean>()
                mAdapter.bindToRecyclerView(mRecyclerView)
                mRecyclerView.isNestedScrollingEnabled = false
                mRecyclerView.isFocusable = false
                mRecyclerView.layoutManager = LinearLayoutManager(mContext)
                mAdapter.setNewData(it.goodsInfo)

                lay_price.removeAllViews()
                for (info in it.moneyInfo) {
                    val lay1 = LayoutInflater.from(mContext).inflate(R.layout.lay_goods_detail_text, RelativeLayout(mContext), false)
                    lay1.tv_key.width = 1
                    lay1.tv_key.text = info.key
                    if (it.payMark == "1") {
                        lay1.tv_value.text = String.format("%s${mContext.getString(R.string.SCORE)}", info.value)
                        tv_totalPrice.text = String.format("%s${mContext.getString(R.string.SCORE)}", it.actualMoney)
                    } else {
                        lay1.tv_value.text = String.format("${mContext.getString(R.string.CNY)}%s", info.value)
                        tv_totalPrice.text = String.format("${mContext.getString(R.string.CNY)}%s", it.actualMoney)
                    }
                    lay_price.addView(lay1)
                }


                lay_time.removeAllViews()
                lay_time.addView(with(lay_time.context) {

                    verticalLayout {
                        textView(String.format(getString(R.string.goods_order_orderNum),it.orderNum)) {
                            textSize = 14f
                            setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey500))
                        }.lparams { bottomMargin = SizeUtils.dp2px(5f) }
                        for (time in it.dateInfo) {
                            textView(time.key + ":" + time.value) {
                                textSize = 14f
                                setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey500))
                            }.lparams { bottomMargin = SizeUtils.dp2px(5f) }
                        }
                    }
                })

                initStateBtn(it.orderState, it.isEvaluation, tv_service_orderState, bt_left, bt_mid, bt_right)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            initData()
        }
    }

    companion object {
        var CardPackageDetail_Intent_OrderId = "CardPackageDetail_Intent_OrderId"
    }
}