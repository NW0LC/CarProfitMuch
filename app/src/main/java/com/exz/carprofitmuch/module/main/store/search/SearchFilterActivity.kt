package com.exz.carprofitmuch.module.main.store.search

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.MainStoreAdapter
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.SearchFilterEntity
import com.exz.carprofitmuch.module.main.store.search.SearchGoodsActivity.Companion.Intent_Search_Content
import com.exz.carprofitmuch.module.main.store.search.SearchGoodsActivity.Companion.Intent_isShowSoft
import com.exz.carprofitmuch.pop.SearchFilterPop
import com.exz.carprofitmuch.pop.ServiceListSortPop
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_search_goods_filter.*
import org.jetbrains.anko.textColor
import razerdp.basepopup.BasePopupWindow
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
class SearchFilterActivity : BaseActivity(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var typeId: String = ""
    private var searchContent: String = ""
    private var status = "0"
    private var otherSift = ""

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: MainStoreAdapter<GoodsBean>
    private lateinit var sortPop: ServiceListSortPop
    private lateinit var filterPopWin: SearchFilterPop
    private var filterEntities = ArrayList<SearchFilterEntity>()
    override fun initToolbar(): Boolean {
        mTitle.hint = getString(R.string.search_filter_hint)
        mTitle.text = URLDecoder.decode(searchContent, "utf-8")
        val params = LinearLayout.LayoutParams(ScreenUtils.getScreenWidth() - SizeUtils.dp2px(85f), SizeUtils.dp2px(35f))
        params.topMargin = SizeUtils.dp2px(10f)
        params.bottomMargin = SizeUtils.dp2px(10f)
        params.marginEnd = SizeUtils.dp2px(10f)
        mTitle.layoutParams = params
        mTitle.textColor = ContextCompat.getColor(mContext, R.color.MaterialGrey500)
        mTitle.textSize = 12f
        mTitle.gravity = Gravity.CENTER_VERTICAL
        mTitle.background = ContextCompat.getDrawable(mContext, R.drawable.search_filter_title_bg)
        mTitle.setPadding(SizeUtils.dp2px(10f), SizeUtils.dp2px(5f), SizeUtils.dp2px(5f), SizeUtils.dp2px(5f))
        mTitle.setOnClickListener(this)

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 50f)
        SZWUtils.setMargin(header, 45f)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_search_goods_filter

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)

        typeId = intent.getStringExtra("typeId") ?: ""
        searchContent = intent.getStringExtra(Intent_Search_Content) ?: ""
        if (!TextUtils.isEmpty(searchContent)) {
            this.searchContent = URLEncoder.encode(searchContent, "utf-8")
        }


        initRecycler()
        initFilterPop()
        initEvent()
        onRefresh(refreshLayout)
    }

    private fun initFilterPop() {
        sortPop = ServiceListSortPop(this) { title, state, _ ->
            status = state
            radioButton1.text = title
            radioButton2.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
            onRefresh(refreshLayout)
        }
        val sortData = SZWUtils.getSearchGoodsResultSortData()
        sortData[0].isCheck = true
        status = sortData[0].key
        sortPop.data = sortData
        setGaryOrOrange(radioButton1, false)
        sortPop.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                radioGroup.clearCheck()
            }
        }




        filterPopWin = SearchFilterPop(this)
        filterPopWin.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                otherSift = ""
                if (filterEntities.size > 0)
                    for (filterEntity in filterEntities) {
                        var str = "" + filterEntity.sectionId + ":" + "f"
                        var str2 = ""
                        filterEntity.items.filter { it.isCheck }.forEach { str2 += it.itemId + "," }
                        if (!TextUtils.isEmpty(str2)) {
                            str2 = str2.substring(0, str2.length - 1)
                            str = str.replace("f", str2)
                            otherSift += str + "|"
                        }
                    }
                if (TextUtils.isEmpty(otherSift) && TextUtils.isEmpty(filterPopWin.lowPrice) && TextUtils.isEmpty(filterPopWin.heightPrice)) {
                    radioButton4.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.icon_search_filter_off), null)
                    radioButton4.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                } else {
                    radioButton4.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.icon_search_filter_on), null)
                    radioButton4.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                }
                radioGroup.clearCheck()
                onRefresh(refreshLayout)
            }
        }
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        radioButton1.setOnClickListener(this)
        radioButton2.setOnClickListener(this)
        radioButton4.setOnClickListener(this)
    }

    private fun initRecycler() {
        mAdapter = MainStoreAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (SZWUtils.getMarkIntent(this@SearchFilterActivity, mAdapter.data[position]) != null)
                    startActivity(SZWUtils.getMarkIntent(this@SearchFilterActivity, mAdapter.data[position]))
            }
        })
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.mTitle -> {
                val intent = Intent()
                intent.setClass(this@SearchFilterActivity, SearchGoodsActivity::class.java)
                intent.putExtra(Intent_isShowSoft, true)
                startActivity(intent)
                finish()
            }
            R.id.radioButton1 ->
                if (!sortPop.isShowing)
                    sortPop.showPopupWindow(radioGroup)
                else
                    radioGroup.clearCheck()
            R.id.radioButton2 -> {
                setGaryOrOrange(radioButton1, true)
                radioButton1.text = getString(R.string.service_list_sort_default)
                radioButton2.setTextColor(ContextCompat.getColor(this@SearchFilterActivity, R.color.colorPrimary))
                for (valueEntity in sortPop.adapter.data) {
                    valueEntity.isCheck = false
                }
                sortPop.adapter.data.firstOrNull()?.isCheck = true
                sortPop.adapter.notifyDataSetChanged()
                status = "4"
                onRefresh(refreshLayout)
            }

            R.id.radioButton4 -> {
                filterPopWin.showPopupWindow()
                if (filterEntities.size == 0) {
                    DataCtrlClass.searchFilterData(mContext, typeId, searchContent) {
                        filterEntities = it ?: ArrayList()
                        filterPopWin.setData(filterEntities)
                    }
                }
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
        DataCtrlClass.searchFilterGoodsData(this, currentPage, typeId, searchContent, otherSift, status, filterPopWin) {
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


}