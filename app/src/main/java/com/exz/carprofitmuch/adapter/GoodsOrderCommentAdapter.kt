package com.exz.carprofitmuch.adapter

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
import kotlinx.android.synthetic.main.item_comment_order_img.view.*
import kotlinx.android.synthetic.main.item_goods_order_comment.view.*

class GoodsOrderCommentAdapter : BaseQuickAdapter<GoodsOrderCommentBean, BaseViewHolder>(R.layout.item_goods_order_comment, ArrayList<GoodsOrderCommentBean>()) {
    override fun convert(helper: BaseViewHolder, item: GoodsOrderCommentBean) {
        val v = helper.itemView
        v.llImgLay.removeAllViews()
        for (photo in item.photos) {
            val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_comment_order_img, LinearLayout(mContext), false)
            itemView.imgs.setImageURI(photo)
            itemView.bt_close.visibility = if (item.photos.indexOf(photo) == item.photos.size - 1) View.GONE else View.VISIBLE
            helper.addOnClickListener(R.id.bt_close)
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.height = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(40f)) / 5
            layoutParams.width = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(40f)) / 5
            itemView.layoutParams = layoutParams
            v.llImgLay.addView(itemView)
            itemView.bt_close.setOnClickListener {
                item.photos.remove(photo)
                notifyItemChanged(helper.adapterPosition)

            }
            itemView.imgs.setOnClickListener {
                onItemClick.OnItemClickListener(helper.adapterPosition, item.photos.indexOf(photo))

            }

        }
        v.ed_content_count.setText(String.format(mContext.getString(R.string.service_order_comment_content_count), 120))




        v.mRatingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
            item.score = ratingBar.rating.toString()
        }


        v.ed_content.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                v.ed_content_count.setText(String.format(mContext.getString(R.string.service_order_comment_content_count), 120 - s.toString().trim().length))
                item.content = v.ed_content_count.text.toString().trim()
            }
        })


    }

    fun setOnItemClickListeners(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    lateinit var onItemClick: OnItemClick

    interface OnItemClick {
        fun OnItemClickListener(position: Int, positionImg: Int)
    }

}
