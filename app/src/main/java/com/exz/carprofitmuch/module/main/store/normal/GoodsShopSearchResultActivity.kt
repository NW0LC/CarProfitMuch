package com.exz.carprofitmuch.module.main.store.normal

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsShopAdapter
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.GoodsShopClassifyBean
import com.exz.carprofitmuch.module.main.store.service.ServiceShopActivity
import com.exz.carprofitmuch.pop.GoodsShopClassifyPop
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_goods_shop_serach_result.*
import razerdp.basepopup.BasePopupWindow
import java.util.*



/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class GoodsShopSearchResultActivity : BaseActivity(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: GoodsShopAdapter<GoodsBean>
    private var isPriceUp = false  // 是否升序
    private lateinit var goodsShopClassifyPop: GoodsShopClassifyPop

    override fun initToolbar(): Boolean {
        toolbar.setContentInsetsAbsolute(0, 0)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 45f)
        SZWUtils.setMargin(header, 45f)

        mTitle.postDelayed({val isShowSoft = intent.getBooleanExtra("isShowSoft", false)
            if (isShowSoft) {
                mTitle.isFocusable = true
                mTitle.isFocusableInTouchMode = true
                mTitle.requestFocus()
                val inputManager = mTitle.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(mTitle, 0)
            }},200)
        mTitle.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // do something
                val searchContent = mTitle.text.toString().trim { it <= ' ' }
                if (!TextUtils.isEmpty(searchContent)) {
                    refreshLayout.autoRefresh()
                }
                return@OnEditorActionListener true
            }
            false
        })
        return false
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                mRecyclerView.isFocusable = true
                mRecyclerView.isFocusableInTouchMode = true
                mRecyclerView.requestFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && (v is EditText)) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_goods_shop_serach_result

    override fun init() {
        goodsShopClassifyPop = GoodsShopClassifyPop(this)
        val arrayList = ArrayList<GoodsShopClassifyBean>()
        val classifyBean = GoodsShopClassifyBean("1")
        classifyBean.name = "分类1"
        classifyBean.list.add(GoodsShopClassifyBean("2", "子类1"))
        classifyBean.list.add(GoodsShopClassifyBean("3", "子类2"))
        classifyBean.list.add(GoodsShopClassifyBean("4", "子类3"))
        val classifyBean2 = GoodsShopClassifyBean("5")
        classifyBean2.name = "分类2"
        classifyBean2.list.add(GoodsShopClassifyBean("6", "子类1"))
        classifyBean2.list.add(GoodsShopClassifyBean("7", "子类2"))
        classifyBean2.list.add(GoodsShopClassifyBean("8", "子类3"))
        arrayList.add(GoodsShopClassifyBean("9", "全部宝贝"))
        arrayList.add(GoodsShopClassifyBean("10", "新品首发"))
        arrayList.add(classifyBean)
        arrayList.add(GoodsShopClassifyBean("11", "新品首发"))
        arrayList.add(classifyBean2)
        goodsShopClassifyPop.data = arrayList
        goodsShopClassifyPop.onDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                if (GoodsShopClassifyPop.shopClassifyId.isNotEmpty())
                    refreshLayout.autoRefresh()
            }
        }
        initRecycler()
        initEvent()
    }

    private fun initEvent() {

        mLeftImg.setOnClickListener(this)
        mRightImg.setOnClickListener(this)
        bt_sort1.setOnClickListener(this)
        bt_sort2.setOnClickListener(this)
        bt_sort3.setOnClickListener(this)
    }

    private fun initRecycler() {
        mAdapter = GoodsShopAdapter()
        val arrayList = ArrayList<GoodsBean>()
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())
        arrayList.add(GoodsBean())

        mAdapter.setNewData(arrayList)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.isFocusable = false

        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(headerView: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                header.visibility = View.VISIBLE
            }

            override fun onHeaderReleasing(headerView: RefreshHeader?, percent: Float, offset: Int, footerHeight: Int, extendHeight: Int) {
                if (offset == 0)
                    header.visibility = View.GONE
            }
        })
        refreshLayout.setOnRefreshListener(this)

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                startActivity(Intent(this@GoodsShopSearchResultActivity, ServiceShopActivity::class.java))
            }
        })
    }


    override fun onClick(p0: View?) {
        when (p0) {
            bt_sort1 -> {
                isPriceUp = false
                bt_sort1.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                bt_sort2.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                bt_sort2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.vector_goods_shop_search_result_price_off), null)
                bt_sort3.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                refreshLayout.autoRefresh()
            }
            bt_sort2 -> {
                isPriceUp = !isPriceUp
                if (isPriceUp) {
                    bt_sort1.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                    bt_sort2.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                    bt_sort2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.vector_goods_shop_search_result_price_up), null)
                    bt_sort3.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                } else {
                    bt_sort1.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                    bt_sort2.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                    bt_sort2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.vector_goods_shop_search_result_price_down), null)
                    bt_sort3.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                }
                refreshLayout.autoRefresh()
            }
            bt_sort3 -> {
                isPriceUp = false
                bt_sort1.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                bt_sort2.setTextColor(ContextCompat.getColor(mContext, R.color.MaterialGrey600))
                bt_sort2.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.drawable.vector_goods_shop_search_result_price_off), null)
                bt_sort3.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                refreshLayout.autoRefresh()
            }
            mLeftImg -> {
                onBackPressed()
            }
            mRightImg -> {
                goodsShopClassifyPop.showPopupWindow()
            }
            else -> {
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
        DataCtrlClass.searchGoodsShopResult(this, currentPage) {
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
}