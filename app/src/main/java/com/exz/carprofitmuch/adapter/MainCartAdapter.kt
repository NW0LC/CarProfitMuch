package com.exz.carprofitmuch.adapter

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.GoodsCarBean
import com.exz.carprofitmuch.module.CartFragment
import com.exz.carprofitmuch.module.CartFragment.Companion.itemSelect
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity.Companion.GoodsDetail_Intent_GoodsId
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type_Delete
import com.exz.carprofitmuch.module.mine.favorite.FavoriteGoodsActivity.Companion.Edit_Type_Edit
import com.exz.carprofitmuch.utils.DialogUtils
import kotlinx.android.synthetic.main.fragment_main_cart.*
import kotlinx.android.synthetic.main.item_main_cart.view.*
import kotlinx.android.synthetic.main.item_main_cart_lose.view.*
import java.util.*

class MainCartAdapter<T : GoodsCarBean>(context: CartFragment) : BaseMultiItemQuickAdapter<T, BaseViewHolder>(ArrayList<T>()) {
    private var cartFragment: CartFragment = context
    var animatorEnable = false
    //    R.layout.item_main_cart
    init {
        addItemType(GoodsCarBean.TYPE_1, R.layout.item_main_cart)
        addItemType(GoodsCarBean.TYPE_2, R.layout.item_main_cart_lose)
    }

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        when (getItemViewType(helper.adapterPosition)) {
            GoodsCarBean.TYPE_1 -> {
                helper.addOnClickListener(R.id.cb_goodsShop_name)
                itemView.cb_goodsShop_name.text = item.shopName
                itemView.cb_goodsShop_name.isChecked = item.isCheck

                val mAdapter = ItemMainCartAdapter<GoodsBean>(cartFragment, this)
                mAdapter.bindToRecyclerView(itemView.mRecyclerView)
                itemView.mRecyclerView.isFocusable = false
                itemView.mRecyclerView.layoutManager = LinearLayoutManager(mContext)
                mAdapter.setNewData(item.goods)
                mAdapter.setOnItemChildClickListener { _, view, position ->
                    val goodsEntity = mAdapter.data[position]
                    val index = (if (TextUtils.isEmpty(goodsEntity.goodsCount)) "1" else goodsEntity.goodsCount).toLong()
                    when (view.id) {
                        R.id.radioButton -> {
                            itemSelect(this, mAdapter, cartFragment.select_all, helper.adapterPosition, position)
                            cartFragment.setAllPrice()
                        }
                        R.id.minus -> if (index > 1) {
                            cartFragment.cartChangeCount(goodsEntity, (index - 1)) {}
                        }
                        R.id.add -> cartFragment.cartChangeCount(goodsEntity, (index + 1)) {}
                        R.id.count -> DialogUtils.changeNum(mContext, index) {
                            cartFragment.cartChangeCount(goodsEntity, it) {}
                        }
                        R.id.lay_main_cart -> {
                            when (if (Edit_Type == "-1") Edit_Type_Edit else Edit_Type) {
                                Edit_Type_Edit -> {
                                    //跳转
                                    val intent = Intent(mContext, GoodsDetailActivity::class.java)
                                    intent.putExtra(GoodsDetail_Intent_GoodsId, mAdapter.data[position].id)
                                    mContext.startActivity(intent)
                                }
                                Edit_Type_Delete -> {
                                    //删除
                                    itemSelect(this, mAdapter, cartFragment.select_all, helper.adapterPosition, position)
                                    cartFragment.setAllPrice()
                                }
                            }
                        }
                    }
                }
            }
            GoodsCarBean.TYPE_2 -> {
                helper.addOnClickListener(R.id.bt_clearGoods)
                itemView.tv_loseGoodsCount.text=String.format(mContext.getString(R.string.main_cart_loseGoodsCount),item.goods.size.toString())

                val mAdapter = ItemMainCartLoseAdapter<GoodsBean>(cartFragment, this,animatorEnable)
                mAdapter.bindToRecyclerView(itemView.mLoseRecyclerView)
                itemView.mLoseRecyclerView.isFocusable = false
                itemView.mLoseRecyclerView.layoutManager = LinearLayoutManager(mContext)
                mAdapter.setNewData(item.goods)
                mAdapter.setOnItemChildClickListener { _, view, position ->
                    when (view.id) {
                        R.id.radioButton -> {
                            itemSelect(this, mAdapter, cartFragment.select_all, helper.adapterPosition, position)
                        }
                        R.id.lay_main_cart -> {
                            when (if (Edit_Type == "-1") Edit_Type_Edit else Edit_Type) {
                                Edit_Type_Edit -> {
                                    //跳转
                                    val intent = Intent(mContext, GoodsDetailActivity::class.java)
                                    intent.putExtra(GoodsDetail_Intent_GoodsId, mAdapter.data[position].id)
                                    mContext.startActivity(intent)
                                }
                                Edit_Type_Delete -> {
                                    //删除
                                    itemSelect(this, mAdapter, cartFragment.select_all, helper.adapterPosition, position)
                                }
                            }
                        }
                    }
                }
            }
        }


    }
}