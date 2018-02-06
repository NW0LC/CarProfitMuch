package com.exz.carprofitmuch.module.mine

import android.app.Activity
import android.content.Intent
import android.text.InputFilter
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.TimeUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.CheckPayBean
import com.exz.carprofitmuch.config.Urls.IsSetPayPwd
import com.exz.carprofitmuch.config.Urls.QRAliPay
import com.exz.carprofitmuch.config.Urls.QRBalancePay
import com.exz.carprofitmuch.config.Urls.QRPayCheck
import com.exz.carprofitmuch.config.Urls.QRWeChatPay
import com.exz.carprofitmuch.module.main.pay.PayActivity
import com.exz.carprofitmuch.module.main.pay.PwdGetCodeActivity
import com.exz.carprofitmuch.pop.PwdPop
import com.exz.carprofitmuch.utils.DialogUtils
import com.exz.carprofitmuch.utils.MoneyEditText
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import com.szw.framelibrary.view.pwd.widget.OnPasswordInputFinish
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_pay_qr.*
import org.jetbrains.anko.toast


/**
 * Created by 史忠文
 * on 2017/8/18.
 */
class PayQRActivity : PayActivity(), View.OnClickListener {

    lateinit var pwdPop: PwdPop
    private var canBalancePay = false
    private var payMoney = "0"
    private var shopId = ""
    private var balance = "0"
    override fun setInflateId() = R.layout.activity_pay_qr
    override fun initToolbar(): Boolean {
        mTitle.text = "付款"
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        toolbar.setNavigationOnClickListener({ DialogUtils.payBack(this@PayQRActivity) })
        return false
    }

    override fun init() {
        pwdPop = PwdPop(this) {
            OnPasswordInputFinish {
                balancePay(it)
                pwdPop.dismiss()
            }
        }
        MoneyEditText.setPricePoint(ed_price)
        radioGroup.check(radioGroup.getChildAt(0).id)
        bt_confirm.setOnClickListener(this)
        ed_remark.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(20))
        myBalance()
        shopInfo()
    }

    override fun onClick(p0: View?) {
        payMoney= if (ed_price.text.toString().isEmpty()) {
            toast("请输入付款金额")
            return
        }else{
            ed_price.text.toString()
        }
        val params=HashMap<String,String>()
        params["shopId"] = shopId
        params["payMoney"] = payMoney
        params["remark"] =ed_remark.text.toString()
        if (radioGroup.checkedRadioButtonId == radioGroup.getChildAt(0).id)
            aliPay(QRAliPay, params, EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+shopId+payMoney, MyApplication.salt).toLowerCase())
        else if (radioGroup.checkedRadioButtonId == radioGroup.getChildAt(1).id)
            weChatPay(QRWeChatPay, params, EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+shopId+payMoney, MyApplication.salt).toLowerCase())
        else if (radioGroup.checkedRadioButtonId == radioGroup.getChildAt(2).id) {
            payMoney=ed_price.text.toString()
            canBalancePay = try {
                (balance.toDouble() ) >= payMoney.toDouble()
            } catch (e: Exception) {
                false
            }
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
        DataCtrlClass.accountBalance(this) {
            (radioGroup.getChildAt(2) as RadioButton).text = String.format("%s(￥%s)", resources.getString(R.string.pay_balance), it?.balance)//我的余额
            canBalancePay = try {
                balance=it?.balance?:"0"
                (it?.balance?.toDouble() ?: 0.toDouble()) >= payMoney.toDouble()
            } catch (e: Exception) {
                false
            }
        }
    }
    /**
     * 店铺信息
     */
    private fun shopInfo() {
        DataCtrlClass.shopInfo(this,intent.getStringExtra("QRCode")) {
            if (it != null) {
                img.setImageURI(it.shopIcon)
                tv_shopName.text=it.shopName
                shopId=it.shopId
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
        map["userId"] = MyApplication.loginUserId
        val nowMills = TimeUtils.getNowMills().toString()
        map["timestamp"] = nowMills
        map["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + nowMills, MyApplication.salt).toLowerCase()
        OkGo.post<NetEntity<String>>(IsSetPayPwd).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<String>>(this) {

                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        //                                "data":"能否支付，0未设置 1已设置
                        if ("0" == response.body().data) {
                            DialogUtils.payNoPwd(this@PayQRActivity) {
                                startActivity(Intent(mContext, PwdGetCodeActivity::class.java))
                            }
                        } else {

                            pwdPop.setPrice(String.format("${getString(R.string.CNY)}%s", payMoney))
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
        map["userId"] = MyApplication.loginUserId
        map["shopId"] = shopId
        map["payPwd"] = payPwd
        map["payMoney"] = payMoney
        map["remark"] = ed_remark.text.toString()
        map["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId +shopId+payMoney+ payPwd, MyApplication.salt).toLowerCase()
        OkGo.post<NetEntity<CheckPayBean>>(QRBalancePay).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<CheckPayBean>>(this) {

                    override fun onSuccess(response: Response<NetEntity<CheckPayBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                        RxBus.get().post(Constants.BusAction.Pay_Finish, Constants.BusAction.Pay_Finish)
                        setResult(Activity.RESULT_OK)
                            DialogUtils.WarningWithListener(this@PayQRActivity,response.body().message) {
                                finish()
                            }
                    }else{
                            toast(response.body().message)
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
        //        payId		String		必填		货源订单id
        //        requestCheck		string		必填		验证请求

        if (payId.isEmpty()) {
            return
        }
        val map = HashMap<String, String>()
        map["userId"] = MyApplication.loginUserId
        map["payId"] = payId
        map["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + payId, MyApplication.salt).toLowerCase()
        OkGo.post<NetEntity<CheckPayBean>>(QRPayCheck).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<CheckPayBean>>(this) {

                    override fun onSuccess(response: Response<NetEntity<CheckPayBean>>) =
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
                })

    }

    override fun onBackPressed() {
        DialogUtils.payBack(this@PayQRActivity)
    }

}