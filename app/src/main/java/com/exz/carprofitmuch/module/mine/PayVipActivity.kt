package com.exz.carprofitmuch.module.mine

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.TimeUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.CheckPayBean
import com.exz.carprofitmuch.config.Urls.AliPay
import com.exz.carprofitmuch.config.Urls.BalancePay
import com.exz.carprofitmuch.config.Urls.IsSetPayPwd
import com.exz.carprofitmuch.config.Urls.PayState
import com.exz.carprofitmuch.config.Urls.VipSignature
import com.exz.carprofitmuch.config.Urls.WeChatPay
import com.exz.carprofitmuch.module.main.pay.PayActivity
import com.exz.carprofitmuch.module.main.pay.PwdGetCodeActivity
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
import kotlinx.android.synthetic.main.activity_pay_vip.*
import org.jetbrains.anko.toast


/**
 * Created by 史忠文
 * on 2017/8/18.
 */
class PayVipActivity : PayActivity(), View.OnClickListener {

    lateinit var pwdPop: PwdPop
    private var canBalancePay = false
    private var rechargePrice = ""
    override fun setInflateId() = R.layout.activity_pay_vip
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.pay_vip_recharge)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, lay_price)
        toolbar.setNavigationOnClickListener({ DialogUtils.payBack(this@PayVipActivity) })
        return false
    }

    override fun init() {
        pwdPop = PwdPop(this) {
            OnPasswordInputFinish {
                balancePay(it)
                pwdPop.dismiss()
            }
        }

        radioGroup.check(radioGroup.getChildAt(0).id)
        bt_confirm.setOnClickListener(this)
        orderInfo()
        myBalance()
    }

    override fun onClick(p0: View?) {
        if (radioGroup.checkedRadioButtonId == radioGroup.getChildAt(0).id)
            aliPay(AliPay, "vipPrice", rechargePrice, EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, MyApplication.salt).toLowerCase())
        else if (radioGroup.checkedRadioButtonId == radioGroup.getChildAt(1).id)
            weChatPay(WeChatPay, "vipPrice", rechargePrice, EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, MyApplication.salt).toLowerCase())
        else if (radioGroup.checkedRadioButtonId == radioGroup.getChildAt(2).id) {
            if (canBalancePay)
                checkHavePayPwd()
            else
                Toast.makeText(mContext, "余额不足", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 缴费金额
     */
    private fun orderInfo() {
//        userId	string	必填	用户id
//        requestCheck	string	必填	验证请求

        val map = HashMap<String, String>()
        map.put("userId", MyApplication.loginUserId)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(VipSignature).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<String>>(this) {

                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            rechargePrice = response.body()?.data ?: ""
                            pwdPop.setPrice(String.format("${getString(R.string.CNY)}%s", rechargePrice))
                            tv_rechargeMoney.text = String.format(getString(R.string.pay_vip_unit), response.body()?.data ?: "")
                        } else {
                            toast(response.body()?.message ?: "")
                        }
                    }
                })

    }

    /**
     * 我的余额
     */
    private fun myBalance() {
        DataCtrlClass.accountBalance(this) {
            (radioGroup.getChildAt(2) as RadioButton).text = String.format("%s(￥%s)", resources.getString(R.string.pay_balance), it?.balance)//我的余额
            canBalancePay = try {
                (it?.balance?.toDouble() ?: 0.toDouble()) >= rechargePrice.toDouble()
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * 是否有支付密码
     */
    private fun checkHavePayPwd() {
//        userId	string	必填	用户id
//                timestamp	string	必填	时间戳
//                requestCheck	string	必填	验证请求

        val map = HashMap<String, String>()
        map.put("userId", MyApplication.loginUserId)
        val nowMills = TimeUtils.getNowMills().toString()
        map.put("timestamp", nowMills)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + nowMills, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(IsSetPayPwd).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<String>>(this) {

                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        //                                "data":"能否支付，0未设置 1已设置
                        if ("0" == response.body().data) {
                            DialogUtils.payNoPwd(this@PayVipActivity) {
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
//        userId	string	必填	用户id
//                orderId	string	必填	订单id
//                payPwd	string	必填	支付密码
//                requestCheck	string	必填	验证请求


        val map = HashMap<String, String>()
        map.put("userId", MyApplication.loginUserId)
        map.put("payPwd", payPwd)
        map.put("vipPrice", rechargePrice)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + payPwd, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<CheckPayBean>>(BalancePay).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<CheckPayBean>>(this) {

                    override fun onSuccess(response: Response<NetEntity<CheckPayBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                        RxBus.get().post(Constants.BusAction.Pay_Finish, Constants.BusAction.Pay_Finish)
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    }
                })

    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(Constants.BusAction.Pay_Finish)))
    fun PayFinish(tag: String) {
        checkPay()
    }

    private fun checkPay() {
        //        userId		String		必填		用户Id
        //        rechargeId		String		必填		货源订单id
        //        requestCheck		string		必填		验证请求

        if (rechargeId.isEmpty()) {
            return
        }
        val map = HashMap<String, String>()
        map.put("userId", MyApplication.loginUserId)
        map.put("vipOrderId", vipOrderId)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + vipOrderId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<CheckPayBean>>(PayState).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<CheckPayBean>>(this) {

                    override fun onSuccess(response: Response<NetEntity<CheckPayBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                        if (response.body().data?.payState == "1") {
                            setResult(Activity.RESULT_OK)
                            finish()
                        } else {
                            com.szw.framelibrary.utils.DialogUtils.Warning(mContext, getString(R.string.pay_check_failed))
                        }
                    }else{
                            toast(response.body().message)
                        }
                    }
                })

    }

    override fun onBackPressed() {
        DialogUtils.payBack(this@PayVipActivity)
    }

}