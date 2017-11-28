package com.exz.carprofitmuch.module.main.pay

import android.content.Intent
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.blankj.utilcode.util.EncryptUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.CheckPayBean
import com.exz.carprofitmuch.bean.MyAccountBean
import com.exz.carprofitmuch.module.main.store.service.ServicePayResultActivity
import com.exz.carprofitmuch.module.mine.CardPackageDetailActivity
import com.exz.carprofitmuch.module.mine.goodsorder.GoodsOrderDetailActivity
import com.exz.carprofitmuch.pop.PwdPop
import com.exz.carprofitmuch.utils.DialogUtils
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.RxBus
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import com.szw.framelibrary.view.pwd.widget.OnPasswordInputFinish
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_pay_methods.*


/**
 * Created by 史忠文
 * on 2017/8/18.
 */
class PayMethodsActivity : PayActivity(), View.OnClickListener {

    lateinit var pwdPop: PwdPop
    var canBalancePay = false
    override fun onDestroy() {
        super.onDestroy()
        OrderPrice = ""
        OrderId = ""
        payPrice = ""
        paySuccessDate = ""
    }

    companion object {
        var Intent_Finish_Type="Intent_Finish_Type"
        var Intent_Finish_Type_1="ServiceConfirmActivity"
        var Intent_Finish_Type_2="GoodsConfirmActivity"
        var OrderPrice = ""
        var OrderId = ""
        var payPrice = ""
        var paySuccessDate = ""
    }

    override fun setInflateId() = R.layout.activity_pay_methods
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.pay_service_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, tv_orderNum)
        toolbar.setNavigationOnClickListener({ DialogUtils.payBack(this@PayMethodsActivity) })
        return false
    }

    override fun init() {
        pwdPop = PwdPop(this) {
            OnPasswordInputFinish {
                balancePay(it)
                pwdPop.dismiss()
            }
        }
        pwdPop.setPrice(String.format("${getString(R.string.CNY)}%s", OrderPrice))
        radioGroup.check(radioGroup.getChildAt(0).id)
        bt_confirm.setOnClickListener(this)
        myBalance()
    }

    override fun onClick(p0: View?) {
        startActivity(Intent(this, ServicePayResultActivity::class.java))
        if (radioGroup.checkedRadioButtonId == radioGroup.getChildAt(0).id)
            aliPay("", "orderId", "")
        else if (radioGroup.checkedRadioButtonId == radioGroup.getChildAt(2).id)
            weChatPay("", "orderId", "")
        else if (radioGroup.checkedRadioButtonId == radioGroup.getChildAt(4).id) {
            if (canBalancePay)
                checkHavePayPwd()
            else
                Toast.makeText(mContext, "余额不足", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 我的余额
     */
    private fun myBalance() {
        val map = HashMap<String, String>()
        map.put("userId", MyApplication.loginUserId)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId , MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<MyAccountBean>>("").tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<MyAccountBean>>(this) {

                    override fun onSuccess(response: Response<NetEntity<MyAccountBean>>) {
                        (radioGroup.getChildAt(4) as RadioButton).text = String.format("%s(￥%s)", resources.getString(R.string.pay_balance), response.body().info?.price)//我的余额
                        canBalancePay = try {
                            (response.body().info?.price?.toDouble()?:0.toDouble()) >= OrderPrice.toDouble()
                        } catch (e: Exception) {
                            false
                        }

                    }
                })

    }

    /**
     * 是否有支付密码
     */
    private fun checkHavePayPwd() {
        //        buyCardRecordId	string	必填	会员购买卡种记录id
        //        requestCheck	string	必填	验证请求

        val map = HashMap<String, String>()
        map.put("userId", MyApplication.loginUserId)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>("").tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<String>>(this) {

                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        //                                "data":"能否支付，0未设置 1已设置
                        if ("0" == response.body().info) {
                            DialogUtils.payNoPwd(this@PayMethodsActivity) {
                                startActivity(Intent(mContext, PwdGetCodeActivity::class.java))
                            }
                        } else {
                            pwdPop.showPopupWindow()
                        }
                    }
                })

    }

    /**
     * 余额支付
     */
    private fun balancePay(payPwd: String) {
        //     userId		string		必填		用户Id
//        orderId		string		必填		订单编号
//                paymentPassword		string		必填		支付密码
//                requestCheck		string		必填		验证请求


        val map = HashMap<String, String>()
        map.put("userId", MyApplication.loginUserId)
        map.put("paymentPassword", payPwd)
        map.put("orderId", OrderId)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + OrderId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<CheckPayBean>>("").tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<CheckPayBean>>(this) {

                    override fun onSuccess(response: Response<NetEntity<CheckPayBean>>) {
                        if (response.body().info != null) {
                            payPrice = response.body().info?.payMoney ?: ""
                            paySuccessDate = response.body().info?.paySuccessDate ?: ""
                        }
                        val intent= if (Intent_Finish_Type== Intent_Finish_Type_1) {

                           Intent(mContext, CardPackageDetailActivity::class.java)
                        }else
                            Intent(mContext, GoodsOrderDetailActivity::class.java)
                        startActivity(intent)
                        RxBus.get().post(Constants.BusAction.Pay_Finish, Constants.BusAction.Pay_Finish)
                        finish()
                    }
                })

    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(Constants.BusAction.Pay_Finish)))
    fun PayFinish(tag: String) {
        checkPay()
    }

    private fun checkPay() {
        //        userId		String		必填		用户Id
        //        orderId		String		必填		货源订单id
        //        requestCheck		string		必填		验证请求

        if (orderId.isEmpty()) {
            return
        }
        val map = HashMap<String, String>()
        map.put("userId", MyApplication.loginUserId)
        map.put("orderId", orderId)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + orderId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<CheckPayBean>>("").tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<CheckPayBean>>(this) {

                    override fun onSuccess(response: Response<NetEntity<CheckPayBean>>) {
                        if (response.body().info?.payState == "3") {
                            payPrice = response.body().info?.payMoney ?: ""
                            paySuccessDate = response.body().info?.paySuccessDate ?: ""
                            val intent= if (Intent_Finish_Type== Intent_Finish_Type_1) {

                                Intent(mContext, CardPackageDetailActivity::class.java)
                            }else
                                Intent(mContext, GoodsOrderDetailActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            com.szw.framelibrary.utils.DialogUtils.Warning(mContext, getString(R.string.pay_check_failed))
                        }
                    }
                })

    }

    override fun onBackPressed() {
        DialogUtils.payBack(this@PayMethodsActivity)
    }

}