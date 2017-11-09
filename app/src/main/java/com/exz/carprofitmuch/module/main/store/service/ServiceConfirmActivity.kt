package com.exz.carprofitmuch.module.main.store.service

import android.content.Intent
import android.view.View
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.utils.DialogUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_service_confirm.*
import org.jetbrains.anko.toast
import java.text.DecimalFormat

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ServiceConfirmActivity : BaseActivity(), OnRefreshListener, View.OnClickListener {
    private var countIndex: Long = 1
    private var maxCount: Long = 0
    private val decimalFormat= DecimalFormat("0")
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

    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        add.setOnClickListener(this)
        minus.setOnClickListener(this)
        bt_confirm.setOnClickListener(this)
        count.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            bt_confirm -> {
                startActivity(Intent(this, PayServiceActivity::class.java))
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
    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.scoreStoreData(this) {
            if (it != null) {

            }
        }
    }
}