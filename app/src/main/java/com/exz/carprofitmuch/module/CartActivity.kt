package com.exz.carprofitmuch.module

import com.exz.carprofitmuch.R
import com.szw.framelibrary.base.BaseActivity

class CartActivity : BaseActivity() {

    override fun initToolbar(): Boolean = false

    override fun setInflateId(): Int = R.layout.activity_goods_car

    override fun init() {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = CartFragment.newInstance()
        fragment.canBack=true
        transaction.add(R.id.frameLay, fragment)
        transaction.commit()
    }
}