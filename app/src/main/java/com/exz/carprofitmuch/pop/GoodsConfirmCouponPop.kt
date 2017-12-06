package com.exz.carprofitmuch.pop

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsConfirmPopAdapter
import com.exz.carprofitmuch.bean.CouponBean
import com.exz.carprofitmuch.bean.GoodsConfirmSubBean
import com.exz.carprofitmuch.utils.RecycleViewDivider
import kotlinx.android.synthetic.main.pop_coupon_or_red_packet.view.*
import razerdp.basepopup.BasePopupWindow

class GoodsConfirmCouponPop(context: Context,listener:(couponId:String)->Unit) : BasePopupWindow(context), View.OnClickListener {

    private lateinit var inflate: View
    var mAdapter: GoodsConfirmPopAdapter<CouponBean> = GoodsConfirmPopAdapter()
    var data: GoodsConfirmSubBean? = null
        set(value) {
            if (value?.couponInfo?.last()?.toString()!=context.getString(R.string.goods_confirm_coupon_item)) {
                value?.couponInfo?.add(CouponBean())
            }
        field=value

        mAdapter.setNewData(value?.couponInfo)
    }
    var couponData: ArrayList<CouponBean>? = null
        set(value) {
            if (value?.last()?.toString()!=context.getString(R.string.goods_confirm_coupon_item)) {
                value?.add(CouponBean())
            }
        field=value

        mAdapter.setNewData(value)
    }

    init {
        popupWindow.isClippingEnabled = false
        mAdapter.bindToRecyclerView(inflate.mRecyclerView)
        inflate.mRecyclerView.layoutManager = LinearLayoutManager(getContext())
        inflate.mRecyclerView.addItemDecoration(RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(getContext(), R.color.White)))
        inflate.mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                for (couponBean in mAdapter.data) {
                    couponBean.isCheck=false
                }
                mAdapter.data[position].isCheck=true
                data?.couponId=mAdapter.data[position].couponId
                data?.discount=mAdapter.data[position].discount
                listener.invoke(mAdapter.data[position].couponId)
                mAdapter.notifyDataSetChanged()
                dismiss()
            }
        })
    }


    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View? {
        inflate = View.inflate(context, R.layout.pop_coupon_or_red_packet, null)
        inflate.bt_close.setOnClickListener(this)
        inflate.title.text=context.getString(R.string.goods_confirm_coupon_title)
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

}