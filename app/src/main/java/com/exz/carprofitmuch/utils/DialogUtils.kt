package com.exz.carprofitmuch.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.blankj.utilcode.util.KeyboardUtils
import com.common.controls.dialog.CommonDialogFactory
import com.common.controls.dialog.CoreDialog
import com.common.controls.dialog.DialogUtil
import com.common.controls.dialog.ICommonDialog
import com.exz.carprofitmuch.R
import kotlinx.android.synthetic.main.dialog_change_num.view.*
import kotlinx.android.synthetic.main.dialog_score_failed.view.*
import kotlinx.android.synthetic.main.dialog_score_success.view.*

/**
 * Created by 史忠文
 * on 2017/10/24.
 */
object DialogUtils{
    private lateinit var dialog: ICommonDialog
    /**
     * 清除搜索记录
     */
    fun deleteSearch(context: Context, listener: View.OnClickListener) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103)
        dialog.setTitleText("删除")
        dialog.setContentText("确定清除记录？")
        dialog.setCancelBtn("取消") { dialog.dismiss() }
        dialog.setOkBtn("确定") { v ->
            dialog.dismiss()
            listener.onClick(v)
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    /**
     * 支付返回
     */
    fun payBack(context: Activity) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103)
        dialog.setTitleText("返回")
        dialog.setContentText("您确定放弃支付?")
        dialog.setCancelBtn("支付", { dialog.dismiss() })
        dialog.setOkBtn("确定", {
            dialog.dismiss()
            context.finish()
        })
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    /**
     * 余额支付 没支付密码
     */
    fun payNoPwd(context: Activity, listener: (v: View) -> Unit) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103)
        dialog.setTitleText("啊哦")
        dialog.setContentText("未设置支付密码！")
        dialog.setCancelBtn("取消") { dialog.dismiss() }
        dialog.setOkBtn("去设置") { v ->
            dialog.dismiss()
            listener.invoke(v)
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    private var countIndex = 1
    fun changeNum(context: Context, count: Long, listener: (num: Long) -> Unit) {
        dialog = DialogType104(context)
        val view = View.inflate(context,R.layout.dialog_change_num, null)
        view.count.setText(String.format("%s", count))
        view.count.setSelection(view.count.text.length)
        countIndex = Integer.valueOf(view.count.text.toString())!!
        dialog.setTitleText("修改数量")
        dialog.setContentView(view)
        dialog.setOkBtn("确定") {
            val trim = view.count.text.toString().trim()
            if (!TextUtils.isEmpty(trim)) {
                listener.invoke(trim.toLong())
            }
            dialog.dismiss()
            countIndex = 1
        }
        dialog.setCancelBtn("取消") {
            dialog.dismiss()
            countIndex = 1
        }
        dialog.setOnShowListener { KeyboardUtils.toggleSoftInput() }
        (dialog as DialogType104).setOnBeforeDismiss {
            KeyboardUtils.hideSoftInput(view.count)
            true
        }
        dialog.show()
    }
    /***
     *积分支付成功
     */
    fun scorePaySuccess(context: Context) {

        val inflate = View.inflate(context, R.layout.dialog_score_success, null)
        val dlg = CoreDialog(context, com.common.alertpop.R.style.dialog, inflate, true)
        dlg.setPosition(Gravity.CENTER, 0, 0)
        dlg.setCanceledOnTouchOutside(true)
        inflate.bt_score_success_close.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }

    /***
     *积分支付失败
     */
    fun scorePayFailed(context: Context,reason:String=context.getString(R.string.score_pay_failed_default_reason)) {

        val inflate = View.inflate(context, R.layout.dialog_score_failed, null)
        val dlg = CoreDialog(context, com.common.alertpop.R.style.dialog, inflate, true)
        dlg.setPosition(Gravity.CENTER, 0, 0)
        dlg.setCanceledOnTouchOutside(true)
        inflate.tv_score_failed_reason.text=reason
        inflate.bt_score_failed_close.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }
}