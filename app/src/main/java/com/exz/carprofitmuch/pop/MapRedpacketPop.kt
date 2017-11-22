package com.exz.carprofitmuch.pop

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.module.main.store.normal.GoodsShopActivity
import kotlinx.android.synthetic.main.pop_map_treasure.view.*
import razerdp.basepopup.BasePopupWindow

/**
 * Created by pc on 2017/11/22.
 */

class MapRedpacketPop(context: Context) : BasePopupWindow(context) {


    private lateinit var inflate: View

    init {
        popupWindow.isClippingEnabled = false
        inflate.tv_store.setOnClickListener { context.startActivity(Intent(context, GoodsShopActivity::class.java))
        dismiss()
        }
    }


    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View? {
        inflate = View.inflate(context, R.layout.pop_map_red_packet, null)

        return inflate
    }

    override fun initAnimaView(): View = findViewById(R.id.animationView)

    override fun initShowAnimation(): Animation {
        var shakeAnimate =   AnimationUtils.loadAnimation(context, R.anim.map_open);
        return shakeAnimate
    }

    override fun initExitAnimation(): Animation {
        val shakeAnimate = TranslateAnimation(0f, 0f, 0f, 900f)
        shakeAnimate.duration = 300
        return shakeAnimate
    }

}