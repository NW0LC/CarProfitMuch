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
        if(intent.getStringExtra("className").equals("店铺类型")){
            data.add(OpenShopKeyValueBean("实体商品类", "1", false))
            data.add(OpenShopKeyValueBean("虚拟服务类", "2", false))
            adapter.setNewData(data)
            var entity = intent.getSerializableExtra("keyValue") as OpenShopKeyValueBean
            for (bean in adapter.data) {
                if (entity.title.equals(bean.title)) {
                    adapter.data.get(adapter.data.indexOf(bean)).check = true
                    adapter.notifyItemChanged(adapter.data.indexOf(bean))
                    break
                }
            }
        }else  if(intent.getStringExtra("className").equals("店铺类目")){
            initShopCategory()
        }


        adapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.app_bg)))
//        adapter.setOnLoadMoreListener(this, mRecyclerView)






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

    private fun initShopCategory() {
        DataCtrlClassXZW.ShopCategoryData(mContext,intent.getStringExtra("classMark"),{
            if(it!=null){
                adapter.setNewData(it)
                adapter.loadMoreEnd()

                var entity = intent.getSerializableExtra("keyValue") as OpenShopKeyValueBean
                for (bean in adapter.data) {
                    if (entity.title.equals(bean.title)) {
                        adapter.data.get(adapter.data.indexOf(bean)).check = true
                        adapter.notifyItemChanged(adapter.data.indexOf(bean))
                        break
                    }
                }
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

    }
