package com.exz.carprofitmuch.module.main.store.service

import android.content.Intent
import android.view.View
import android.widget.CompoundButton
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.ServiceConfirmBean
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Intent_Finish_Type_1
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Pay_Intent_Finish_Type
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Pay_Intent_OrderId
import com.exz.carprofitmuch.module.mine.goodsorder.GoodsOrderActivity
import com.exz.carprofitmuch.pop.GoodsConfirmCouponPop
import com.exz.carprofitmuch.utils.DialogUtils
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_service_confirm.*
import razerdp.basepopup.BasePopupWindow
import java.text.DecimalFormat

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ServiceConfirmActivity : BaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private var countIndex: Long = 1
    private val priceFormat = DecimalFormat("0.00")

    private lateinit var couponPop: GoodsConfirmCouponPop

    private var shopId = "0"
    private var goodsId = "0"
    private var couponId = "0"
    private var discount = "0"
    private var payMark = "1"


    private var data: ServiceConfirmBean? = null
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.service_confirm_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)


        return false
    }

    override fun setInflateId(): Int = R.layout.activity_service_confirm

    override fun init() {
        try {
            val ids = (intent.getStringExtra(ServiceConfirm_Intent_ids) ?: "").split(",")
            shopId = ids[0]
            goodsId = ids[1]
            payMark = ids[2]
        } catch (e: Exception) {
        }
        initPayMark()
        initEvent()
        initPop()
        getData(countIndex)
    }

    private fun initPayMark() {
        if (payMark == "1") {
            accumulatePoints.visibility = View.GONE
            bt_coupon.visibility = View.GONE
        }
    }

    private fun getData(position: Long) {
        DataCtrlClass.serviceConfirmData(this, shopId, goodsId, position, payMark) { data, index ->
            if (data != null)
                countIndex = data.shopInfo?.goodsInfo?.goodsCount?.toLong() ?: position

            count.text = String.format("%s", index)
            this.data = data

            tv_service_goodsName.text = data?.shopInfo?.goodsInfo?.goodsName
            tv_service_goodsPrice.text = String.format(if (payMark == "1") "%s${getString(R.string.SCORE)}" else "${getString(R.string.CNY)}%s", priceFormat.format(data?.shopInfo?.goodsInfo?.goodsPrice?.toDouble() ?: "0"))

            accumulatePoints.visibility = if (data?.scoreInfo != null && data.scoreInfo?.money?.toDoubleOrNull() != 0.toDouble()) View.VISIBLE else View.GONE
            accumulatePoints.text = data?.scoreInfo?.toString(this)
            //默认优惠券选择第一个

            if (data?.couponInfo != null && data.couponInfo.size > 0) {
                data.couponInfo.firstOrNull()?.isCheck = true
                couponId = data.couponInfo.firstOrNull()?.couponId ?: ""
                couponPop.couponData = (data.couponInfo)
            }
            initData(data)
        }
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        add.setOnClickListener(this)
        minus.setOnClickListener(this)
        bt_confirm.setOnClickListener(this)
        count.setOnClickListener(this)
        bt_coupon.setOnClickListener(this)
    }

    private fun initPop() {
        couponPop = GoodsConfirmCouponPop(this) {
            couponId = it
            discount = data?.couponInfo?.first { couponBean -> couponBean.isCheck }?.discount ?: "0"
        }
        val popDismiss: BasePopupWindow.OnDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                tv_coupon.text = data?.couponInfo?.first { it.isCheck }.toString()
            }
        }
        couponPop.onDismissListener = popDismiss
    }

    /**
     * 计算总价格
     * return 总价格
     */
    private fun initData(data: ServiceConfirmBean?): String {
        //总价格
        var totalPrice = (data?.shopInfo?.goodsInfo?.goodsPrice?.toDouble() ?: 0.toDouble()) * (data?.shopInfo?.goodsInfo?.goodsCount?.toDouble() ?: 0.toDouble())
        //除去优惠券金额
        val couponPrice = (data?.couponInfo?.firstOrNull { it.isCheck }?.discount?.toDoubleOrNull()) ?: 0.toDouble()
        tv_coupon.text = data?.couponInfo?.firstOrNull { it.isCheck }.toString()
        //是否有可选优惠券
        bt_coupon.visibility = if (data?.couponInfo?.isNotEmpty() == true) View.VISIBLE else View.GONE
        totalPrice -= couponPrice

        //避免负数
        totalPrice = if (totalPrice < 0) 0.toDouble() else totalPrice

        //小计金额
        tv_service_totalPrice.text = String.format(if (payMark == "1") "%s${getString(R.string.SCORE)}" else "${getString(R.string.CNY)}%s", priceFormat.format(totalPrice))

        //减去积分对应的金额
        totalPrice -= if (data?.scoreInfo?.isSelect == true) (data.scoreInfo?.money ?: "0").toDouble() else 0.toDouble()
        data?.scores = if (data?.scoreInfo?.isSelect == true) (data.scoreInfo?.money ?: "0").toDouble() else 0.toDouble()

        //避免负数
        totalPrice = if (totalPrice < 0) 0.toDouble() else totalPrice

        //给总价格赋值
        tv_totalPrice.text = String.format(if (payMark == "1") "%s${getString(R.string.SCORE)}" else "${getString(R.string.CNY)}%s", priceFormat.format(totalPrice))
        if (payMark == "1") {
            data?.scores = totalPrice
            data?.totalPrice = 0.toDouble()
        } else
            data?.totalPrice = totalPrice

        return priceFormat.format(totalPrice)
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        data?.scoreInfo?.isSelect = p1
        initData(data)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            bt_confirm -> {
//                shopId", param[0])
//                goodsId", param[1])
//                goodsCount", param[2])
//                payMark", param[3])
//                scores", param[4])
//                ordersMoney", param[5])
//                couponId", param[6])
//                discount", param[7])
                DataCtrlClass.createServiceOrder(this, shopId, goodsId, countIndex.toString(), payMark, data?.scores.toString(), data?.totalPrice.toString(), couponId, discount) {
                    if (it != null) {
                        if (payMark == "1") {
                            startActivity(Intent(this, GoodsOrderActivity::class.java))
                        } else {
                            val intent = Intent(this, PayMethodsActivity::class.java)
                            intent.putExtra(Pay_Intent_OrderId, it.orderId)
                            intent.putExtra(Pay_Intent_Finish_Type, Intent_Finish_Type_1)
                            startActivity(intent)
                        }
                        finish()
                    }
                }


            }

            minus -> if (countIndex > 1) {
                getData(countIndex - 1)
            }
            add -> getData(countIndex + 1)
            count -> DialogUtils.changeNum(mContext, countIndex) {
                getData(it)
            }
            bt_coupon -> couponPop.showPopupWindow()
        }
    }

    companion object {
        var ServiceConfirm_Intent_ids = "ServiceConfirm_Intent_ids"
    }

}