package com.exz.carprofitmuch.module.mine.favorite

import android.support.v4.app.Fragment
import android.view.View
import android.widget.TextView
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.FavoriteGoodsAdapter
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.TabEntity
import com.exz.carprofitmuch.config.Constants
import com.flyco.tablayout.listener.CustomTabEntity
import com.hwangjr.rxbus.RxBus
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_favorite_goods.*
import java.util.*

/**
 * Created by 史忠文
 * on 2017/3/28.
 */

class FavoriteGoodsActivity : BaseActivity() {

    private val mTitles = arrayOf("全部商品", "降价商品", "失效商品")
    private val mIconUnSelectIds = intArrayOf(R.mipmap.icon_home_off, R.mipmap.icon_store_off, R.mipmap.icon_goods_car_off, R.mipmap.icon_mine_off)
    private val mIconSelectIds = intArrayOf(R.mipmap.icon_home_on, R.mipmap.icon_store_on, R.mipmap.icon_goods_car_on, R.mipmap.icon_mine_on)
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val mFragments = ArrayList<Fragment>()
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }
        mTitle.text = getString(R.string.favorite_goods_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        toolbar.inflateMenu(R.menu.menu_favorite_goods)
        val actionView = toolbar.menu.getItem(0).actionView
        (actionView as TextView).text=getString(R.string.favorite_goods_edit)
        actionView.setOnClickListener{
            when (if (Edit_Type == "-1") Edit_Type_Edit else Edit_Type) {
                Edit_Type_Edit -> {
                    lay_tabLayout.visibility = View.GONE
                    actionView.text=getString(R.string.favorite_goods_confirm)
                    Edit_Type = Edit_Type_Delete
                }
                Edit_Type_Delete -> {
                    lay_tabLayout.visibility = View.VISIBLE
                    actionView.text=getString(R.string.favorite_goods_edit)
                    Edit_Type = Edit_Type_Edit
                }
        }
            actionView.isClickable = false
            RxBus.get().post(Constants.Collection_Edit, Edit_Type)
            actionView.postDelayed({ actionView.isClickable = true }, 500)
        }
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_favorite_goods

    override fun init() {
        mFragments.add(FavoriteGoodsFragment.newInstance())
        mFragments.add(FavoriteGoodsFragment.newInstance())
        mFragments.add(FavoriteGoodsFragment.newInstance())
        mTitles.indices.mapTo(mTabEntities) { TabEntity(mTitles[it], mIconSelectIds[it], mIconUnSelectIds[it]) }
        mTabLayout.setTabData(mTabEntities, this, R.id.frameLayout, mFragments)

//        val animatorExit = ObjectAnimator.ofFloat(lay_headLayout, "translationY", 0f, SizeUtils.dp2px(-45f).toFloat())
//        val animatorIn = ObjectAnimator.ofFloat(lay_headLayout, "translationY", SizeUtils.dp2px(-45f).toFloat(), 0f)
//        val transition = LayoutTransition()
//        transition.setDuration(200)
//        lay_headLayout.layoutTransition = transition
//        transition.setAnimator(LayoutTransition.APPEARING, animatorIn)
//        transition.setAnimator(LayoutTransition.DISAPPEARING, animatorExit)
    }


    override fun onDestroy() {
        super.onDestroy()
        Edit_Type = "-1"
    }

    companion object {
        var Edit_Type = "-1"
        val Edit_Type_Edit = "0"
        val Edit_Type_Delete = "1"

        /**
         * @param adapter       适配器
         * @param goodsEntities 移除
         */
        fun removeItem(adapter: FavoriteGoodsAdapter<out GoodsBean>, goodsEntities: Array<GoodsBean>?) {
            if (goodsEntities==null) {
                return
            }
            val iterator = adapter.data.iterator()
            while (iterator.hasNext()) {
                val temp = iterator.next()
                for (goodsEntity in goodsEntities) {
                    if (temp.id == goodsEntity.id) {
                        val position = adapter.data.indexOf(temp)
                        iterator.remove()
                        if (goodsEntities.isEmpty())
                            adapter.notifyDataSetChanged()
                        else
                        adapter.notifyItemRemoved(position)
                    }
                }

            }
        }
    }
}
