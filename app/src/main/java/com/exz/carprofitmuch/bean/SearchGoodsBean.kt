package com.exz.carprofitmuch.bean

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.*

/**
 * Created by 史忠文
 * on 2017/6/6.
 */
@Entity
open class SearchGoodsBean(var searchContent: String = "", var date: Date? = null, @Id var id: Long = 0) {
    companion object {
        val primaryKey = "searchContent"
        val DateKey = "date"
    }
}
