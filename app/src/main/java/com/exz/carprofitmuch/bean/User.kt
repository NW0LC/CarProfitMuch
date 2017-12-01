package com.exz.carprofitmuch.bean

import com.szw.framelibrary.entities.AbsUser

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
class User(override val userId: String = "") : AbsUser() {
    /**
     * headerUrl : 1
     * nickname : 昵称
     * level : 标准会员
     * overDate : 2018.10.16
     * isMsg : 0
     * scoreT : 3650
     * scoreL : 2000
     * scoreG : 156.3
     * balance : 1000
     * wallet : 2
     * coupon : 2
     * treasure : 2
     * score : 200
     * openState : 0
     */
    var headerUrl: String=""
    var nickname: String=""
    var level: String=""
    var overDate: String=""
    var isMsg: String=""
    var scoreT: String=""
    var scoreL: String=""
    var scoreG: String=""
    var balance: String=""
    var wallet: String=""
    var coupon: String=""
    var treasure: String=""
    var score: String=""
    var openState: String=""
}