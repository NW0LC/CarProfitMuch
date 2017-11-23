package com.exz.carprofitmuch.module.main.store.service

import android.content.Intent
import android.view.View
import android.widget.CompoundButton
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.CouponBean
import com.exz.carprofitmuch.bean.ServiceGoodsBean
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity
import com.exz.carprofitmuch.pop.GoodsConfirmCouponPop
import com.exz.carprofitmuch.utils.DialogUtils
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_service_confirm.*
import org.jetbrains.anko.toast
import razerdp.basepopup.BasePopupWindow
import java.text.DecimalFormat

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ServiceConfirmActivity : BaseActivity(),  View.OnClickListener, CompoundButton.OnCheckedChangeListener  {
    private var countIndex: Long = 1
    private var maxCount: Long = 200
    private val decimalFormat= DecimalFormat("0")
    private val priceFormat = DecimalFormat("0.00")

    lateinit var couponPop: GoodsConfirmCouponPop


    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.service_confirm_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)


        return false
    }

    override fun setInflateId(): Int = R.layout.activity_service_confirm

    override fun init() {

        initEvent()
        initPop()
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
        val coupons = ArrayList<CouponBean>()
        coupons.add(CouponBean(couponPrice = "2",couponFullPrice = "2"))
        couponPop.couponData=coupons


        couponPop = GoodsConfirmCouponPop(this){

        }
        val popDismiss: BasePopupWindow.OnDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
//                tv_coupon.text=item.goodsCoupons.first { it.isCheck }.toString()
            }
        }
        couponPop.onDismissListener = popDismiss
    }

    /**
     * 计算总价格
     * return 总价格
     */
    fun initData(data: ServiceGoodsBean): String {
        //总价格
        var totalPrice =data.price.toDouble()
            //除去优惠券金额
            val couponPrice = data.coupons?.first { it.isCheck }?.couponPrice?.toDoubleOrNull()
            tv_coupon.text=data.coupons?.first { it.isCheck }.toString()
            //是否有可选优惠券
            bt_coupon.visibility=if (data.coupons?.isNotEmpty()==true) View.VISIBLE else View.GONE
        totalPrice -= couponPrice?:0.toDouble()

            //避免负数
        totalPrice = if (totalPrice < 0) 0.toDouble() else totalPrice


        //减去积分对应的金额
        totalPrice -= if (data.score?.isSelect == true) (data.score?.scorePrice ?: "0").toDouble() else 0.toDouble()

        //避免负数
        totalPrice = if (totalPrice < 0) 0.toDouble() else totalPrice

        //给总价格赋值
        tv_totalPrice.text = String.format("${getString(R.string.CNY)}%s",priceFormat.format(totalPrice))

        return priceFormat.format(totalPrice)
    }
    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {

    }
    override fun onClick(p0: View?) {
        when (p0) {
            bt_confirm -> {
//                startActivity(Intent(this, PayServiceActivity::class.java))
                startActivity(Intent(this, PayMethodsActivity::class.java))
            }

            minus -> {
                countIndex = if (countIndex <= 1) 1 else --countIndex
                count.text = decimalFormat.format(countIndex)
            }
            add -> {
                if (countIndex + 1 > maxCount) {
                    toast(getString(R.string.classify_pop_toast_outOfDex))
                    return
                }
                countIndex += 1
                count.text = decimalFormat.format(countIndex)
            }
            count -> DialogUtils.changeNum(this, countIndex, {
                onNum(it)
            })
            bt_coupon -> couponPop.showPopupWindow()
        }
    }
    /**
     * 当重新选择商品属性，和输入修改数量时调用。
     */
    private fun onNum(it: Long) {
        countIndex = if (it > maxCount) maxCount else it
        if (countIndex == 0L) {
            if (maxCount >= 0) {
                countIndex = 1
            }
        }
        count.text = decimalFormat.format(countIndex)
    }

}