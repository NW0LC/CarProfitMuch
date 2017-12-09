package com.exz.carprofitmuch.adapter

import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsOrderCommentBean
import com.szw.framelibrary.imageloder.GlideApp
import kotlinx.android.synthetic.main.item_comment_order_img.view.*
import kotlinx.android.synthetic.main.item_goods_order_comment.view.*

class GoodsOrderCommentAdapter : BaseQuickAdapter<GoodsOrderCommentBean, BaseViewHolder>(R.layout.item_goods_order_comment, ArrayList<GoodsOrderCommentBean>()) {
    override fun convert(helper: BaseViewHolder, item: GoodsOrderCommentBean) {
        val itemView = helper.itemView
        itemView.img.setImageURI(item.imgUrl)
        itemView.tv_service_goodsName.text=item.goodsName
        itemView.llImgLay.removeAllViews()
        for (index in 0 until item.photos.size){
            val imgGrid = LayoutInflater.from(mContext).inflate(R.layout.item_comment_order_img, LinearLayout(mContext), false)
            GlideApp.with(mContext).load(Uri.parse(item.photos[index])).into(imgGrid.imgs)
            imgGrid.bt_close.visibility = if (index == item.photos.size - 1) View.GONE else View.VISIBLE
            helper.addOnClickListener(R.id.bt_close)
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.height = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(40f)) / 5
            layoutParams.width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(40f)) / 5
            imgGrid.layoutParams = layoutParams
            itemView.llImgLay.addView(imgGrid)
            imgGrid.bt_close.setOnClickListener {
                item.photos.remove(item.photos[index])
                notifyItemChanged(helper.adapterPosition)

            }
            imgGrid.imgs.setOnClickListener {
                onItemClick.onItemClickListener(helper.adapterPosition, index)

            }
        }
        itemView.ed_content_count.text = String.format(mContext.getString(R.string.service_order_comment_content_count), 120)
        itemView.mRatingBar.setOnRatingBarChangeListener { ratingBar, _, _ ->
            data[helper.adapterPosition].score = ratingBar.rating.toString()
        }


        itemView.ed_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                itemView.ed_content_count.text = String.format(mContext.getString(R.string.service_order_comment_content_count), 120 - s.toString().trim().length)
                data[helper.adapterPosition].content =s.toString()
            }
        })


    }

    fun setOnItemClickListeners(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    private lateinit var onItemClick: OnItemClick

    interface OnItemClick {
        fun onItemClickListener(position: Int, positionImg: Int)
    }

}
