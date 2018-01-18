package com.exz.carprofitmuch.module.main.promotion

import android.app.Activity
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
import com.exz.carprofitmuch.module.MainActivity
import com.exz.carprofitmuch.module.main.promotion.PromotionsDetailActivity.Companion.PromotionsDetail_Intent_PromotionId
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
        mTitle.text = getString(R.string.promotion_name)

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

    override fun setInflateId(): Int = R.layout.activity_promotion_list

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        initRecycler()
        initFilterPop()
        initEvent()
        refreshLayout.autoRefresh()
    }

    private fun initFilterPop() {
        sortPop = ServiceListSortPop(this) { title, state, position->
            status = state
            radioButton1.text = title
            radioButton2.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
            radioButton3.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
            setGaryOrOrange(radioButton1, position==0)
            onRefresh(refreshLayout)
        }
        val sortData = SZWUtils.getPromotionsSortData()
        sortData[0].isCheck = true
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
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val intent = Intent(this@PromotionsActivity, PromotionsDetailActivity::class.java)
                intent.putExtra(PromotionsDetail_Intent_PromotionId, mAdapter.data[position].id)
                startActivityForResult(intent, 100)
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (mAdapter.data[position].isJoin + mAdapter.data[position].state == "01") {
                    if (view?.id == R.id.tv_state) {
                        DataCtrlClass.promotionJoin(this@PromotionsActivity, mAdapter.data[position].id) { onRefresh(refreshLayout) }
                    }
                }
            }
        })
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.radioButton1 ->
                if (!sortPop.isShowing ) {
                    sortPop.showPopupWindow(radioGroup)
                } else
                    radioGroup.clearCheck()
            R.id.radioButton2 -> {
                setGaryOrOrange(radioButton1, true)
                radioButton1.text = getString(R.string.service_list_sort_default)
                radioButton2.setTextColor(ContextCompat.getColor(this@PromotionsActivity, R.color.colorPrimary))
                radioButton3.setTextColor(ContextCompat.getColor(this@PromotionsActivity, R.color.MaterialGrey600))
                for (valueEntity in sortPop.adapter.data) {
                    valueEntity.isCheck = false
                }
                sortPop.adapter.notifyDataSetChanged()
                status = "6"
                onRefresh(refreshLayout)
            }

            R.id.radioButton3 -> {
                setGaryOrOrange(radioButton1, true)
                radioButton1.text = getString(R.string.service_list_sort_default)
                radioButton2.setTextColor(ContextCompat.getColor(this@PromotionsActivity, R.color.MaterialGrey600))
                radioButton3.setTextColor(ContextCompat.getColor(this@PromotionsActivity, R.color.colorPrimary))
                for (valueEntity in sortPop.adapter.data) {
                    valueEntity.isCheck = false
                }
                sortPop.adapter.notifyDataSetChanged()
                status = "7"
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

    private fun iniData() {
        DataCtrlClass.promotionsListData(this, currentPage, status, MainActivity.locationEntity?.longitude ?: "", MainActivity.locationEntity?.latitude ?: "") {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            onRefresh(refreshLayout)
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

}