package com.exz.carprofitmuch.bean

import java.io.Serializable

/**
 * Created by 史忠文
 * on 2017/11/20.
 */
open class AddressBean :Serializable{
    var isDefault: String = "1"
    var id=""
    var addressId=""
        get() {
            if (field.isEmpty())
            field=id
            return field
        }
    var name=""
    var phone=""
    var province=""
    var city=""
    var district=""
    var districtId=""
    var detail=""
    fun isDefault():Boolean = isDefault=="1"
    override fun toString(): String = province+city+district+detail




}