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
    /**
     * 一级商品分类
     */
    val TypeList=url+"App/Store/Reality/TypeList.aspx"
    /**
     * 获取的二级、三级分类
     */
    val SubTypeList=url+"App/Store/Reality/SubTypeList.aspx"
    /**
     * 筛选列表
     */
    val SiftList=url+"App/Store/Reality/SiftList.aspx"
    /**
     * 商品列表（不包含积分商品）
     */
    val GoodsList=url+"App/Store/Reality/GoodsList.aspx"
    /**
     * 商品详情
     */
    val GoodsDetail=url+"App/Store/Reality/GoodsDetail.aspx"
    /**
     * 优惠劵列表
     */
    val CouponList=url+"App/Store/CouponList.aspx"
    /**
     * 领取优惠劵
     */
    val GetCoupon=url+"App/Store/GetCoupon.aspx"
    /**
     * 商品规格
     */
    val GoodsRank=url+"App/Store/Reality/GoodsRank.aspx"
    /**
     * 【添加/取消】【关注/收藏】
     */
    val Attention=url+"App/Store/Attention.aspx"
    /**
     * 评价列表
     */
    val CommentList=url+"App/Store/CommentList.aspx"
    /**
     * 评价统计
     */
    val CommentCount=url+"App/Store/CommentCount.aspx"
    /**
     * 店铺主页
     */
    val ShopMain=url+"App/Store/Reality/ShopMain.aspx"
    /**
     * 商品规格
     */
    val ShopSelfTypeList=url+"App/Store/Reality/ShopSelfTypeList.aspx"
    /**
     * 店铺内商品列表
     */
    val ShopGoodsList=url+"App/GoodsType/ShopGoodsList.aspx"
    /**
     * 虚拟类店铺列表
     */
    val ShopList=url+"App/Store/Virtually/ShopList.aspx"
    /**
     * 服务店铺详情
     */
    val VirtuallyShopMain=url+"App/Store/Virtually/ShopMain.aspx"
    /**
     * 虚拟类商品详情
     */
    val VirtuallyGoodsDetail=url+"App/Store/Virtually/GoodsDetail.aspx"

    /**
     * 订单确认信息页-积分实物类
     */
    val ScoreOrderInfo=url+"App/Order/Reality/ScoreOrderInfo.aspx"

    /**
     * 创建订单-积分实物类
     */
    val CreateScoreOrder=url+"App/Order/Reality/CreateScoreOrder.aspx"
    /**
     * 订单确认信息页-虚拟类
     */
    val VirtuallyOrderInfo=url+"App/Order/Virtually/OrderInfo.aspx"

    /**
     * 创建订单-虚拟类
     */
    val VirtuallyCreateOrder=url+"App/Order/Virtually/CreateOrder.aspx"

    /**
     * 加入购物车
     */
    val AddShopCar=url+"App/ShopCar/Reality/AddShopCar.aspx"

    /**
     * 删除购物车
     */
    val DeleteShopCar=url+"App/ShopCar/Reality/DeleteShopCar.aspx"

    /**
     * 我的购物车
     */
    val ShopCarList=url+"App/ShopCar/Reality/ShopCarList.aspx"

    /**
     * 编辑购物车中商品数量
     */
    val EditShopCar=url+"App/ShopCar/Reality/EditShopCar.aspx"

    /**
     * 订单确认信息页-金钱实物类
     */
    val MoneyOrderInfo=url+"App/Order/Reality/MoneyOrderInfo.aspx"
    /**
     * 可用积分-金钱实物类
     */
    val CanUseScore=url+"App/Order/Reality/CanUseScore.aspx"

    /**
     * 创建订单-金钱实物类（有可能是多个订单，因为一个店铺就是一个订单）
     */
    val CreateMoneyOrder=url+"App/Order/Reality/CreateMoneyOrder.aspx"
    /**
     * 订单支付信息
     */
    val PayInfo=url+"App/Order/PayInfo.aspx"
    /**
     * 是否设置了支付密码
     */
    val IsSetPayPwd=url+"App/PayPwd/IsSetPayPwd.aspx"
    /**
     * 【订单支付】 支付宝签名
     */
    val AliPay=url+"App/Order/AliPay/Signature.aspx"
    /**
     * 【订单支付】 微信支付签名
     */
    val WeChatPay=url+"App/Order/WeChatPay/Signature.aspx"
    /**
     * 【订单支付】 支付状态 (第三方支付服务器端验证）
     */
    val PayState=url+"App/Order/PayState.aspx"
    /**
     * 【订单支付】 余额支付
     */
    val BalancePay=url+"App/Order/BalancePay.aspx"
    /**
     * 设置支付密码的验证码验证
     */
    val PayPwdCodeVerify=url+"App/PayPwd/PayPwdCodeVerify.aspx"
    /**
     * 设置支付密码
     */
    val SetPayPwd=url+"App/PayPwd/SetPayPwd.aspx"
    /**
     * 验证支付密码（修改支付密码前调用）
     */
    val PayPwdVerify=url+"App/PayPwd/PayPwdVerify.aspx"
    /**
     * 修改支付密码
     */
    val ModifyPayPwd=url+"App/PayPwd/ModifyPayPwd.aspx"
    /**
     * 设置-修改登录密码
     */
    val ModifyLoginPwd=url+"App/UserCenter/Set/ModifyLoginPwd.aspx"

    /**
     * 缴费金额
     */
    val VipSignature=url+"App/UserCenter/Vip/Signature.aspx"
    /**
     * 【会员缴费】 余额支付
     */
    val VipBalancePay=url+"App/UserCenter/Vip/BalancePay.aspx"
    /**
     * 【会员缴费】 支付状态 (第三方支付服务器端验证）
     */
    val VipPayState=url+"App/UserCenter/Vip/PayState.aspx"
    /**
     * 【会员缴费】 微信支付签名
     */
    val VipWeChatPay=url+"App/UserCenter/Vip/WeChatPay/Signature.aspx"
    /**
     * 【会员缴费】 支付宝签名
     */
    val VipAliPay=url+"App/UserCenter/Vip/AliPay/Signature.aspx"
    /**
     * 订单详情-实物类
     */
    val OrderDetail=url+"App/Order/Reality/OrderDetail.aspx"
    /**
     * 订单列表-虚拟类（卡券包）
     */
    val VirtuallyOrderList=url+"App/Order/Virtually/OrderList.aspx"
    /**
     * 订单详情-虚拟类（卡券包详情）
     */
    val VirtuallyOrderDetail=url+"App/Order/Virtually/OrderDetail.aspx"
    /**
     * 编辑订单（删除，申请退款）-虚拟类
     */
    val VirtuallyEditOrder=url+"App/Order/Virtually/EditOrder.aspx"
    /**
     * 积分规则
     */
    val PointRule=url+"App/H5/PointRule.aspx"
    /**
     * 关于我们
     */
    val AboutUS=url+"App/H5/AboutUS.aspx"
    /**
     * 用户协议
     */
    val UserAgreement=url+"App/H5/UserAgreement.aspx"
    /**
     * 向平台申诉
     */
    val PlatformAppeal=url+"App/Order/Reality/ReturnOrder/PlatformAppeal.aspx"
    /**
     * 消息列表
     */
    val Message=url+"App/UserCenter/Message.aspx"

    /**
     * 买车险送积分
     */
    val SendIntegral=url+"App/Home/SendIntegral.aspx"

    /**
     * 清空购物车内所有失效商品
     */
    val ClearDeleteGoods=url+"App/ShopCar/Reality/ClearDeleteGoods.aspx"


































































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
        * 退货/退款订单列表-实物类
        */
     var ReturnOrderOrderList = url + "App/Order/Reality/ReturnOrder/OrderList.aspx"


     /*
        * 编辑订单（取消，删除，确认收货）-实物类
        */
     var EditOrder = url + "App/Order/Reality/EditOrder.aspx"
     /*
        *编辑退款/货订单（取消，删除）-实物类
        */
     var ReturnOrderEditOrder = url + "App/Order/Reality/ReturnOrder/EditOrder.aspx"

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

     /*
        *  申请退货-实物类
        */
     var ReturnGoods = url + "App/Order/Reality/ReturnOrder/ReturnGoods.aspx"



     /*
        * 申请退款-实物类
        */
     var ApplyReturnMoney = url + "App/Order/Reality/ReturnOrder/applyReturnMoney.aspx"


    /*
     * 个人中心主页
     */
    var Main = url + "App/UserCenter/Main.aspx"


    /*
     * 我的余额
     */
    var MyBalance = url + "App/UserCenter/Balance/MyBalance.aspx"


    /*
     * 申请提现
     */
    var ApplyWithdraw = url + "App/UserCenter/Balance/ApplyWithdraw.aspx"

    /*
     * 余额变更记录
     */
    var Record = url + "App/UserCenter/Balance/Record.aspx"

    /*
     * 支付宝充值
     */
    var AliRecharge = url + "App/UserCenter/Balance/AliRecharge/Signature.aspx"

    /*
     * 微信充值
     */
    var WeChatRecharge = url + "App/UserCenter/Balance/WeChatRecharge/Signature.aspx"


    /*
     * 充值状态 (第三方支付服务器端验证）
     */
    var BalanceRechargeCheck = url + "App/UserCenter/Balance/RechargeCheck.aspx"

    /*
     * 我的积分
     */
    var MyScore = url + "App/UserCenter/MyScore.aspx"

    /*
     * 我的积分变更记录
     */
    var MyScoreRecord = url + "App/UserCenter/MyScoreRecord.aspx"

    /*
     * 收藏的商品
     */
    var CollectedGoodsList = url + "App/UserCenter/CollectedGoodsList.aspx"



    /*
     * 关注的店铺
     */
    var CollectedShopList = url + "App/UserCenter/CollectedShopList.aspx"

    /*
     * 我的足迹
     */
    var Footprint = url + "App/UserCenter/Footprint.aspx"

    /*
     * 清空我的足迹
     */
    var ClearFootprint = url + "App/UserCenter/ClearFootprint.aspx"


    /*
     * 设置-个人信息
     */
    var UserInfo = url + "App/UserCenter/Set/UserInfo.aspx"


    /*
     * 设置-修改个人信息
     */
    var UpdateUserInfo = url + "App/UserCenter/Set/UpdateUserInfo.aspx"


    /*
     * 设置-收货地址列表
     */
    var AddressList = url + "App/Address/List.aspx"


    /*
     * 设置-默认收货地址
     */
    var AddressDefault = url + "App/Address/Default.aspx"


    /*
     * 设置-删除收货地址
     */
    var AddressDelete = url + "App/Address/Delete.aspx"



    /*
     * 新增收货地址（当用户添加地址时，后台判断该用户是否有其他地址，若没有，将该地址设为默认地址）
     */
    var AddAddress = url + "App/Address/Add.aspx"
    /*
     * 新增收货地址（当用户添加地址时，后台判断该用户是否有其他地址，若没有，将该地址设为默认地址）
     */
    var ModifyAddress = url + "App/Address/Modify.aspx"



































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