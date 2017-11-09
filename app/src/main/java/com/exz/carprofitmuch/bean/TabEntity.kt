package com.exz.carprofitmuch.bean

import com.flyco.tablayout.listener.CustomTabEntity

class TabEntity(var title: String, private var selectedIcon: Int, private var unSelectedIcon: Int) : CustomTabEntity {

    override fun getTabTitle(): String = title

    override fun getTabSelectedIcon(): Int = selectedIcon

    override fun getTabUnselectedIcon(): Int = unSelectedIcon
}