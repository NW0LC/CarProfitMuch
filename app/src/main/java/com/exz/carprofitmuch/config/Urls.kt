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