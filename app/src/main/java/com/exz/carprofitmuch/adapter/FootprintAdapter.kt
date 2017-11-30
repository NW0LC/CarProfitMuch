package com.exz.carprofitmuch.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GoodsBean
import kotlinx.android.synthetic.main.item_footprint.view.*
import java.text.SimpleDateFormat
import java.util.*


class FootprintAdapter<T : GoodsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_footprint, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, entity: T) {
        val itemView=helper.itemView
        itemView.img.setImageURI(entity.imgUrl)
        itemView.tv_goodsName.text=entity.title
        itemView.tv_goodsPrice.text =String.format("${mContext.getString(R.string.CNY)}%s",entity.price)
//        setMouth(mContext,itemView.tv_date,entity.date,if (helper.adapterPosition>0)data[helper.adapterPosition-1].date else "")

        itemView.tv_date.text=entity.date
        itemView.tv_date.visibility=if (helper.adapterPosition>0) if (entity.date==getItem(helper.adapterPosition-1)?.date) View.GONE else View.VISIBLE else View.VISIBLE
    }
    companion object {
        fun setMouth(mContext: Context, mouthView: TextView, time:String, lastTime:String) {
            var mouth:Int
            val calendar = Calendar.getInstance(Locale.CHINA)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
            calendar.time = dateFormat.parse(time)
            mouth = calendar.get(Calendar.MONTH)
            if (lastTime.isNotEmpty()) {
                calendar.time = dateFormat.parse(lastTime)
                val lastMouth = calendar.get(Calendar.MONTH)
                if (mouth!=lastMouth) {
                    mouthView.visibility= View.VISIBLE
                }else{
                    mouthView.visibility= View.GONE
                }
            }else{
                mouthView.visibility= View.VISIBLE
            }


            if (mouth==Calendar.getInstance(Locale.CHINA).get(Calendar.MONTH))
                mouth=-1
            when (mouth) {
                0 ->  mouthView.text=mContext.getString(R.string.mouth_0)
                1 ->  mouthView.text=mContext.getString(R.string.mouth_1)
                2 ->  mouthView.text=mContext.getString(R.string.mouth_2)
                3 ->  mouthView.text=mContext.getString(R.string.mouth_3)
                4 ->  mouthView.text=mContext.getString(R.string.mouth_4)
                5 ->  mouthView.text=mContext.getString(R.string.mouth_5)
                6 ->  mouthView.text=mContext.getString(R.string.mouth_6)
                7 ->  mouthView.text=mContext.getString(R.string.mouth_7)
                8 ->  mouthView.text=mContext.getString(R.string.mouth_8)
                9 ->  mouthView.text=mContext.getString(R.string.mouth_9)
                10 -> mouthView.text=mContext.getString(R.string.mouth_10)
                11 -> mouthView.text=mContext.getString(R.string.mouth_11)
                else -> {
                    mouthView.text=mContext.getString(R.string.mouth_now)
                }
            }
        }
    }
}


