package com.exz.carprofitmuch.wxapi


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.exz.carprofitmuch.config.Urls
import com.hwangjr.rxbus.RxBus
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.DialogUtils
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory


class WXPayEntryActivity : Activity(), IWXAPIEventHandler {

    lateinit var api: IWXAPI
    private var code = ""
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, Urls.APP_ID)
        api.handleIntent(intent, this)
        when (code) {
            "-1" -> DialogUtils.Warning(this, "支付失败")
            "-2" -> DialogUtils.Warning(this, "支付被取消")
            "0" -> DialogUtils.Warning(this, "支付成功")
        }
        if (DialogUtils.dialog != null) {
            DialogUtils.dialog!!.setOnDismissListener {
                if ("0" == code) {
                    // TODO: 2017/1/16 刷新
                    RxBus.get().post(Constants.BusAction.Pay_Finish, Constants.BusAction.Pay_Finish)
                }
                finish()
            }
            DialogUtils.dialog!!.setOkBtn("确定") { DialogUtils.dialog!!.dismiss() }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {}

    override fun onResp(resp: BaseResp) {
        Log.d(String.format("onPayFinish, errCode = %s", resp.errCode), TAG)

        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            //			AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //			builder.setTitle("提示");
            code = resp.errCode.toString()
            //			Toast.makeText(this,  resp.errStr + ";code=" + String.valueOf(resp.errCode), Toast.LENGTH_SHORT).show();
            //			builder.setMessage(getString(R.string.pay_result_callback_msg, resp.errStr +";code=" + String.valueOf(resp.errCode)));
            //			builder.show();
        }
    }

    companion object {

        private val TAG = "MicroMsg.SDKSample.WXPayEntryActivity"
    }
}