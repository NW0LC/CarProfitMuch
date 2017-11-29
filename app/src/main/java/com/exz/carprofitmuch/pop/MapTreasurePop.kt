package com.exz.carprofitmuch.pop

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.MapGetTreasurePacketBean
import com.exz.carprofitmuch.bean.MapPinBean
import com.exz.carprofitmuch.module.main.store.normal.GoodsShopActivity
import com.exz.carprofitmuch.module.main.store.normal.GoodsShopActivity.Companion.GoodsShop_Intent_ShopId
import com.exz.carprofitmuch.module.main.store.service.ServiceShopActivity
import com.exz.carprofitmuch.module.main.store.service.ServiceShopActivity.Companion.ServiceShop_Intent_ServiceShopId
import kotlinx.android.synthetic.main.pop_map_treasure.view.*
import razerdp.basepopup.BasePopupWindow

/**
 * Created by pc on 2017/11/22.
 */

class MapTreasurePop(context: Context) : BasePopupWindow(context) {


    private lateinit var inflate: View
    private var entity: MapPinBean? = null

    init {
        popupWindow.isClippingEnabled = false
        inflate.tv_store.setOnClickListener {
            //1实物类(商品/店铺) 2虚拟类(商品/店铺)
            if (entity!!.classMark.equals("1")) {
                context.startActivity(Intent(context, GoodsShopActivity::class.java).putExtra(GoodsShop_Intent_ShopId, entity!!.id))
            } else {
                context.startActivity(Intent(context, ServiceShopActivity::class.java).putExtra(ServiceShop_Intent_ServiceShopId, entity!!.id))
            }
            dismiss()
        }
    }


    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View? {
        inflate = View.inflate(context, R.layout.pop_map_treasure, null)

        return inflate
    }

    override fun initAnimaView(): View = findViewById(R.id.animationView)

    override fun initShowAnimation(): Animation {

        var shakeAnimate = AnimationUtils.loadAnimation(context, R.anim.map_open);
        return shakeAnimate
    }

    override fun initExitAnimation(): Animation {
        val shakeAnimate = TranslateAnimation(0f, 0f, 0f, 900f)
        shakeAnimate.duration = 300
        return shakeAnimate
    }

    fun setData(it: MapGetTreasurePacketBean, entity: MapPinBean) {
        this.entity = entity;
        inflate.tv_title.text = it.title
        inflate.tv_time.text = it.date
    }

}
