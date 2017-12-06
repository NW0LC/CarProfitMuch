package com.exz.carprofitmuch.module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.blankj.utilcode.util.EncryptUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.MainCartAdapter
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.GoodsCarBean
import com.exz.carprofitmuch.bean.GoodsCarBean.Companion.TYPE_2
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.config.Urls.AddShopCar
import com.exz.carprofitmuch.config.Urls.DeleteShopCar
import com.exz.carprofitmuch.module.main.store.normal.GoodsConfirmActivity
import com.exz.carprofitmuch.module.main.store.normal.GoodsConfirmActivity.Companion.GoodsConfirm_Intent_shopInfo
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type_Delete
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type_Edit
import com.exz.carprofitmuch.utils.DialogUtils
import com.exz.carprofitmuch.utils.SZWUtils
import com.google.gson.Gson
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.RecycleViewDivider
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.fragment_main_cart.*
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.toast
import java.text.DecimalFormat
import java.util.HashMap
import kotlin.collections.ArrayList

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class CartFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: MainCartAdapter<GoodsCarBean>
    private var selectCount = 0
    private lateinit var actionView: TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_cart, container, false)
        return rootView
    }

    override fun initView() {
        initBar()
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        initRecycler()
        onRefresh(refreshLayout)
    }

    private fun initBar() {
        toolbar.navigationIcon = null
        mTitle.text = getString(R.string.main_cart_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(activity)
        StatusBarUtil.setPaddingSmart(activity, toolbar)
        StatusBarUtil.setPaddingSmart(activity, mRecyclerView)
        StatusBarUtil.setPaddingSmart(activity, blurView)
        StatusBarUtil.setMargin(activity, header)
        toolbar.inflateMenu(R.menu.menu_favorite_goods)
        actionView = toolbar.menu.getItem(0).actionView as TextView
        actionView.text = getString(R.string.favorite_goods_edit)
        actionView.setOnClickListener(this)
        assignPriceAndCount(0.toDouble())
    }

    override fun initEvent() {
        buyNow.setOnClickListener(this)
        select_all.setOnClickListener(this)
    }

    private fun initRecycler() {
        mAdapter = MainCartAdapter(this)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(context, R.color.app_bg)))
        mRecyclerView.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                when (view?.id) {
                    R.id.bt_clearGoods -> {
                        //清空失效商品
                    }
                    R.id.cb_goodsShop_name -> {
                        mRecyclerView.post {
                            val goodsCarBean = mAdapter.data[position]
                            goodsCarBean.isCheck = !goodsCarBean.isCheck
                            for (goodsBean in goodsCarBean.goodsInfo) {  //设置某个店铺下的商品选中状态
                                goodsBean.isCheck = goodsCarBean.isCheck
                            }
                            mAdapter.notifyItemChanged(position)
                            setAllPrice()
                            checkSelectAll(mAdapter, select_all)
                        }
                    }
                    else -> {
                    }
                }

            }
        })
    }


    override fun onClick(p0: View?) {
        when (p0) {
            select_all -> {//全选

                mAdapter.data
                        .flatMap {
                            it.isCheck = select_all.isChecked
                            it.goodsInfo
                        }
                        .forEach { it.isCheck = select_all.isChecked }
                mAdapter.notifyDataSetChanged()

                setAllPrice()
            }
            actionView -> {//编辑
                mAdapter.animatorEnable = true
                when (if (Edit_Type == "-1") Edit_Type_Edit else Edit_Type) {
                    Edit_Type_Edit -> {
                        priceLay.visibility = View.INVISIBLE
                        actionView.text = getString(R.string.favorite_goods_confirm)
                        Edit_Type = Edit_Type_Delete
                        buyNow.text = getString(R.string.favorite_goods_delete)
                        refreshLayout.isEnableRefresh = false

                        checkSelectAll(mAdapter, select_all)
                    }
                    Edit_Type_Delete -> {
                        priceLay.visibility = View.VISIBLE
                        actionView.text = getString(R.string.favorite_goods_edit)
                        Edit_Type = Edit_Type_Edit
                        buyNow.text = String.format(getString(R.string.cart_confirm), selectCount)
                        refreshLayout.isEnableRefresh = true

                        checkSelectAll(mAdapter, select_all)
                    }
                }
                mAdapter.notifyDataSetChanged()
                mRecyclerView.post {
                    mAdapter.animatorEnable = false
                }
                actionView.isClickable = false
                actionView.postDelayed({ actionView.isClickable = true }, 500)
            }
            else -> {
                buyOrDelete()
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

    /**
     * 获取购物车数据
     */
    private fun iniData() {
        DataCtrlClass.cartListData(context, currentPage) {
            refreshLayout?.finishRefresh()
            if (it != null) {
                if (refreshState == Constants.RefreshState.STATE_REFRESH) {
                    mAdapter.setNewData(it)
                    if (mAdapter.data.isNotEmpty())
                        setLoseGoods()
                } else {
                    if (it.isNotEmpty()) {
                        val data = mAdapter.data
//                        最后一条数据
                        var goodsCarBean = data.lastOrNull()
//                        最后一条是否为失效商品，是则取上一条 否 则取最后一条
                        goodsCarBean = data[if (goodsCarBean?.itemType == TYPE_2) data.lastIndex - 1 else data.lastIndex]
//                        如果上下匹配，合并到一起
                        if (it.first().shopId == goodsCarBean.shopId) {
                            goodsCarBean.goodsInfo.addAll(it.first().goodsInfo)
//                          匹配的话移除新增数组的第一条数据
                            it.removeAt(0)
                        }
                        mAdapter.addData(it)
//                        将所有失效商品取出来建立新集合  删除 每组没商品的空集合 移动至最后一组
                        setLoseGoods()

                    }

                }
                if (it.isNotEmpty()) {
                    mAdapter.loadMoreComplete()
                    currentPage++
                } else {
                    mAdapter.loadMoreEnd()
                }
                setAllPrice()
                checkSelectAll(mAdapter, select_all)
            } else {
                mAdapter.loadMoreFail()
            }
        }

    }

    /**
     * 设置过时商品
     */
    private fun setLoseGoods() {
        val goodsCarBeans = mAdapter.data.iterator()
        val newGoodsCarBean = GoodsCarBean()
        newGoodsCarBean.type = TYPE_2
        while (goodsCarBeans.hasNext()) {
            val goodsCarBean = goodsCarBeans.next()
            val goodsBeans = goodsCarBean.goodsInfo.iterator()
            while (goodsBeans.hasNext()) {
                val goodsBean = goodsBeans.next()
                newGoodsCarBean.goodsInfo = if (newGoodsCarBean.goodsInfo.isEmpty()) ArrayList() else newGoodsCarBean.goodsInfo
                if (goodsBean.isDelete== "1") {
                    newGoodsCarBean.goodsInfo.add(goodsBean)
                    goodsBeans.remove()
                }
            }
            if (!goodsCarBean.goodsInfo.isNotEmpty()) {
                goodsCarBeans.remove()
            }

        }
        if (newGoodsCarBean.goodsInfo.size!=0){
            mAdapter.data.add(newGoodsCarBean)
            mAdapter.notifyDataSetChanged()
        }
    }


    /**
     * 购物车更改 商品数量
     * @param goodsEntity 商品
     */
    fun cartChangeCount(goodsEntity: GoodsBean, index: Long, listener: (data: String?) -> Unit) {
//        userId	string	必填	用户id
//        shopCarId	string	必填	购物车id
//        goodsCount	string	必填	商品数量
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("goodsCount", String.format("%s", index))
        params.put("shopCarId", goodsEntity.shopCarId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+goodsEntity.shopCarId, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.EditShopCar)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) =
                            if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                                goodsEntity.goodsCount = String.format("%s", index)
                                mAdapter.notifyDataSetChanged()
                                setAllPrice()
                                listener.invoke(response.body().data)
                            } else {
                                listener.invoke(null)
                            }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)

                        goodsEntity.goodsCount = String.format("%s", index)
                        mAdapter.notifyDataSetChanged()
                        setAllPrice()
                    }

                })

    }

    /**
     * 删除与结算
     */
    private fun buyOrDelete() {
        var b = false
        val goodsCarBeans = ArrayList<GoodsCarBean>()
        for (goodsCarBean in mAdapter.data) {
            val goodsBeans = ArrayList<GoodsBean>()
            for (goodsBean in goodsCarBean.goodsInfo) {
                if (goodsBean.isCheck) {
                    goodsBeans.add(goodsBean)
                    b = true
                }
            }
            if (goodsBeans.size > 0) {
                val newGoodsCarBean = GoodsCarBean()
                newGoodsCarBean.shopId=goodsCarBean.shopId
                newGoodsCarBean.goodsInfo.addAll(goodsBeans)
                goodsCarBeans.add(newGoodsCarBean)
            }
        }
        if (Edit_Type == Edit_Type_Delete) {//删除
            if (b) {

                DialogUtils.delete(context) {
                    goodsCarBeans.forEach {
                        deleteCar(context, it.goodsInfo) {
                            removeItem(this, mAdapter, null, it)
                            if (mAdapter.data.size <= 0) {
                                checkSelectAll(mAdapter, select_all)

                            }
                        }
                    }

                }
            } else {
                toast("选择要删除的商品")
            }
        } else {//结算
            if (b) {

                val intent = Intent(context, GoodsConfirmActivity::class.java)
                intent.putExtra(GoodsConfirm_Intent_shopInfo, Gson().toJson(goodsCarBeans))
                startActivityForResult(intent, 100)
            }
        }

    }


    /**
     * 设置总计价格
     */
    fun setAllPrice() {
        selectCount = 0
        val price = mAdapter.data.flatMap { if (it.itemType == TYPE_2) ArrayList() else it.goodsInfo }.filter { it.isCheck }.sumByDouble {
            selectCount++
            (if (it.goodsPrice.isEmpty()) "0" else it.goodsPrice).toDouble() * (if (it.goodsCount.isEmpty()) "0" else it.goodsCount).toInt()
        }
        assignPriceAndCount(price)
        footer_bar.visibility = if (mAdapter.data.size > 0) View.VISIBLE else View.GONE
    }

    /**
     * 给价格和货物数量赋值
     */
    private fun assignPriceAndCount(price: Double) {
        val priceFormat = DecimalFormat("0.00")
        this.price.text = priceFormat.format(price)
        if (Edit_Type != Edit_Type_Delete)//如果是编辑状态就不用改变
            buyNow.text = String.format(getString(R.string.cart_confirm), selectCount)
    }

    override fun onDetach() {
        Edit_Type = "-1"
        super.onDetach()
    }

    companion object {
        fun newInstance(): CartFragment {
            val bundle = Bundle()
            val fragment = CartFragment()
            fragment.arguments = bundle
            return fragment
        }

        /**
         * 单选，是否全选
         *
         */
        fun itemSelect(mAdapter: MainCartAdapter<*>, subAdapter: BaseQuickAdapter<GoodsBean, *>, selectAll: CheckBox, parentPosition: Int, position: Int) {
            val goodsCarBean = mAdapter.data[parentPosition]
            val goodsBean = subAdapter.data[position]
            goodsBean.isCheck = !goodsBean.isCheck
            subAdapter.notifyItemChanged(position)
            if (goodsCarBean.isCheck != goodsCarBean.goodsInfo.none { !it.isCheck }) {
                goodsCarBean.isCheck = !goodsCarBean.isCheck
                mAdapter.notifyItemChanged(parentPosition)
            }

            checkSelectAll(mAdapter, selectAll)
        }

        /**
         * @param view 是否全选
         */
        fun checkSelectAll(mAdapter: MainCartAdapter<*>, view: CheckBox) {
            val b = mAdapter.data.flatMap { if (it.itemType == TYPE_2 && Edit_Type == Edit_Type_Edit) ArrayList() else it.goodsInfo }.none { !it.isCheck }
            view.isChecked = b
        }


        /**
         * @param context       上下文
         * type 0,删除；1，添加
         * @param goodsEntities 实体
         */
        fun deleteCar(context: Context, goodsEntities: ArrayList<GoodsBean>, onDeleteFinishListener: (scoreRecordBean: List<GoodsBean>) -> Unit) {
            addOrDelete(context, DeleteShopCar, "0", "", "", "", goodsEntities, onDeleteFinishListener)
        }

        /**
         * @param context       上下文
         * type 0,删除；1，添加
         * @param goodsEntities 实体
         */
        fun addCar(context: Context, shopId: String, poolId: String, goodsCount: String, goodsEntities: ArrayList<GoodsBean>) {
            addOrDelete(context, AddShopCar, "1", shopId, poolId, goodsCount, goodsEntities, null)
        }

        /**
         * @param context                     上下文
         * @param type                     0,删除；1，添加
         * @param onDeleteFinishListener    是否删除成功
         * @param goodsEntities            实体
         */
        private fun addOrDelete(context: Context, url: String, type: String, shopId: String, poolId: String, goodsCount: String, goodsEntities: List<GoodsBean>, onDeleteFinishListener: ((goodsEntities: List<GoodsBean>) -> Unit)?) {
//            userId	string	必填	用户id
//            goodsId	string	必填	商品id
//            shopId	string	必填	店铺id
//            skuid	string	必填	规格id
//            count	string	必填	数量(最少为1)
//            requestCheck	string	必填	验证请求


            var goodsIds = ""
            for (goodsEntity in goodsEntities) {
                goodsIds += (if ("0" == type) goodsEntity.shopCarId else goodsEntity.goodsId) + ","
            }
            val map = HashMap<String, String>()
            map.put("userId", MyApplication.loginUserId)
            if ("0" == type) {
                map.put("shopCarId", goodsIds.substring(0, goodsIds.length - 1))
                map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + goodsIds.substring(0, goodsIds.length - 1), salt).toLowerCase())
            } else {
                map.put("shopId", shopId)
                map.put("goodsId", goodsIds.substring(0, goodsIds.length - 1))
                map.put("skuid", if (TextUtils.isEmpty(poolId)) "0" else poolId)
                map.put("count", goodsCount)
                map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + goodsIds.substring(0, goodsIds.length - 1) + shopId, salt).toLowerCase())
            }

            OkGo.post<NetEntity<String>>(url).tag(context)
                    .params(map)
                    .execute(object : DialogCallback<NetEntity<String>>(context) {
                        override fun onSuccess(response: Response<NetEntity<String>>) {
                            context.toast(response.body().message)
                            onDeleteFinishListener?.invoke(goodsEntities)
                        }

                    })

        }


        /**
         * @param context       上下文
         * @param adapter       适配器
         * @param goodsEntities 移除
         */
        fun removeItem(context: CartFragment, adapter: MainCartAdapter<out GoodsCarBean>, subAdapter: BaseQuickAdapter<*, *>?, goodsEntities: List<GoodsBean>) {
            val iterator = adapter.data.iterator()
            while (iterator.hasNext()) {
                val temp = iterator.next()
                val parentPosition = adapter.data.indexOf(temp)


                val subIterator = temp.goodsInfo.iterator()
                while (subIterator.hasNext()) {
                    val subTemp = subIterator.next()
                    for (goodsEntity in goodsEntities) {
                        if (subTemp.shopCarId == goodsEntity.shopCarId) {
                            val position = adapter.data[parentPosition].goodsInfo.indexOf(subTemp)
                            subIterator.remove()
                            if (subAdapter == null) {
                                adapter.notifyDataSetChanged()
                            } else {
                                subAdapter.notifyItemRemoved(position)
                            }
                        }
                    }
                }
                if (temp.goodsInfo.size <= 0) {
                    iterator.remove()
                    if (adapter.data.size <= 0) {
                        adapter.notifyDataSetChanged()
                        context.onClick(context.actionView)
                    } else
                        adapter.notifyItemRemoved(parentPosition)
                }


            }
            context.setAllPrice()
        }
    }
}