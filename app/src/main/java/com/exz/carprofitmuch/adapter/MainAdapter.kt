package com.exz.carprofitmuch.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.InformationBean
import kotlinx.android.synthetic.main.item_main_type_1.view.*
import java.util.*


class MainAdapter<T : InformationBean> : BaseMultiItemQuickAdapter<T, BaseViewHolder>(ArrayList<T>()) {
    init {
        addItemType(InformationBean.TYPE_1, R.layout.item_main_type_1)
        addItemType(InformationBean.TYPE_2, R.layout.item_main_type_2)
    }

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.tv_title.text=item.title
        itemView.tv_time.text=item.time
        itemView.img.setImageURI(item.img)

    }
}
