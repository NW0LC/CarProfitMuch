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
import com.exz.carprofitmuch.adapter.ItemMainCartAdapter
import com.exz.carprofitmuch.adapter.MainCartAdapter
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.GoodsCarBean
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.module.main.store.normal.GoodsConfirmActivity
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type_Delete
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type_Edit
import com.exz.carprofitmuch.utils.DialogUtils
import com.exz.carprofitmuch.utils.SZWUtils
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
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        initRecycler()

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
        val arrayList = ArrayList<GoodsCarBean>()

        val element = GoodsCarBean()
        val element2 = GoodsCarBean()
        element.goods.add(GoodsBean("1", "2", price = "20"))
        element.goods.add(GoodsBean("3", "4", price = "21"))
        element2.goods.add(GoodsBean("5", "6", price = "22"))
        element2.goods.add(GoodsBean("7", "8", price = "23"))

        arrayList.add(element)
        arrayList.add(element2)

        mAdapter.setNewData(arrayList)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(context, R.color.app_bg)))
        mRecyclerView.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
               mRecyclerView.post {  val goodsCarBean = mAdapter.data[position]
                   goodsCarBean.isCheck = !goodsCarBean.isCheck
                   for (goodsBean in goodsCarBean.goods) {  //设置某个店铺下的商品选中状态
                       goodsBean.isCheck = goodsCarBean.isCheck
                   }
                   mAdapter.notifyItemChanged(position)
                   setAllPrice()
                   checkSelectAll(mAdapter, select_all) }
            }
        })
    }


    override fun onClick(p0: View?) {
        when (p0) {
            select_all -> {//全选
                mAdapter.data
                        .flatMap {
                            it.isCheck = select_all.isChecked
                            it.goods
                        }
                        .forEach { it.isCheck = select_all.isChecked }
                mAdapter.notifyDataSetChanged()
                setAllPrice()
            }
            actionView -> {//编辑
                when (if (Edit_Type == "-1") Edit_Type_Edit else Edit_Type) {
                    Edit_Type_Edit -> {
                        priceLay.visibility = View.INVISIBLE
                        actionView.text = getString(R.string.favorite_goods_confirm)
                        Edit_Type = Edit_Type_Delete
                        buyNow.text = getString(R.string.favorite_goods_delete)
                        refreshLayout.isEnableRefresh = false
                    }
                    Edit_Type_Delete -> {
                        priceLay.visibility = View.VISIBLE
                        actionView.text = getString(R.string.favorite_goods_edit)
                        Edit_Type = Edit_Type_Edit
                        buyNow.text = String.format(getString(R.string.cart_confirm), selectCount)
                        refreshLayout.isEnableRefresh = true
                    }
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
                } else {
                    mAdapter.addData(it)

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
     * 购物车更改 商品数量
     * @param goodsEntity 商品
     */
    fun cartChangeCount(goodsEntity: GoodsBean, index: Long, listener: (data: String?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("goodsCount", String.format("%s", index))
        params.put("goodsCarId", goodsEntity.getGoodsCarId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.url)
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
            for (goodsBean in goodsCarBean.goods) {
                if (goodsBean.isCheck) {
                    goodsBeans.add(goodsBean)
                    b = true
                }
            }
            if (goodsBeans.size > 0) {
                val newGoodsCarBean = GoodsCarBean()
                newGoodsCarBean.goods.addAll(goodsBeans)
                goodsCarBeans.add(newGoodsCarBean)
            }
        }
        if (Edit_Type == Edit_Type_Delete) {//删除
            if (b) {

                DialogUtils.delete(context) {
                    goodsCarBeans.forEach {
                        //                        deleteCar(context, it.goods) {
                        removeItem(this, mAdapter, null, it.goods)
                        if (mAdapter.data.size <= 0) {
                            checkSelectAll(mAdapter, select_all)

                        }
//                        }
                    }

                }
            } else {
                toast("选择要删除的商品")
            }
        } else {//结算
            if (b) {

                val intent = Intent(context, GoodsConfirmActivity::class.java)
//                intent.putExtra(Intent_GoodsCar_GoodsCarId, goodsCarIds.substring(0, goodsCarIds.length - 1))
                startActivityForResult(intent, 100)
            }
        }

    }


    /**
     * 设置总计价格
     */
    fun setAllPrice() {
        selectCount = 0
        val price = mAdapter.data.flatMap { it.goods }.filter { it.isCheck }.sumByDouble {
            selectCount++
            (if (it.price.isEmpty()) "0" else it.price).toDouble() * (if (it.goodsCount.isEmpty()) "0" else it.goodsCount).toInt()
        }
        assignPriceAndCount(price)
        footer_bar.visibility = if (mAdapter.data.size > 0) View.VISIBLE else View.GONE
    }
    /**
     * 给价格和货物数量赋值
     */
    private fun assignPriceAndCount(price:Double) {
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
        fun itemSelect(mAdapter: MainCartAdapter<*>, subAdapter: ItemMainCartAdapter<*>, selectAll: CheckBox, parentPosition: Int, position: Int) {
            val goodsCarBean = mAdapter.data[parentPosition]
            val goodsBean = subAdapter.data[position]
            goodsBean.isCheck = !goodsBean.isCheck
            subAdapter.notifyItemChanged(position)
            if (goodsCarBean.isCheck != goodsCarBean.goods.none { !it.isCheck }) {
                goodsCarBean.isCheck = !goodsCarBean.isCheck
                mAdapter.notifyItemChanged(parentPosition)
            }

            checkSelectAll(mAdapter, selectAll)
        }

        /**
         * @param view 是否全选
         */
        fun checkSelectAll(mAdapter: MainCartAdapter<*>, view: CheckBox) {
            val b = mAdapter.data.flatMap { it.goods }.none { !it.isCheck }
            view.isChecked = b
        }


        /**
         * @param context       上下文
         * type 0,删除；1，添加
         * @param goodsEntities 实体
         */
        fun deleteCar(context: Context, goodsEntities: ArrayList<GoodsBean>, onDeleteFinishListener: (scoreRecordBean: List<GoodsBean>) -> Unit) {
            addOrDelete(context, "0", "", "", goodsEntities, onDeleteFinishListener)
        }

        /**
         * @param context       上下文
         * type 0,删除；1，添加
         * @param goodsEntities 实体
         */
        fun addCar(context: Context, poolId: String, goodsCount: String, goodsEntities: List<GoodsBean>) {
            addOrDelete(context, "1", poolId, goodsCount, goodsEntities, null)
        }

        /**
         * @param context                     上下文
         * @param type                     0,删除；1，添加
         * @param onDeleteFinishListener    是否删除成功
         * @param goodsEntities            实体
         */
        private fun addOrDelete(context: Context, type: String, poolId: String, goodsCount: String, goodsEntities: List<GoodsBean>, onDeleteFinishListener: ((goodsEntities: List<GoodsBean>) -> Unit)?) {
            var goodsIds = ""
            for (goodsEntity in goodsEntities) {
                goodsIds += (if ("0" == type) goodsEntity.getGoodsCarId else goodsEntity.id) + ","
            }
            val map = HashMap<String, String>()
            map.put("userId", MyApplication.loginUserId)
            map.put("type", type)
            map.put("poolKey", if (TextUtils.isEmpty(poolId)) "0" else poolId)
            map.put("goodsCount", goodsCount)
            map.put(if ("0" == type) "goodsCarIds" else "goodsId", goodsIds.substring(0, goodsIds.length - 1))
            map.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
            OkGo.post<NetEntity<String>>(Urls.url).tag(context)
                    .params(map)
                    .execute(object : DialogCallback<NetEntity<String>>(context) {
                        override fun onSuccess(response: Response<NetEntity<String>>) {
                            onDeleteFinishListener?.invoke(goodsEntities)
                        }

                    })

        }


        /**
         * @param context       上下文
         * @param adapter       适配器
         * @param goodsEntities 移除
         */
        fun removeItem(context: CartFragment, adapter: MainCartAdapter<out GoodsCarBean>, subAdapter: ItemMainCartAdapter<*>?, goodsEntities: List<GoodsBean>) {
            val iterator = adapter.data.iterator()
            while (iterator.hasNext()) {
                val temp = iterator.next()
                val parentPosition = adapter.data.indexOf(temp)


                val subIterator = temp.goods.iterator()
                while (subIterator.hasNext()) {
                    val subTemp = subIterator.next()
                    for (goodsEntity in goodsEntities) {
                        if (subTemp.getGoodsCarId == goodsEntity.getGoodsCarId) {
                            val position = adapter.data[parentPosition].goods.indexOf(subTemp)
                            subIterator.remove()
                            if (subAdapter == null) {
                                adapter.notifyDataSetChanged()
                            } else {
                                subAdapter.notifyItemRemoved(position)
                            }
                        }
                    }
                }
                if (temp.goods.size <= 0) {
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