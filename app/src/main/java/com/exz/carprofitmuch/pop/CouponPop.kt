package com.exz.carprofitmuch.pop

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.CouponAdapter
import com.exz.carprofitmuch.bean.CouponBean
import com.exz.carprofitmuch.utils.RecycleViewDivider
import kotlinx.android.synthetic.main.pop_coupon_or_red_packet.view.*
import org.jetbrains.anko.toast
import razerdp.basepopup.BasePopupWindow

class CouponPop(context: Context) : BasePopupWindow(context), View.OnClickListener {


    private lateinit var inflate: View
    var mAdapter: CouponAdapter<CouponBean> = CouponAdapter()
    var data: ArrayList<CouponBean>? = null
        set(value) {
        field=value
        mAdapter.setNewData(value)
    }

    init {
        mAdapter.bindToRecyclerView(inflate.mRecyclerView)
        inflate.mRecyclerView.layoutManager = LinearLayoutManager(getContext())
        inflate.mRecyclerView.addItemDecoration(RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(getContext(), R.color.White)))
        inflate.mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                getCoupon(getContext(), inflate.mRecyclerView, mAdapter.data[position].couponId)
            }
        })
    }


    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View? {
        inflate = View.inflate(context, R.layout.pop_coupon_or_red_packet, null)
        inflate.bt_close.setOnClickListener(this)
        inflate.title.text=context.getString(R.string.goods_detail_coupon_title)
        return inflate
    }

    override fun initAnimaView(): View = findViewById(R.id.animationView)

    override fun initShowAnimation(): Animation {
        val shakeAnimate = TranslateAnimation(0f, 0f, 600f, 0f)
        shakeAnimate.duration = 300
        return shakeAnimate
    }

    override fun initExitAnimation(): Animation {
        val shakeAnimate = TranslateAnimation(0f, 0f, 0f, 900f)
        shakeAnimate.duration = 300
        return shakeAnimate
    }
    override fun onClick(p0: View?) {
        when (p0) {
            inflate.bt_close -> dismiss()
        }
    }
    companion object {
        private var canGet = true
        private fun getCoupon(context: Context, v: View, id: String) {
            //        page	string	必填	分页请求（从第1页开始，每页50条）
            //        requestCheck	string	必填	验证请求
            if (!canGet) {
                return
            }
            canGet = false
            v.postDelayed({ canGet = true }, 2000)
            DataCtrlClass.getGoodsCoupon(context){
                context.toast(it?:"")
            }
        }
    }

}