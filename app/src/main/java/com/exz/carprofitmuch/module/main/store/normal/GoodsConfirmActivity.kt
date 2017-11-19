package com.exz.carprofitmuch.module.main.store.normal

import android.support.v4.content.ContextCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.blankj.utilcode.util.EncryptUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsConfirmAdapter
import com.exz.carprofitmuch.adapter.GoodsConfirmBean
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.GoodsConfirmSubBean
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.utils.DialogUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_goods_confirm.*
import kotlinx.android.synthetic.main.layout_address.*
import java.text.DecimalFormat
import java.util.HashMap

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class GoodsConfirmActivity : BaseActivity(),  View.OnClickListener {
    lateinit var mAdapter: GoodsConfirmAdapter<GoodsConfirmSubBean>
    lateinit var data :GoodsConfirmBean
    val format= DecimalFormat("0.00")
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.goods_confirm_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)


        return false
    }

    override fun setInflateId(): Int= R.layout.activity_goods_confirm

    override fun init() {
        val scoreConfirmAddressDetail = getString(R.string.score_confirm_address_detail)
        val msp = SpannableString(scoreConfirmAddressDetail + "更换当前号码将从手机发送一条普通短信进行验证")
        msp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.MaterialGrey700)), 0, scoreConfirmAddressDetail.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_address.text = msp



        initEvent()
        initRecycler()

    }

    private fun initRecycler() {
        mAdapter = GoodsConfirmAdapter()
        val arrayList = ArrayList<GoodsConfirmSubBean>()
        mAdapter.setNewData(arrayList)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.isFocusable = false
        mRecyclerView.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                when (view.id) {
                    R.id.minus -> if (mAdapter.data[position].goodsCount.toInt() > 1) {
                        changeCount(mAdapter.data[position].goods[0], (mAdapter.data[position].goodsCount.toInt() - 1).toLong()){}
                    }
                    R.id.add -> changeCount(mAdapter.data[position].goods[0], (mAdapter.data[position].goodsCount.toInt() + 1).toLong()){}
                    R.id.count -> DialogUtils.changeNum(mContext, mAdapter.data[position].goodsCount.toLong()){
                        changeCount(mAdapter.data[position].goods[0], it){}
                    }
                    R.id.bt_coupon-> {
                        //更改优惠券。刷新数据
                    }
                    R.id.bt_sendWay-> {
                        //更改配送方式。刷新数据
                    }
                    else -> {
                    }
                }
            }
        })
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        bt_choose_type.setOnClickListener(this)
        bt_confirm.setOnClickListener(this)
    }

    /**
     * 计算总价格
     * return 总价格
     */
    fun initData(data :GoodsConfirmBean):String{
        //总价格
        var totalPrice=0.toDouble()
        for (item in data.goodsConfirmSubs) {
            //计算单组商品总价格
            var itemPrice= item.goods.sumByDouble { it.goodsCount.toDouble()* it.price.toDouble() }
            //除去优惠券金额
            val couponPrice= item.goodsCoupons.first { it.isSelect }.couponPrice.toDouble()
            item.coupon= item.goodsCoupons.first { it.isSelect }.toString(this)
            itemPrice-= couponPrice
            //加上运费
            val sendPrice=item.sendWays.first { it.isSelect }.expressPrice.toDouble()
            item.sendWay=item.sendWays.first { it.isSelect }.toString(this)
            itemPrice+= sendPrice

            //避免负数
            itemPrice=if (itemPrice<0) 0.toDouble() else itemPrice

            //给一组总价格赋值
            item.totalPrice=itemPrice
            //给一组商品数量赋值
            item.goodsCount=item.goods.sumBy { it.goodsCount.toInt() }.toString()

            //计入总价
            totalPrice+=itemPrice
        }
        //减去积分对应的金额
        totalPrice-=if (data.score.isSelect) data.score.scale.toDouble()*data.score.score.toDouble() else 0.toDouble()

        //避免负数
        totalPrice=if (totalPrice<0) 0.toDouble() else totalPrice

        //给总价格赋值
        tv_totalPrice.text=format.format(totalPrice)

        return format.format(totalPrice)
    }

    /**
     * 更改 商品数量
     * @param goodsEntity 商品
     */
    fun changeCount(goodsEntity: GoodsBean, index: Long, listener: (data: String?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("goodsCount", String.format("%s", index))
        params.put("goodsId", goodsEntity.id)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(this) {
                    override fun onSuccess(response: Response<NetEntity<String>>) =
                            if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                                goodsEntity.goodsCount = String.format("%s", index)
                                initData(data)
                                mAdapter.notifyDataSetChanged()
                                listener.invoke(response.body().data)
                            } else {
                                listener.invoke(null)
                            }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })

    }
    override fun onClick(p0: View?) {
        when (p0) {
            bt_confirm -> {

            }

        }
    }

}