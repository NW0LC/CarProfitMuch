package com.exz.carprofitmuch.adapter

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
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
import java.util.*

class MainCartAdapter<T : GoodsCarBean> (context: CartFragment): BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_main_cart, ArrayList<T>()) {
    private var cartFragment: CartFragment = context
    override fun convert(helper: BaseViewHolder, item: T) {
        helper.addOnClickListener(R.id.cb_goodsShop_name)
        val itemView=helper.itemView
        itemView.cb_goodsShop_name.text=item.shopName
        itemView.cb_goodsShop_name.isChecked=item.isCheck

        val mAdapter= ItemMainCartAdapter<GoodsBean>(cartFragment,this,helper.adapterPosition)
        mAdapter.bindToRecyclerView(itemView.mRecyclerView)
        itemView.mRecyclerView.isFocusable=false
        itemView.mRecyclerView.layoutManager= LinearLayoutManager(mContext)
        mAdapter.setNewData(item.goods)
        mAdapter.setOnItemChildClickListener { _, view, position ->
            val goodsEntity = mAdapter.data[position]
            val index = Integer.valueOf(if (TextUtils.isEmpty(goodsEntity.goodsCount)) "1" else goodsEntity.goodsCount)
            when (view.id) {
                R.id.radioButton -> {
                    itemSelect(this, mAdapter,cartFragment.select_all,helper.adapterPosition, position)
                    cartFragment.setAllPrice()
                }
                R.id.minus -> if (index > 1) {
                    cartFragment.cartChangeCount(goodsEntity, (index - 1).toLong()){}
                }
                R.id.add -> cartFragment.cartChangeCount(goodsEntity, (index + 1).toLong()){}
                R.id.count -> DialogUtils.changeNum(mContext, index.toLong()){
                    cartFragment.cartChangeCount(goodsEntity, it){}
                }
                R.id.lay_main_cart->{
                    when(if (Edit_Type == "-1") Edit_Type_Edit else Edit_Type) {
                        Edit_Type_Edit->{
                            //跳转
                            val intent = Intent(mContext, GoodsDetailActivity::class.java)
                            intent.putExtra(GoodsDetail_Intent_GoodsId, mAdapter.data[position].id)
                            mContext.startActivity (intent)
                        }
                        Edit_Type_Delete->{
                            //删除
                            itemSelect(this,mAdapter, cartFragment.select_all,helper.adapterPosition, position)
                            cartFragment.setAllPrice()
                        }
                    }
                }
            }
        }

    }
}