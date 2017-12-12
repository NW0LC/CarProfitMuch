package com.exz.carprofitmuch.module.mine.favorite

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.FavoriteShopAdapter
import com.exz.carprofitmuch.bean.GoodsShopBean
import com.exz.carprofitmuch.module.main.store.normal.GoodsShopActivity
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type_Delete
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type_Edit
import com.exz.carprofitmuch.utils.DialogUtils
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.fragment_favorite_goods.*

class FavoriteShopActivity : BaseActivity(), OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {
    override fun setInflateId(): Int = R.layout.activity_favorite_shop

    var refreshState = Constants.RefreshState.STATE_REFRESH
    var currentPage = 1
    lateinit var mAdapter: FavoriteShopAdapter<GoodsShopBean>
    lateinit var actionView: TextView
    private fun refresh() {
        if (Edit_Type == Edit_Type_Edit) {//正常状态
            lay_bottom.visibility = View.GONE
            refreshLayout.isEnableRefresh = true
        } else {//编辑状态
            lay_bottom.visibility = View.VISIBLE
            refreshLayout.isEnableRefresh = false
        }
        mAdapter.notifyDataSetChanged()
    }

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        initRecycler()
        initEvent()
        onRefresh(refreshLayout)
    }

    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }
        mTitle.text = getString(R.string.favorite_shop_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setMargin(this, header)

        toolbar.inflateMenu(R.menu.menu_favorite_goods)
        actionView = toolbar.menu.getItem(0).actionView as TextView
        actionView.text = getString(R.string.favorite_goods_edit)
        actionView.setOnClickListener(this)
        return false
    }

    private fun initRecycler() {
        mAdapter = FavoriteShopAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(this, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(this, R.color.app_bg)))

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(baseAdapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                when (if (Edit_Type == "-1") Edit_Type_Edit else Edit_Type) {
                    Edit_Type_Edit -> {
                        //跳转
                        val intent = Intent(this@FavoriteShopActivity, GoodsShopActivity::class.java)
                        intent.putExtra(GoodsShopActivity.GoodsShop_Intent_ShopId, mAdapter.data[position].id)
                        startActivity(intent)
                    }
                    Edit_Type_Delete -> {
                        //删除
                        mAdapter.data[position].isCheck = !mAdapter.data[position].isCheck
                        mAdapter.notifyItemChanged(position)
                        val b = mAdapter.data.none { !it.isCheck }
                        cb_check.isChecked = b
                    }
                }
            }
        })
    }


    fun initEvent() {
        cb_check.setOnClickListener(this)
        bt_delete.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            cb_check -> {//全选
                mAdapter.animatorEnable = false
                for (goodsBean in mAdapter.data) {
                    goodsBean.isCheck = cb_check.isChecked
                }
                mAdapter.notifyDataSetChanged()
                mRecyclerView.post {
                    mAdapter.animatorEnable = true
                }
            }
            bt_delete -> {//删除
                var b = false
                val goodsEntities = ArrayList<GoodsShopBean>()
                for (GoodsBean in mAdapter.data) {
                    if (GoodsBean.isCheck) {
                        goodsEntities.add(GoodsBean)
                        b = true
                    }
                }
                if (b) {
                    DialogUtils.delete(this) {

                        DataCtrlClass.favoriteShopIsCollection(mContext, "0", "0", goodsEntities.toTypedArray()) {
                            removeItem(this, mAdapter, it)
                        }
                    }
                }
            }
            actionView -> {
                when (if (Edit_Type == "-1") Edit_Type_Edit else Edit_Type) {
                    Edit_Type_Edit -> {
                        actionView.text = getString(R.string.favorite_goods_confirm)
                        Edit_Type = Edit_Type_Delete
                    }
                    Edit_Type_Delete -> {
                        actionView.text = getString(R.string.favorite_goods_edit)
                        Edit_Type = Edit_Type_Edit
                    }
                }
                actionView.isClickable = false
                actionView.postDelayed({ actionView.isClickable = true }, 500)
                refresh()
            }
            else -> {
            }
        }
    }

    private fun initData() {
        DataCtrlClass.favoriteShopListData(this, currentPage) {
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

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        currentPage = 1
        refreshState = Constants.RefreshState.STATE_REFRESH
        initData()

    }

    override fun onLoadMoreRequested() {
        refreshState = Constants.RefreshState.STATE_LOAD_MORE
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        Edit_Type = "-1"
    }

    companion object {
        /**
         * @param adapter       适配器
         * @param goodsEntities 移除
         */
        fun removeItem(context: FavoriteShopActivity, adapter: FavoriteShopAdapter<out GoodsShopBean>, goodsEntities: Array<out GoodsShopBean>?) {
            if (goodsEntities == null) {
                return
            }
            val iterator = adapter.data.iterator()
            while (iterator.hasNext()) {
                val temp = iterator.next()
                for (goodsEntity in goodsEntities) {
                    if (temp.id == goodsEntity.id) {
                        val position = adapter.data.indexOf(temp)
                        iterator.remove()
                        if (adapter.data.size <= 0) {
                            adapter.notifyDataSetChanged()
                            if (Edit_Type == Edit_Type_Delete)
                            context.onClick(context.actionView)
                        } else
                            adapter.notifyItemRemoved(position)
                    }
                }

            }
        }
    }

}