package com.exz.carprofitmuch.adapter

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.R.id.state
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.ReturnGoodsBean
import com.exz.carprofitmuch.module.mine.InputLogisticsActivity
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import kotlinx.android.synthetic.main.item_return_goods.view.*
import kotlinx.android.synthetic.main.lay_goods_order_bt.view.*
import kotlinx.android.synthetic.main.lay_return_goods_num.view.*
import java.net.URLEncoder

/**
 * Created by pc on 2017/11/15.
 *
 */

class ReturnGoodsAdapter<T : ReturnGoodsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_return_goods, null) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val mAdapter = ItemGoodsOrderAdapter<GoodsBean>()
        mAdapter.bindToRecyclerView(helper.itemView.mRecyclerView)
        mAdapter.setNewData(item.goodsInfo)
        helper.itemView.mRecyclerView.isFocusable = false
        helper.itemView.mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        helper.addOnClickListener(R.id.tv_mid)
        helper.addOnClickListener(R.id.tv_right)
        when (item.returnOrderType) {
            "1", "3" -> {//退款
                helper.itemView.tv_refund_num.text = String.format(mContext.getString(R.string.return_priceOrderNum) + "%s", item.returnOrderId)
            }
            "2" -> {//退货
                helper.itemView.tv_refund_num.text = String.format(mContext.getString(R.string.return_goodsOrderNum) + "%s", item.returnOrderId)
            }
        }

        if (item.goodsInfo[0].payMark == "1")
            helper.itemView.tv_refund.text = String.format(mContext.getString(R.string.mine_return_price) + mContext.getString(R.string.SCORE), item.returnScore)
        else {
            helper.itemView.tv_refund.text = String.format(mContext.getString(R.string.mine_return_price), mContext.getString(R.string.CNY) + item.returnMoney)
        }
        initStateBtn(item.returnOrderState, item.returnOrderSubState, item.returnOrderType, helper.itemView.tv_my_order, helper.itemView.tv_mid, helper.itemView.tv_right)


    }

    companion object {

//        returnOrderSubState	"1{[1]仅退款,[2]卖家拒绝} 2{[1]仅退款,[2]卖家拒绝,[2]买家向平台申诉} 3{[2]退货退款,[1]卖家同意}
//        4{[2]退货退款,[1]卖家同意,[3]买家填写物流单号} 5{[2]退货退款,[1]卖家同意,[3]买家填写物流单号,[2]卖家7天内拒绝收货}
//        6{[2]退货退款,[1]卖家同意,[3]买家填写物流单号,[2]卖家7天内拒绝收货,[2]买家向平台申诉}
//        7{[2]退货退款,[2]卖家拒绝} 8{[2]退货退款,[2]卖家拒绝,[2]买家向平台申诉}


        //全部 1待处理 2处理中 3已退款 4已删除 5已取消 6平台已拒绝）
        private fun getState(state: String): String = when (state) {
            "1" -> "待处理"
            "2" -> "处理中"
            "3" -> "已完成"
            "4" -> "已删除"
            "5" -> "已取消"
            "6" -> "已拒绝"
            else -> "无此状态"

        }

        //退货还是退款
        private fun returnGoodsOrPrice(returnOrderType: String) = when (returnOrderType) {
            "1", "3" -> "取消退款"
            "2" -> "取消退货"
            else -> "无此操作"
        }

        /**
         *  returnOrderState 0全部 1待处理 2处理中 3已退款 4已删除 5已取消 6平台已拒绝）
         *  returnOrderSubState   订单子状态
         *  returnOrderType    退货还是退款
         * [state] 订单状态id
         * [view] view[0] 订单状态view
         * [view] view[2] btMid
         * [view] view[3] btRight
         */
        fun initStateBtn(returnOrderState: String, returnOrderSubState: String, returnOrderType: String, vararg view: TextView) {
            var mainState = getState(returnOrderState)   //状态
            var strMid = ""  //左按钮显示文字
            var strRight = ""//右按钮显示文字
//            按钮可见初始化
            view[1].visibility = View.VISIBLE
            view[2].visibility = View.VISIBLE
            when (returnOrderState) {
                "1"// 待处理 联系卖家 取消售后（颜色）
                -> {
                    strMid = "联系卖家"
                    strRight = returnGoodsOrPrice(returnOrderType)
                }
                "2"// 处理中 联系卖家（颜色）
                -> {
                    /*
                     returnOrderState为[2处理中]时，订单子状态才有意义
                     1：{[1]仅退款,[2]卖家拒绝}
                     2：{[1]仅退款,[2]卖家拒绝,[2]买家向平台申诉}
                     3：{[2]退货退款,[1]卖家同意}
                     4：{[2]退货退款,[1]卖家同意,[3]买家填写物流单号}
                     5：{[2]退货退款,[1]卖家同意,[3]买家填写物流单号,[2]卖家7天内拒绝收货}
                     6：{[2]退货退款,[1]卖家同意,[3]买家填写物流单号,[2]卖家7天内拒绝收货,[2]买家向平台申诉}
                     7：{[2]退货退款,[2]卖家拒绝}
                     8：{[2]退货退款,[2]卖家拒绝,[2]买家向平台申诉}
                     具体含义参考《商城模板新接口(补充).xlsx》文件中的《退货流程》sheet
                     */
                    when (returnOrderSubState) {
                        "1" -> {
                            mainState += "(卖家拒绝仅退款)"
                            strMid = "平台申诉"
                            strRight = returnGoodsOrPrice(returnOrderType)
                        }
                        "2" -> {
                            mainState += "(平台申诉中)"
                            strMid = "联系平台"
                            strRight = returnGoodsOrPrice(returnOrderType)
                        }
                        "3" -> {
                            mainState += "(等待买家填写物流)"
                            strMid = "填写物流"
                            strRight = returnGoodsOrPrice(returnOrderType)
                        }
                        "4" -> {
                            mainState += "(等待卖家确认收货)"
                            strMid = "联系卖家"
                            view[2].visibility = View.GONE
                        }
                        "5" -> {
                            mainState += "(卖家拒绝收货)"
                            strMid = "平台申诉"
                            strRight = returnGoodsOrPrice(returnOrderType)
                        }
                        "6" -> {
                            mainState += "(平台申诉中)"
                            strMid = "联系平台"
                            strRight = returnGoodsOrPrice(returnOrderType)
                        }
                        "7" -> {
                            mainState += "(卖家拒绝退货退款)"
                            strMid = "平台申诉"
                            strRight = returnGoodsOrPrice(returnOrderType)
                        }
                        "8" -> {
                            mainState += "(平台申诉中)"
                            strMid = "联系平台"
                            strRight = returnGoodsOrPrice(returnOrderType)

                        }
                        else -> {
                            strMid = "联系平台"
                            strRight = returnGoodsOrPrice(returnOrderType)
                        }
                    }
                }
                "3"// 已退款（颜色）
                -> {
                    strMid = "联系平台"
                    strRight = "删除订单"
                }
            // case 4:// 已删除（颜色）
            // holder.statue.setText("已取消");
            // holder.left.setVisibility(View.GONE);
            // holder.right.setVisibility(View.VISIBLE);
            // holder.right.setText("删除订单");
            // break;
                "5"// 已取消 （颜色）
                -> {
                    strMid = "联系平台"
                    strRight = "删除订单"
                }
                "6"// 已拒绝（颜色）
                -> {
                    strMid = "联系平台"
                    strRight = "删除订单"
                }

                else -> {
                }
            }
            view[0].text = mainState
            view[1].text = strMid
            view[2].text = strRight
        }

        /**
         * 设置点击事件
         */
        fun setOnClick(context: Activity, viewId: Int, entity: ReturnGoodsBean, listener: () -> Unit) {
            when (viewId) {
                R.id.tv_mid -> {//左边按钮
                    when (entity.returnOrderState) {
                        "1" -> {
                            DialogUtils.Call(context as BaseActivity, entity.shopPhone)
                        }
                        "2" -> {
                            when (entity.returnOrderSubState) {
                                "1", "5", "7" -> {//平台申诉
                                    com.exz.carprofitmuch.utils.DialogUtils.platform(context) {
                                        DataCtrlClass.platformAppeal(context, entity.returnOrderId, URLEncoder.encode(it, "utf-8")) {
                                            if (it != null) {
                                                listener.invoke()
                                            }
                                        }
                                    }
                                }
                                "2", "6", "8" -> {//联系平台
                                    DialogUtils.Call(context as BaseActivity, entity.platformPhone)
                                }
                                "3" -> {//填写物流
                                    context.startActivityForResult(Intent(context, InputLogisticsActivity::class.java).putExtra(InputLogisticsActivity.InputLogistics_Intent_OrderId, entity.returnOrderId), 100)
                                }
                                "4" -> {//联系卖家
                                    DialogUtils.Call(context as BaseActivity, entity.shopPhone)
                                }
                                else -> {
                                }
                            }
                        }
                        "3", "5", "6" -> {
                            DialogUtils.Call(context as BaseActivity, entity.platformPhone)
                        }
                    }
                }
                R.id.tv_right -> {//右边按钮
                    when (entity.returnOrderState) {
                        "1", "2" -> {//取消退
                            com.exz.carprofitmuch.utils.DialogUtils.cancel(context) {
                                DataCtrlClassXZW.returnEditOrderData(context, entity.returnOrderId, "0", {
                                    if (it != null) {
                                        listener.invoke()
                                    }
                                })
                            }
                        }
                        "3", "5", "6" -> {  //删除订单
                            com.exz.carprofitmuch.utils.DialogUtils.delete(context) {
                                DataCtrlClassXZW.returnEditOrderData(context, entity.returnOrderId, "1", {
                                    if (it != null) {
                                        listener.invoke()
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}