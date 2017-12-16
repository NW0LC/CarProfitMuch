package com.exz.carprofitmuch.pop

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import com.blankj.utilcode.util.ScreenUtils
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ServiceListFilterAdapter
import com.exz.carprofitmuch.bean.ServiceListFilterBean
import kotlinx.android.synthetic.main.pop_list.view.*
import razerdp.basepopup.BasePopupWindow

class ServiceListSortPop(context: Activity, listener:(title:String,state:String,position:Int)->Unit) : BasePopupWindow(context) {

    private var firstSetData=true
    var sortId="0"
    var data = ArrayList<ServiceListFilterBean>()
        set(value) {
            field = value
            if (firstSetData) {
                firstSetData=false
//                val bean = ServiceListFilterBean()
//                bean.name="全部"
//                bean.isCheck=true
//                value.add(0,bean)
            }
            adapter.setNewData(value)

        }
    var adapter: ServiceListFilterAdapter<ServiceListFilterBean>
    init {
        popupWindow.isClippingEnabled = false
        isDismissWhenTouchOutside = true
        setBackPressEnable(true)
        isNeedPopupFade = false
        popupWindowView.recyclerView.layoutManager = LinearLayoutManager(context)
        popupWindowView.recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        adapter = ServiceListFilterAdapter()
        adapter.bindToRecyclerView(popupWindowView.recyclerView)
        adapter.setOnItemClickListener { _, _, position ->
            adapter.data.forEach { it.isCheck = false }
            adapter.data[position].isCheck = true
            adapter.notifyDataSetChanged()
            sortId=adapter.data[position].key
            val value = adapter.data[position].value
            listener.invoke(if (position==0) context.getString(R.string.service_list_sort_default) else {
                if (value.length>5)value.substring(0,5)+".." else value
            },adapter.data[position].key,position)
            dismiss()
        }

    }

    override fun initShowAnimation(): Animation {
        val set = AnimationSet(false)
        val showAnimation = TranslateAnimation(0f, 0f, -ScreenUtils.getScreenHeight().toFloat(), 0f)
//        shakeAnimation.interpolator = CycleInterpolator(5f)//循环几次
        showAnimation.duration = 400
//        set.addAnimation(defaultAlphaAnimation)
        set.addAnimation(showAnimation)
        return set
    }

    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View = createPopupById(R.layout.pop_list)

    override fun initAnimaView(): View = findViewById(R.id.popup_animation)


}