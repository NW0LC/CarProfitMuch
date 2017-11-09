package com.exz.carprofitmuch.bean

import com.szw.framelibrary.entities.AbsUser

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
class User : AbsUser() {
    override val userId: String = ""
    var autoLoginToken=""
}