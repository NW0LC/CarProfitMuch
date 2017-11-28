package com.exz.carprofitmuch.bean

open class SearchFilterEntity : KeyAndValueBean() {
    var isOpen = false //是否展开
    var filter = "全部"//筛选条件
    var items=ArrayList<SearchFilterBean>()
    /**
     * sectionId : 1
     * sectionTitle : 品牌
     */

    var sectionId: String=""
    var sectionTitle: String=""

    override fun absKey(): String = this.sectionId

    override fun absValue(): String = this.sectionTitle
}
