package com.exz.carprofitmuch.pop

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.*
import com.blankj.utilcode.util.ScreenUtils
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ServiceListFilterAdapter
import com.exz.carprofitmuch.bean.ServiceListFilterBean
import kotlinx.android.synthetic.main.pop_list.view.*
import razerdp.basepopup.BasePopupWindow

class ServiceListSortPop(context: Activity, listener:(title:String,state:String,position:Int)->Unit) : BasePopupWindow(context) {
    companion object {
        var sortId = ""
    }
    private var firstSetData=true
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
    fun setChildClickListener(childClickListener: OnItemChildClickListener) {
        popupWindowView.recyclerView.addOnItemTouchListener(childClickListener)
    }
    init {
        setDismissWhenTouchOutside(true)
        popupWindowView.recyclerView.layoutManager = LinearLayoutManager(context)
        popupWindowView.recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        adapter = ServiceListFilterAdapter()
        adapter.bindToRecyclerView(popupWindowView.recyclerView)
        adapter.setOnItemClickListener { _, _, position ->
            adapter.data.forEach { it.isCheck = false }
            adapter.data[position].isCheck = true
            adapter.notifyDataSetChanged()
            sortId = adapter.data[position].key
            listener.invoke(if (position==0) context.getString(R.string.service_list_sort_default) else adapter.data[position].value,adapter.data[position].key,position)
            dismiss()
        }

    }

    override fun initShowAnimation(): Animation {
        val set = AnimationSet(false)
        val showAnimation = TranslateAnimation(0f, 0f, -ScreenUtils.getScreenHeight().toFloat(), 0f)
//        shakeAnimation.interpolator = CycleInterpolator(5f)//循环几次
        showAnimation.duration = 400
        set.addAnimation(defaultAlphaAnimation)
        set.addAnimation(showAnimation)
        return set
    }

    override fun initExitAnimation(): Animation {
        val set = AnimationSet(false)
        val showAnimation = TranslateAnimation(0f, 0f, 0f, -ScreenUtils.getScreenHeight().toFloat())
//        shakeAnimation.interpolator = CycleInterpolator(5f)//循环几次
        showAnimation.duration = 400
        val alphaAnimation = AlphaAnimation(1.0f, 0.0f)
        alphaAnimation.duration = 300L
        alphaAnimation.interpolator = AccelerateInterpolator()
        set.addAnimation(alphaAnimation)
        set.addAnimation(showAnimation)
        return set
    }


    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View = createPopupById(R.layout.pop_list)

    override fun initAnimaView(): View = findViewById(R.id.popup_animation)


}