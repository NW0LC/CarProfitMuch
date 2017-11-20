package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/11/20.
 */
class AddressBean{
    var addressId=""
    var userName=""
    var userPhone=""
    var province=""
    var city=""
    var district=""
    var detail=""
    override fun toString(): String = province+city+district+detail
}