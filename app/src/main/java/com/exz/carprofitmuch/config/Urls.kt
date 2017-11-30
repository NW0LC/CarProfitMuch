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
    /**
     * 商家活动列表页
     */
    val ActivityList=url+"App/Home/ActivityList.aspx"
    /**
     * 参加商家活动
     */
    val ActivityJoin=url+"App/Home/ActivityJoin.aspx"
    /**
     * 商家活动详情
     */
    val ActivityDetial=url+"App/Home/ActivityDetial.aspx"
    /**
     * 上传参加的活动图片
     */
    val UploadActivity=url+"App/UserCenter/UploadActivity.aspx"
    /**
     * 我参加的活动
     */
    val MyActivity=url+"App/UserCenter/MyActivity.aspx"
    /**
     * 我参加的活动
     */
    val StoreHome=url+"App/Store/Home.aspx"
    /**
     * 积分商品列表
     */
    val ScoreGoodsList=url+"App/Store/ScoreGoodsList.aspx"


































































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


     /*
        * 编辑订单（取消，删除，确认收货）-实物类
        */
     var EditOrder = url + "App/Order/Reality/EditOrder.aspx"

     /*
        * 上传图片
        */
     var UploadImg = url + "App/Tool/UploadImg.aspx"


     /*
        * 订单评价-实物类
        */
     var CommentOrder = url + "App/Order/Reality/CommentOrder.aspx"


     /*
        * 填写物流单号-实物类
        */
     var WriteLogistics = url + "App/Order/Reality/ReturnOrder/WriteLogistics.aspx"


     /*
        *  退货方式列表-实物类
        */
     var ReturnGoodsTypeList = url + "App/Order/Reality/ReturnOrder/ReturnGoodsTypeList.aspx"


     /*
        *  退货原因列表-实物类
        */
     var ReturnGoodsReasonList = url + "App/Order/Reality/ReturnOrder/ReturnGoodsReasonList.aspx"




































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