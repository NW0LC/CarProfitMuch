package com.exz.carprofitmuch.module.main.store.normal

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.EncryptUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsClassifyOneAdapter
import com.exz.carprofitmuch.adapter.GoodsClassifyTwoAdapter
import com.exz.carprofitmuch.bean.GoodsClassifyOneEntities
import com.exz.carprofitmuch.bean.GoodsClassifyTwoEntities
import com.exz.carprofitmuch.config.Urls.SubTypeList
import com.exz.carprofitmuch.config.Urls.TypeList
import com.exz.carprofitmuch.module.main.store.search.SearchGoodsActivity
import com.exz.carprofitmuch.module.main.store.search.SearchGoodsActivity.Companion.Intent_isShowSoft
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.RecycleViewDivider
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_goods_classify.*


/**
 * Created by 史忠文
 * on 2017/11/24.
 */
class GoodsClassifyActivity : BaseActivity() {
    lateinit var mGoodsClassifyOneAdapter: GoodsClassifyOneAdapter
    lateinit var mGoodsClassifyTwoAdapter: GoodsClassifyTwoAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }
        mTitle.text = getString(R.string.goods_classify_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        toolbar.inflateMenu(R.menu.menu_favorite_goods)
        val actionView = toolbar.menu.getItem(0).actionView
        (actionView as TextView).text = getString(R.string.goods_classify_search)
        actionView.setOnClickListener {
            intent.setClass(this@GoodsClassifyActivity, SearchGoodsActivity::class.java)
            intent.putExtra(Intent_isShowSoft, false)
            startActivity(intent)
        }
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_goods_classify

    override fun init() {
        initRecycler()
        oneLevel()
    }

    private fun initRecycler() {
        mGoodsClassifyOneAdapter = GoodsClassifyOneAdapter()
        linearLayoutManager = LinearLayoutManager(mContext)
        mRecyclerViewOne.layoutManager = linearLayoutManager
        mRecyclerViewOne.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(mContext, R.color.White)))
        mGoodsClassifyOneAdapter.bindToRecyclerView(mRecyclerViewOne)
        mRecyclerViewOne.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                val mGoodsClassifyOneEntities = adapter.data[position] as GoodsClassifyOneEntities
                mGoodsClassifyOneAdapter.setSelect(position)
                moveToPosition(linearLayoutManager, mRecyclerViewOne, position)
                adapter.notifyDataSetChanged()
                twoLevel(mGoodsClassifyOneEntities.typeId)
            }
        })

        // 2级分类
        mGoodsClassifyTwoAdapter = GoodsClassifyTwoAdapter()
        mRecyclerViewTwo.layoutManager = LinearLayoutManager(mContext)
        mRecyclerViewTwo.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, 1, ContextCompat.getColor(mContext, R.color.White)))
        mGoodsClassifyTwoAdapter.bindToRecyclerView(mRecyclerViewTwo)
    }

    private fun oneLevel() {
        val params = HashMap<String, String>()
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("FirstTypeList", salt).toLowerCase())
        OkGo.post<NetEntity<List<GoodsClassifyOneEntities>>>(TypeList).tag(this)
                .params(params)
                .execute(object : DialogCallback<NetEntity<List<GoodsClassifyOneEntities>>>(this) {
                    override fun onSuccess(response: Response<NetEntity<List<GoodsClassifyOneEntities>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            mGoodsClassifyOneAdapter.setNewData(response.body()?.data)
                            if (response.body()?.data?.size ?: 0 > 0) {
                                twoLevel(response.body()?.data?.get(0)?.typeId ?: "")
                            }
                        }
                    }
                })
    }

    private fun twoLevel(typeId: String) {
        val params = HashMap<String, String>()
        params.put("typeId", typeId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(typeId, salt).toLowerCase())
        OkGo.post<NetEntity<List<GoodsClassifyTwoEntities>>>(SubTypeList).tag(this)
                .params(params)
                .execute(object : DialogCallback<NetEntity<List<GoodsClassifyTwoEntities>>>(this) {
                    override fun onSuccess(response: Response<NetEntity<List<GoodsClassifyTwoEntities>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            mGoodsClassifyTwoAdapter.setNewData(response.body().data)
                        }
                    }

                })
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    fun moveToPosition(manager: LinearLayoutManager, mRecyclerView: RecyclerView,
                       n: Int) {

        val firstItem = manager.findFirstVisibleItemPosition()
        val lastItem = manager.findLastVisibleItemPosition()
        when {
            n <= firstItem -> mRecyclerView.scrollToPosition(n)
            n <= lastItem -> {
                val top = mRecyclerView.getChildAt(n - firstItem).top
                mRecyclerView.scrollBy(0, top)
            }
            else -> mRecyclerView.scrollToPosition(n)
        }
    }

}