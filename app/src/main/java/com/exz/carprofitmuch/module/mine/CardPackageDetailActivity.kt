package com.exz.carprofitmuch.module.mine

import android.support.v4.content.ContextCompat
import android.view.View
import com.blankj.utilcode.util.SizeUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_card_package_detail.*
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class CardPackageDetailActivity : BaseActivity(), OnRefreshListener, View.OnClickListener {
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }
        mTitle.text = getString(R.string.card_package_detail_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_card_package_detail

    override fun init() {
        initEvent()

    }

    private fun initEvent() {

    }

    override fun onClick(p0: View?) {

    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        DataCtrlClass.cardPackageDetailData(this) {
            if (it != null) {
                tv_service_goodsName.text=it.goods.name
                tv_service_goodsCount.text=it.goods.count
                tv_service_time.text=String.format(mContext.getString(R.string.card_package_list_time),it.time)

                tv_service_shop_name.text=it.serviceShopName
                img.setImageURI(it.orderImg)
                tv_service_orderName.text=it.goods.name
                tv_service_orderCount.text=String.format(mContext.getString(R.string.card_package_list_count),it.goods.count)
                tv_service_orderPrice.text=String.format(mContext.getString(R.string.card_package_list_price),it.goods.price)
                tv_totalPrice.text=String.format("${mContext.getString(R.string.CNY)}%s",it.orderPrice)

                lay_time.addView(with(lay_time.context){
                    verticalLayout{
                        for (serviceCode in it.serviceCodes) {
                            textView ( serviceCode){
                                textSize = 14f
                                setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                            }.lparams { bottomMargin=SizeUtils.dp2px(5f) }
                        }
                    }
                })
            }
        }
    }
}