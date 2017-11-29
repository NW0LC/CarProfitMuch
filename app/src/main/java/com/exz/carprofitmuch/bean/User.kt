package com.exz.carprofitmuch.bean

import com.szw.framelibrary.entities.AbsUser

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
class User(override val userId: String = "") : AbsUser() {
    var autoLoginToken=""
    var openState=""
}