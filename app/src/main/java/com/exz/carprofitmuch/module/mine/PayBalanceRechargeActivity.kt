package com.exz.carprofitmuch.module.mine

import android.text.TextUtils
import android.view.View
import com.blankj.utilcode.util.EncryptUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.CheckPayBean
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.module.main.pay.PayActivity
import com.exz.carprofitmuch.utils.DialogUtils
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
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_account_balance_recharge.*
import org.jetbrains.anko.toast

/**
 * Created by 史忠文
 * on 2017/8/18.
 * 余额充值
 */
class PayBalanceRechargeActivity : PayActivity(), View.OnClickListener {

    override fun setInflateId() = R.layout.activity_account_balance_recharge
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        mTitle.text = getString(R.string.balance_recharge_name)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        toolbar.setNavigationOnClickListener({ DialogUtils.payBack(this@PayBalanceRechargeActivity) })
        return false
    }

    override fun init() {
        super.init()
        radioGroup.check(radioGroup.getChildAt(0).id)
        bt_confirm.setOnClickListener(this)
        initView()
    }

    private fun initView() {

        tv_minRecharge.text = String.format(mContext.getString(R.string.mine_recharge), intent.getStringExtra(PayBalanceRecharge_Intent_MinRecharge))
    }

    override fun onClick(p0: View?) {
        val rechargeMoney = ed_rechargeMoney.text.toString().trim()
        if (TextUtils.isEmpty(rechargeMoney)) {
            mContext.toast("请输入充值金额,数值为整数")
            return
        }
        if(rechargeMoney.toInt()<intent.getStringExtra(PayBalanceRecharge_Intent_MinRecharge).toFloat()){
            mContext.toast("最低充值额度"+intent.getStringExtra(PayBalanceRecharge_Intent_MinRecharge))
            return
        }

        if (radioGroup.checkedRadioButtonId == radioGroup.getChildAt(0).id)
            aliPay(Urls.AliRecharge, "rechargeMoney", rechargeMoney,EncryptUtils.encryptMD5ToString(MyApplication.loginUserId , MyApplication.salt).toLowerCase())
        else if (radioGroup.checkedRadioButtonId == radioGroup.getChildAt(1).id)
            weChatPay(Urls.WeChatRecharge, "rechargeMoney", rechargeMoney,EncryptUtils.encryptMD5ToString(MyApplication.loginUserId , MyApplication.salt).toLowerCase())
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag (Constants.BusAction.Pay_Finish)))
    fun PayFinish(tag: String) {
        checkPay()
    }

    private fun checkPay() {

        if (rechargeId.isEmpty()) {
            return
        }
        val map = HashMap<String, String>()
        map.put("userId", MyApplication.loginUserId)
        map.put("rechargeId", rechargeId)
        map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + rechargeId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<CheckPayBean>>(Urls.BalanceRechargeCheck).tag(this)
                .params(map)
                .execute(object : DialogCallback<NetEntity<CheckPayBean>>(this) {

                    override fun onSuccess(response: Response<NetEntity<CheckPayBean>>) {
                        if (response.body().info?.payState == "3") {

//                            val intent = Intent(mContext, PayOrderSuccessActivity::class.java)
//                            startActivity(intent)
                            finish()
                        } else {
                            com.szw.framelibrary.utils.DialogUtils.Warning(mContext, getString(R.string.pay_check_failed))
                        }
                    }
                })

    }

    override fun onBackPressed() {
        DialogUtils.payBack(this@PayBalanceRechargeActivity)
    }

    companion object {
        var PayBalanceRecharge_Intent_MinRecharge = "PayBalanceRecharge_Intent_MinRecharge"
    }

}