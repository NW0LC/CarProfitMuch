package com.exz.carprofitmuch.module.mine.shop

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import com.blankj.utilcode.util.KeyboardUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.OpenTextBen
import com.exz.carprofitmuch.module.mine.shop.OpenShopActivity.Companion.RESULTCODE_OPEN_SHOP
import com.exz.carprofitmuch.utils.SZWUtils
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_shop_input_text.*
import org.jetbrains.anko.toast


/**
 * Created by pc on 2017/11/23.
 */

class OpenShopInputTextActivity : BaseActivity() {
    private lateinit var entity: OpenTextBen
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(scrollView, 10f)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        return false
    }

    override fun setInflateId(): Int {
        return R.layout.activity_shop_input_text
    }

    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {

        entity = intent.getSerializableExtra("text") as OpenTextBen
        mTitle.text = entity.className
        ed_text.setText(entity.content)
        ed_text.setSelection(entity.content.length)
        ed_text.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(entity.length));
        tv_length.setText("剩余" + (entity.length - ed_text.text.length) + "/" + entity.length)
        SZWUtils.matcherSearchTitle(tv_warn, entity.warn, entity.warn.indexOf("*"), entity.warn.indexOf("*") + 1, ContextCompat.getColor(mContext, R.color.Red))
        ed_text.isFocusable = true
        KeyboardUtils.showSoftInput(ed_text)
        ed_text.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tv_length.setText("剩余" + (entity.length - p0.toString().length) + "/" + entity.length)
            }
        })

        tv_submit.setOnClickListener {
            if (TextUtils.isEmpty(ed_text.text.toString().trim())) {
                mContext.toast("输入项不能为空!")
                return@setOnClickListener
            }
            entity.content = ed_text.text.toString().trim()
            var b = Bundle()
            b.putSerializable("text", entity)
            setResult(RESULTCODE_OPEN_SHOP, Intent().putExtras(b))
            finish()
        }

    }

}
