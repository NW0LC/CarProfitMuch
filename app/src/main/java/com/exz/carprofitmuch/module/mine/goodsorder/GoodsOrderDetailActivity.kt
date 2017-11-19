package com.exz.carprofitmuch.module.mine.goodsorder

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsOrderAdapter
import com.exz.carprofitmuch.adapter.ItemGoodsOrderAdapter
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.widget.MyWebActivity
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Title
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Url
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_my_order_detail.*
import kotlinx.android.synthetic.main.lay_goods_detail_text.view.*
import kotlinx.android.synthetic.main.lay_goods_order_bt.*
import java.util.*

/**
 * Created by pc on 2017/11/15.
 * 订单详情
 */

class GoodsOrderDetailActivity : BaseActivity(), View.OnClickListener {

  private  var orderState = "1"
    lateinit var adapter: ItemGoodsOrderAdapter<GoodsBean>
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.mine_my_order_detail)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        toolbar.setNavigationOnClickListener { finish() }
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_my_order_detail

    private val arrayList2Item = ArrayList<GoodsBean>()
    override fun init() {
        super.init()
        initView()
        iniData()
        initClick()
    }

    private fun initClick() {
        tv_left.setOnClickListener(this)
        tv_mid.setOnClickListener(this)
        tv_right.setOnClickListener(this)

    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val lay1 = LayoutInflater.from(mContext).inflate(R.layout.lay_goods_detail_text, RelativeLayout(mContext), false)
        val lay2 = LayoutInflater.from(mContext).inflate(R.layout.lay_goods_detail_text, RelativeLayout(mContext), false)
        lay1.tv_key.text = "商品总额"
        lay1.tv_value.text = "￥ 100"
        lay2.tv_key.text = "订单编号:"
        lay2.tv_value.text = "212412132121"
        ll_lay1.addView(lay1)
        ll_lay2.addView(lay2)
        lay1.tv_key.width = 1
        adapter = ItemGoodsOrderAdapter()
        arrayList2Item.add(GoodsBean())
        GoodsOrderAdapter.initStateBtn(orderState, tv_order_type, tv_left, tv_mid, tv_right)
        adapter = ItemGoodsOrderAdapter()
        adapter.setNewData(arrayList2Item)
        adapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 15, ContextCompat.getColor(mContext, R.color.app_bg)))
        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {

                startActivity(Intent(mContext, GoodsOrderDetailActivity::class.java))
            }

        })
    }


    private fun iniData() {
        DataCtrlClassXZW.OrderDetailData(mContext) {
            refreshLayout?.finishRefresh()
            if (it != null) {
                adapter.setNewData(it.goodsBeanData)
                adapter.loadMoreEnd()
            }
        }
    }

    /**         btLeft    btMid     btRight
     * 1待付款     【联系商家   取消订单  支付订单】
     * 2待收货     【联系商家   查看物流  确认收货】
     * 3待评价     【联系商家   删除订单  评价订单】
     * 4已结束     【联系商家   删除订单  】
     * 其他
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_left -> {
                DialogUtils.Call(this, "110")
            }
            R.id.tv_mid -> {
                when (orderState) {
                    "1" -> {//取消订单
                        DataCtrlClassXZW.CancelOrderDetailData(mContext, "", {
                            if (it != null) {
                                iniData()
                            }
                        })
                    }
                    "2" -> {//查看物流
                        startActivity(Intent(mContext, MyWebActivity::class.java).putExtra(Intent_Url, "http://www.baidu.com").putExtra(Intent_Title, "http://www.baidu.com"))
                    }
                    "3", "4" -> {    //删除订单
                        DataCtrlClassXZW.DeleteOrderDetailData(mContext, "", {
                            if (it != null) {
                                finish()
                            }
                        })
                    }

                }


            }
            R.id.tv_right -> {
                when (orderState) {
                    "1" -> {//支付订单
//                        startActivity(Intent(mContext,MyWebActivity::class.java).putExtra("orderId","1"))

                    }
                    "2" -> {//确认收货
                        DataCtrlClassXZW.ConfirmOrderDetailData(mContext, "", {
                            if (it != null) {
                                finish()
                            }
                        })
                    }
                    "3", "4" -> {    //评价订单
//                        startActivity(Intent(mContext,MyWebActivity::class.java).putExtra(Intent_Url,"http://www.baidu.com").putExtra(Intent_Title,"http://www.baidu.com"))

                    }

                }
            }
        }
    }
}
