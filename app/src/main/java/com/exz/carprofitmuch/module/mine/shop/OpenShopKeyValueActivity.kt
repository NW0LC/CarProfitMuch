package com.exz.carprofitmuch.module.mine.shop

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.OpenShopkeyValueAdapter
import com.exz.carprofitmuch.bean.OpenShopKeyValueBean
import com.exz.carprofitmuch.module.mine.shop.OpenShopActivity.Companion.RESULTCODE_OPEN_SHOP
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_open_list.*

/**
 * Created by pc on 2017/11/23.
 *
 */

class OpenShopKeyValueActivity : BaseActivity() {
    //    private var refreshState = Constants.RefreshState.STATE_REFRESH
//    private var currentPage = 1
    private val data = ArrayList<OpenShopKeyValueBean>()
    private lateinit var adapter: OpenShopkeyValueAdapter
    override fun initToolbar(): Boolean {
        mTitle.text = intent.getStringExtra("className")
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        return false
    }

    override fun setInflateId(): Int {
        return R.layout.activity_open_list
    }

    override fun init() {
        super.init()
        initView()
    }

    private fun initView() {
//        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        adapter = OpenShopkeyValueAdapter()
        data.add(OpenShopKeyValueBean("电脑", "1", false))
        data.add(OpenShopKeyValueBean("数码", "2", false))
        data.add(OpenShopKeyValueBean("家用电器", "3", false))
        data.add(OpenShopKeyValueBean("外设", "4", false))
        data.add(OpenShopKeyValueBean("男装", "5", false))
        data.add(OpenShopKeyValueBean("运动鞋", "6", false))
        data.add(OpenShopKeyValueBean("个人护理", "7", false))
        data.add(OpenShopKeyValueBean("女装女鞋", "8", false))
        adapter.setNewData(data)
        adapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.app_bg)))
//        adapter.setOnLoadMoreListener(this, mRecyclerView)

        var entity = intent.getSerializableExtra("keyValue") as OpenShopKeyValueBean

        for (bean in adapter.data) {
            if (entity.id.equals(bean.id)) {
                adapter.data.get(adapter.data.indexOf(bean)).check = true
                adapter.notifyItemChanged(adapter.data.indexOf(bean))
                break
            }
        }


        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(a: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                adapter.data.get(position).check = true
                var b = Bundle()
                b.putSerializable("keyValue", adapter.data.get(position))
                setResult(RESULTCODE_OPEN_SHOP, Intent().putExtras(b))
                finish()
            }

            })

    }

//    override fun onRefresh(refreshLayout: RefreshLayout?) {
//        currentPage = 1
//        refreshState = Constants.RefreshState.STATE_REFRESH
//        iniData()
//
//    }
//
//    override fun onLoadMoreRequested() {
//        refreshState = Constants.RefreshState.STATE_LOAD_MORE
//        iniData()
//    }

                private fun iniData() {
            DataCtrlClassXZW.OpenInfoData(mContext, "") {
                refreshLayout?.finishRefresh()
                if (it != null) {
                    adapter.setNewData(it)
                    adapter.loadMoreEnd()

                } else {
                    adapter.loadMoreFail()
                }
            }
        }
    }
