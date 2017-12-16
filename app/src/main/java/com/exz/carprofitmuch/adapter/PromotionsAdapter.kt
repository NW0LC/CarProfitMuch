package com.exz.carprofitmuch.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.PromotionsBean
import com.flyco.roundview.RoundTextView
import kotlinx.android.synthetic.main.item_main_promotions.view.*
import java.util.*

class PromotionsAdapter<T : PromotionsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_main_promotions, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        helper.addOnClickListener(R.id.tv_state)
        itemView.img.setImageURI(item.imgUrl)
        itemView.img.layoutParams.height = (ScreenUtils.getScreenWidth() - SizeUtils.dp2px(20f)) * 5 / 13
        itemView.tv_title.text = item.title
        itemView.tv_speed.text = String.format("%s" + mContext.getString(R.string.DAY), item.dayCount)
        itemView.tv_distance.text = item.distance
        setStateColorAndStr(mContext, item.isJoin + item.state + item.isUpload, view = *arrayOf(itemView.tv_state))
    }

    companion object {
        /**
         * isUpload  当前用户是否已上传图片：0未上传 1已上传
         * isJoin :0未报名 1已报名
         * state :1可报名 2名额已满 3报名截止 4活动开始 5活动结束
         *         橘黄色   灰色       灰色      绿色      灰色
         */
        fun setStateColorAndStr(context: Context, state: String, isList: Boolean = true, vararg view: RoundTextView) {
            val str: String
            val color: Int
            when (state) {
                "01", "010", "011" -> {
                    str = "去报名"
                    color = ContextCompat.getColor(context, R.color.colorPrimary)
                }
                "02", "020", "021" -> {
                    str = "名额已满"
                    color = ContextCompat.getColor(context, R.color.MaterialGrey400)
                }
                "03", "030", "031" -> {
                    str = "报名截止"
                    color = ContextCompat.getColor(context, R.color.MaterialGrey400)
                }
                "04", "040", "041" -> {
                    str = "已开始"
                    color = ContextCompat.getColor(context, R.color.MaterialGreenA400)
                }
                "05", "050", "051" -> {
                    str = "已结束"
                    color = ContextCompat.getColor(context, R.color.MaterialGrey400)
                }
                "11", "110", "111" -> {
                    str = "已报名"
                    color = ContextCompat.getColor(context, R.color.MaterialBlue400)
                }
                "12", "120", "121" -> {
                    str = "已报名"
                    color = ContextCompat.getColor(context, R.color.MaterialBlue400)
                }
                "13", "130", "131" -> {
                    str = "已报名"
                    color = ContextCompat.getColor(context, R.color.MaterialBlue400)
                }
                "14","140" -> {
                    str = if (isList) "已开始" else "上传照片"
                    color = ContextCompat.getColor(context, R.color.MaterialGreenA400)
                }
                "141" -> {
                    str = if (isList) "已开始" else "已上传"
                    color = ContextCompat.getColor(context, R.color.MaterialGreenA400)
                }
                "15", "150", "151" -> {
                    str = "已结束"
                    color = ContextCompat.getColor(context, R.color.MaterialGrey400)
                }
                else -> {
                    str = "无此状态"
                    color = ContextCompat.getColor(context, R.color.MaterialGrey400)
                }
            }
            view[0].text = str
            view[0].delegate.backgroundColor = color
        }
    }
}