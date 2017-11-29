package com.exz.carprofitmuch.module.main.store.service

import android.content.Intent
import android.view.View
import android.widget.CompoundButton
import com.blankj.utilcode.util.EncryptUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.CouponBean
import com.exz.carprofitmuch.bean.ServiceGoodsBean
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Intent_Finish_Type_1
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Pay_Intent_Finish_Type
import com.exz.carprofitmuch.pop.GoodsConfirmCouponPop
import com.exz.carprofitmuch.utils.DialogUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_service_confirm.*
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
    fun initData(data: ServiceGoodsBean?): String {
        //总价格
        var totalPrice =data?.price?.toDouble()?:0.toDouble()
            //除去优惠券金额
            val couponPrice = data?.coupons?.first { it.isCheck }?.couponPrice?.toDoubleOrNull()
            tv_coupon.text=data?.coupons?.first { it.isCheck }.toString()
            //是否有可选优惠券
            bt_coupon.visibility=if (data?.coupons?.isNotEmpty()==true) View.VISIBLE else View.GONE
        totalPrice -= couponPrice?:0.toDouble()

            //避免负数
        totalPrice = if (totalPrice < 0) 0.toDouble() else totalPrice


        //减去积分对应的金额
        totalPrice -= if (data?.score?.isSelect == true) (data.score?.scorePrice ?: "0").toDouble() else 0.toDouble()

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
                val intent = Intent(this, PayMethodsActivity::class.java)
                intent.putExtra(Pay_Intent_Finish_Type, Intent_Finish_Type_1)
                startActivity(intent)
            }

            minus -> if (countIndex > 1) {
                changeCount("", (countIndex - 1)) {}
            }
            add -> changeCount("", (countIndex + 1)) {}
            count -> DialogUtils.changeNum(mContext, countIndex) {
                changeCount("", it) {}
            }
            bt_coupon -> couponPop.showPopupWindow()
        }
    }
    /**
     * 购物车更改 商品数量
     * @param goodsEntity 商品
     */
    private fun changeCount(serviceId: String, index: Long, listener: (data: ServiceGoodsBean?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("goodsCount", String.format("%s", index))
        params.put("serviceId", serviceId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<ServiceGoodsBean>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ServiceGoodsBean>>(this) {
                    override fun onSuccess(response: Response<NetEntity<ServiceGoodsBean>>) =
                            if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                                count.text = String.format("%s", index)
                                initData(response.body().data)
                                listener.invoke(response.body().data)
                            } else {
                                listener.invoke(null)
                            }

                    override fun onError(response: Response<NetEntity<ServiceGoodsBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })

    }


}