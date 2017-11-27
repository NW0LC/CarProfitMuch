package com.exz.carprofitmuch.module.main.promotion

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.RadioButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.PromotionsAdapter
import com.exz.carprofitmuch.bean.PromotionsBean
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity
import com.exz.carprofitmuch.pop.ServiceListSortPop
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_promotion_list.*
import razerdp.basepopup.BasePopupWindow
import java.util.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 * 商家活动。（Promotions促销）
 */
class PromotionsActivity : BaseActivity(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var status = "0"

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: PromotionsAdapter<PromotionsBean>
    private lateinit var sortPop: ServiceListSortPop
    override fun initToolbar(): Boolean {
        mTitle.text =getString(R.string.promotion_name)

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 45f)
        SZWUtils.setMargin(header, 45f)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_promotions_upgrade

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        initRecycler()
        initFilterPop()
        initEvent()
    }

    private fun initFilterPop() {
        sortPop = ServiceListSortPop(this) { title,state, _  ->
            status = state
            radioButton1.text = title
            radioButton2.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
            onRefresh(refreshLayout)
        }
        val sortData = SZWUtils.getSearchGoodsResultSortData()
        sortData[0].isCheck=true
        status = sortData[0].key
        sortPop.data = sortData
        setGaryOrOrange(radioButton1, false)
        sortPop.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                radioGroup.clearCheck()
            }
        }
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        radioButton1.setOnClickListener(this)
        radioButton2.setOnClickListener(this)
        radioButton3.setOnClickListener(this)
    }

    private fun initRecycler() {
        mAdapter = PromotionsAdapter()
        val arrayList = ArrayList<PromotionsBean>()
       arrayList.add(PromotionsBean())
       arrayList.add(PromotionsBean())
       arrayList.add(PromotionsBean())
       arrayList.add(PromotionsBean())
       arrayList.add(PromotionsBean())
       arrayList.add(PromotionsBean())

        mAdapter.setNewData(arrayList)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener(){
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            startActivity(Intent(this@PromotionsActivity, GoodsDetailActivity::class.java))
            }
        })
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.radioButton1 -> sortPop.showPopupWindow(radioGroup)
            R.id.radioButton2 -> {
                setGaryOrOrange(radioButton1, true)
                radioButton1.text =getString(R.string.service_list_sort_default)
                radioButton2.setTextColor(ContextCompat.getColor(this@PromotionsActivity, R.color.colorPrimary))
                for (valueEntity in sortPop.adapter.data) {
                    valueEntity.isCheck=false
                }
                sortPop.adapter.data.firstOrNull()?.isCheck=true
                sortPop.adapter.notifyDataSetChanged()
                status = "4"
                onRefresh(refreshLayout)
            }

            R.id.radioButton3 -> {
                setGaryOrOrange(radioButton1, true)
                radioButton1.text =getString(R.string.service_list_sort_default)
                radioButton2.setTextColor(ContextCompat.getColor(this@PromotionsActivity, R.color.MaterialGrey600))
                radioButton3.setTextColor(ContextCompat.getColor(this@PromotionsActivity, R.color.colorPrimary))
                for (valueEntity in sortPop.adapter.data) {
                    valueEntity.isCheck=false
                }
                sortPop.adapter.data.firstOrNull()?.isCheck=true
                sortPop.adapter.notifyDataSetChanged()
                status = "5"
                onRefresh(refreshLayout)
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        currentPage = 1
        refreshState = Constants.RefreshState.STATE_REFRESH
        iniData()

    }

    override fun onLoadMoreRequested() {
        refreshState = Constants.RefreshState.STATE_LOAD_MORE
        iniData()
    }

    private fun iniData(){
        DataCtrlClass.promotionsListData(this, currentPage) {
            refreshLayout?.finishRefresh()
            if (it != null) {
                if (refreshState == Constants.RefreshState.STATE_REFRESH) {
                    mAdapter.setNewData(it)
                } else {
                    mAdapter.addData(it)

                }
                if (it.isNotEmpty()) {
                    mAdapter.loadMoreComplete()
                    currentPage++
                } else {
                    mAdapter.loadMoreEnd()
                }
            } else {
                mAdapter.loadMoreFail()
            }
        }
    }

    /**
     * 设置灰色还是橘色 箭头
     *
     * @param b true gary  ; false orange
     */
    private fun setGaryOrOrange(view: RadioButton, b: Boolean) {
        if (b) {
            view.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.selector_service_list_triangle_grey), null)
            view.setTextColor(ContextCompat.getColor(this, R.color.MaterialGrey600))
        } else {
            view.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(this, R.drawable.selector_service_list_triangle_green), null)
            view.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        }
    }


    override fun onDestroy() {
        ServiceListSortPop.sortId = ""
        super.onDestroy()
    }
}