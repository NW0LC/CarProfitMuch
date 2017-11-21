package com.exz.carprofitmuch.module.mine.returngoods

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ItemOrderCommentImageAdapter
import com.exz.carprofitmuch.module.mine.RefundActivity
import com.exz.carprofitmuch.utils.SZWUtils
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_retunr_goods_detail.*
import kotlinx.android.synthetic.main.item_item_goods_order.*
import kotlinx.android.synthetic.main.lay_goods_order_bt.*
import kotlinx.android.synthetic.main.lay_return_goods_num.*

/**
 * Created by pc on 2017/11/20.
 * 退款退货订单详情
 */

class ReturnGoodsDetailActivity : BaseActivity(), View.OnClickListener {


    var orderState = "2"
    lateinit var mAdapter: ItemOrderCommentImageAdapter
    var photos = ArrayList<String>()
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        mTitle.text = getString(R.string.mine_return_goods_detail)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, header)
        SZWUtils.setPaddingSmart(scrollView, 10f)
        toolbar.setNavigationOnClickListener { finish() }
        return false
    }

    override fun setInflateId(): Int {
        return R.layout.activity_retunr_goods_detail
    }

    override fun init() {
        super.init()
        initView()
        initEvent()
        initImgRecycler()
        initOrderDetail()

    }

    private fun initOrderDetail() {
    }


    private fun initView() {
        //订单类型
        when (intent.getStringExtra("orderType")) {
            "1" -> {//待处理
                tv_my_order.text = mContext.getString(R.string.mine_return_goodsbuy_pending)
            }
            "2" -> {//处理中
                tv_my_order.text = mContext.getString(R.string.mine_return_goodsbuy_processed)
            }
            "3" -> {//已完成
                tv_my_order.text = mContext.getString(R.string.mine_return_goodsbuy_completed)
            }
        }
        tv_left.visibility = View.VISIBLE
        when (orderState) {
            "1" -> {//待处理
                icon_warning.visibility = View.VISIBLE
                icon_warning.text = String.format(mContext.getString(R.string.mine_return_goods_platform_appeal1), "3");
                tv_left.text = "联系卖家"
                tv_mid.text = "平台申诉"
                tv_right.text = "取消退货"
            }
            "2" -> {//处理中 卖家已同意申请
                ll_lay_1.visibility = View.VISIBLE
                tv_left.text = "联系卖家"
                tv_mid.text = "填写物流"
                tv_right.text = "取消退货"
            }
            "3" -> {//处理中 卖家拒绝申请
                ll_lay_1.visibility = View.VISIBLE
                ll_top_left_1.text = mContext.getString(R.string.mine_return_goodsbuy_refuse_appeal1)
                ll_content_1.text = mContext.getString(R.string.mine_return_goodsbuy_seller_appeal1)
                tv_left.text = "联系卖家"
                tv_mid.text = "平台申诉"
                tv_right.text = "取消退货"
            }
            "4" -> {//处理中 买家已发物流
                ll_lay_1.visibility = View.VISIBLE
                ll_lay_2.visibility = View.VISIBLE
                icon_warning.visibility = View.VISIBLE
                tv_left.text = "联系卖家"
                tv_mid.text = "填写物流"
                tv_right.text = "取消退货"
                icon_warning.text = String.format(mContext.getString(R.string.mine_return_goods_platform_appeal1), "7");
            }
            "5" -> {//处理中 买家已发物流 卖家拒绝退货
                ll_lay_1.visibility = View.VISIBLE
                ll_lay_2.visibility = View.VISIBLE
                ll_lay_3.visibility = View.VISIBLE
                tv_mid.visibility = View.GONE
                tv_left.text = "联系卖家"
                tv_right.text = "取消退货"
                ll_top_left_3.text = mContext.getString(R.string.mine_return_goodsbuy_refuse_appeal1)
                ll_content_3.text = mContext.getString(R.string.mine_return_goodsbuy_seller_appeal1)
            }
            "6" -> {//已完成
                ll_lay_1.visibility = View.VISIBLE
                ll_lay_2.visibility = View.VISIBLE
                ll_lay_3.visibility = View.VISIBLE
                llLay.visibility = View.GONE

                ll_lay_3.setBackgroundResource(R.color.colorPrimary)
                ll_top_left_3.text = mContext.getString(R.string.mine_return_goods_success)
                ll_content_3.text = mContext.getString(R.string.mine_return_goods_rejected)

                ll_top_left_3.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                ll_top_right_3.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                ll_content_3.setTextColor(ContextCompat.getColor(mContext, R.color.white))

            }
        }


    }

    private fun initEvent() {
        tv_left.setOnClickListener(this)
        tv_mid.setOnClickListener(this)
        tv_right.setOnClickListener(this)
        bt_refund.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_refund->{//申请退款
                startActivity(Intent(mContext, RefundActivity::class.java))
        }


            R.id.tv_left -> {//联系卖家
                DialogUtils.Call(this, "110")
            }
            R.id.tv_mid -> {
                when (orderState) {
                    "1", "3" -> {//平台申诉

                    }
                    "2", "4", "5" -> {//填写物流
                    }

                }

            }
            R.id.tv_right -> {//取消退货
                DataCtrlClassXZW.CancelOrderDetailData(this, "", {
                    if (it != null) {
                        finish()
                    }
                })
            }


        }

    }

    private fun initImgRecycler() {
        photos.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=216557540,268792798&fm=200&gp=0.jpg")
        photos.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=216557540,268792798&fm=200&gp=0.jpg")
        photos.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=216557540,268792798&fm=200&gp=0.jpg")
        photos.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=216557540,268792798&fm=200&gp=0.jpg")
        mAdapter = ItemOrderCommentImageAdapter()
        mAdapter.setNewData(photos)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = GridLayoutManager(mContext, 4)
        mRecyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.isFocusable = false
    }
}
