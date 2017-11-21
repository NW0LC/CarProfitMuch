package com.exz.carprofitmuch.bean

/**
 * Created by 史忠文
 * on 2017/11/20.
 */
class AddressBean{
    var addressId="1"
    var userName="鱼干"
    var userPhone="18888888878"
    var province="江苏"
    var city="南京"
    var district="玄武"
    var detail="吼吼吼吼"
    override fun toString(): String = province+city+district+detail
}