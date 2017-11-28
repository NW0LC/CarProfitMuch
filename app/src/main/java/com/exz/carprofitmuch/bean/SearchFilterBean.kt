package com.exz.carprofitmuch.bean

/**
 * Created by Administrator
 * on 2016/10/12.
 */

open class SearchFilterBean : KeyAndValueBean() {
    /**
     * itemId : 1
     * itemTitle : 阿迪
     */

    var itemId: String=""
    var itemTitle: String=""

    override fun absKey(): String = this.itemId

    override fun absValue(): String = this.itemTitle
}
