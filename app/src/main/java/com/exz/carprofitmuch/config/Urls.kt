package com.exz.carprofitmuch.config

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
object Urls{
    var APP_ID = ""
    var url = "http://jingu.xzsem.cn/"

    /**
     * 获取验证码
     */
    val VerifyCode=url+"App/Account/VerifyCode.aspx"
    /**
     * 注册
     */
    val Register=url+"App/Account/Register.aspx"
    /**
     * 登录
     */
    val Login=url+"App/Account/Login.aspx"
    /**
     * 忘记密码
     */
    val ForgetPwd=url+"App/Account/ForgetPwd.aspx"
    /**
     * 首页banner图
     */
    val HomeBanner=url+"App/Home/Banner.aspx"
    /**
     * 首页banner图
     */
    val AdsList=url+"App/Home/AdsList.aspx"


































































    //==========================================================================================

    /*
    * 宝藏地图页数据
    */
    var MapTreasure = url + "App/Home/MapTreasure.aspx"

    /*
    * 店铺的宝藏数据
    */
    var ShopTreasure = url + "App/Home/ShopTreasure.aspx"

    /*
    * 领取宝藏
    */
    var GetTreasure = url + "App/Home/GetTreasure.aspx"


    /*
    * 红包地图页数据
    */
    var MapPacket = url + "App/Home/MapPacket.aspx"


    /*
       * 店铺的红包数据
       */
    var ShopPacket = url + "App/Home/ShopPacket.aspx"

    /*
       * 领取红包
       */
    var GetPacket = url + "App/Home/GetPacket.aspx"


    /*
       * 我的宝藏
       */
    var MyTreasure = url + "App/UserCenter/MyTreasure.aspx"

    /*
       * 店铺等级
       */
    var ShopLevel = url + "App/Open/ShopLevel.aspx"

    /*
       * 店铺类目
       */
    var ShopCategory = url + "App/Store/ShopCategory.aspx"

    /*
       * 资料审核结果
       */
    var ConfirmInfo = url + "App/Open/ConfirmInfo.aspx"


    /*
       * 资料审核结果
       */
    var CheckResult = url + "App/Open/CheckResult.aspx"

    /*
       * 修改申请资料
       */
    var ModifyInfo = url + "App/Open/ModifyInfo.aspx"


    /*
       * 我的优惠券
       */
    var MyCoupon = url + "App/UserCenter/MyCoupon.aspx"

     /*
        * 我的评价
        */
     var MyCommentList = url + "App/UserCenter/MyCommentList.aspx"

     /*
        * 订单列表-实物类
        */
     var OrderList = url + "App/Order/Reality/OrderList.aspx"




































    /**
     * 广告点击（已登录的用户调用此接口）
     */
    val AdsClick=url+"App/Home/AdsClick.aspx"
    /**
     * 热销推荐(返回5条数据)
     */
    val HomeRecommend=url+"App/Home/Recommend.aspx"
    /**
     * 热点资讯
     */
    val HomeHotNews=url+"App/Home/HotNews.aspx"
    /**
     * 热点资讯列表
     */
    val HomeNewsList=url+"App/Home/NewsList.aspx"
}