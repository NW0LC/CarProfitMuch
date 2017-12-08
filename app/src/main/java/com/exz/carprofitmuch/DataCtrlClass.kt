package com.exz.carprofitmuch

import android.content.Context
import android.text.TextUtils
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.TimeUtils
import com.exz.carprofitmuch.R.id.heightPrice
import com.exz.carprofitmuch.R.id.lowPrice
import com.exz.carprofitmuch.adapter.GoodsConfirmBean
import com.exz.carprofitmuch.adapter.GoodsConfirmScoreBean
import com.exz.carprofitmuch.bean.*
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.pop.SearchFilterPop
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.app.MyApplication.Companion.loginUserId
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import com.szw.framelibrary.utils.net.callback.JsonCallback
import com.szw.framelibrary.view.CustomProgress
import org.jetbrains.anko.toast
import java.util.*

/**
 * Created by 史忠文
 * on 2017/10/18.
 */

object DataCtrlClass {
    /**
     * 获取验证码
     * @param[phone] string	必填	手机号
     * @param[purpose] string	必填	用途：1注册，2忘记密码，3设置支付密码
     * */
    fun getSecurityCode(context: Context, phone: String, purpose: String, listener: (errorMsg: String?) -> Unit) {
//        phone	string	必填	手机号
//        purpose	string	必填	用途：1注册，2忘记密码，3设置支付密码
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("phone", phone)
        params.put("purpose", purpose)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(phone + MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.VerifyCode)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data?:"")
                        } else {
                            listener.invoke(null)
                        }
                        context.toast(response.body().message)
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 登录
     * */
    fun login(context: Context, mobile: String, pwd: String, listener: (userId: String?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("phone", mobile)
        params.put("pwd", pwd)
        params.put("deviceType", "1")
//      params.put("jpushToken", JPushInterface.getRegistrationID(this))
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(mobile+pwd, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.Login)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            context.toast(response.body().message)
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 登录
     * */
    fun loginNoDialog(mobile: String, pwd: String,listener: (userId: String?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("phone", mobile)
        params.put("pwd", pwd)
        params.put("deviceType", "1")
//      params.put("jpushToken", JPushInterface.getRegistrationID(this))
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(mobile+pwd, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.Login)
                .params(params)
                .tag(this)
                .execute(object : JsonCallback<NetEntity<String>>() {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 注册
     * @param[phone] string	必填	手机号
     * @param[code] string	必填	验证码
     * @param[pwd] string	必填	密码
     * @param[wechat] string	必填	微信号
     * @param[invitePhone] string	选填	推荐人手机号
     * */
    fun register(context: Context, phone: String, code: String, pwd: String,wechat: String, invitePhone: String, listener: (userId: String?) -> Unit) {
//        phone	string	必填	手机号
//        code	string	必填	验证码
//        pwd	string	必填	密码
//        wechat	string	必填	微信号
//        putPhone	string	选填	推荐人手机号
//        deviceType	string	必填	设备类型：1 Android；2 iOS
//        jpushToken	string	选填	极光推送令牌
//        requestCheck	string	必填	验证请求 "手机号+验证码+秘钥"的 MD5加密

        val params = HashMap<String, String>()
        params.put("phone", phone)
        params.put("code", code)
        params.put("pwd", pwd)
        params.put("wechat", wechat)
        params.put("putPhone", invitePhone)
        params.put("deviceType", "1")
//        params.put("jpushToken", JPushInterface.getRegistrationID(this))
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(phone + code, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.Register)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 忘记密码
     * */
    fun forgetPwd(context: Context, mobile: String, code: String, pwd: String, listener: (user: String?) -> Unit) {
//        phone	string	必填	手机号
//        code	string	必填	验证码
//        pwd	string	必填	密码
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("phone", mobile)
        params.put("code", code)
        params.put("pwd", pwd)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(mobile + code, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.ForgetPwd)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data?:"")
                        } else {
                            listener.invoke(null)
                        }
                        context.toast(response.body().message)
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * banner
     * */
    fun bannerData(context: Context, type :String,listener: (bannersBean:ArrayList< BannersBean>?) -> Unit) {
//        type	string	必填	位置

        val params = HashMap<String, String>()
        params.put("type", type)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<ArrayList< BannersBean>>>(Urls.HomeBanner)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList< BannersBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList< BannersBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList< BannersBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 热销推荐
     * */
    fun mainRecommendData(context: Context, listener: (mainBean: ArrayList<MainRecommendBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("Recommend", salt).toLowerCase())
        OkGo.post<NetEntity<ArrayList<MainRecommendBean>>>(Urls.HomeRecommend)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<MainRecommendBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<MainRecommendBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<MainRecommendBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 热点资讯
     * */
    fun mainNewsData(context: Context, listener: (informationBeans: ArrayList<InformationBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("HotNews", salt).toLowerCase())
        OkGo.post<NetEntity<ArrayList<InformationBean>>>(Urls.HomeHotNews)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<InformationBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<InformationBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<InformationBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 热点资讯列表
     * */
    fun mainNewsListData(context: Context,currentPage: Int, listener: (informationBeans: ArrayList<InformationBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(currentPage.toString(), salt).toLowerCase())
        OkGo.post<NetEntity<ArrayList<InformationBean>>>(Urls.HomeNewsList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<InformationBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<InformationBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<InformationBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 首页商城数据
     * */
    fun mainStoreData(context: Context, listener: (mainStoreBean: MainStoreBean?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("StoreHome", salt).toLowerCase())
        OkGo.post<NetEntity<MainStoreBean>>(Urls.StoreHome)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<MainStoreBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<MainStoreBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<MainStoreBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 积分商城页
     * */
    fun scoreStoreData(context: Context,currentPage:Int, listener: (scoreStoreBean: List<GoodsBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(currentPage.toString(), salt).toLowerCase())
        OkGo.post<NetEntity<List<GoodsBean>>>(Urls.ScoreGoodsList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<GoodsBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<GoodsBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<GoodsBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 积分确认信息
     * */
    fun scoreConfirmData(context: Context,shopId:String, goodsId:String,goodsCount:String, skuid:String, listener: (scoreConfirmBean: ScoreConfirmBean?) -> Unit) {
//        userId	string	必填	用户id
//        shopId	string	必填	店铺id
//        goodsId	string	必填	商品id
//        goodsCount	string	必填	商品数量
//        skuid	string	必填	规格id
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("shopId", shopId)
        params.put("goodsId", goodsId)
        params.put("goodsCount", goodsCount)
        params.put("skuid", skuid)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<ScoreConfirmBean>>(Urls.ScoreOrderInfo)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ScoreConfirmBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ScoreConfirmBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ScoreConfirmBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 创建积分订单
     * */
    fun createScoreOrder(context: Context,addressId:String,scores:String,shopId:String, goodsId:String,goodsCount:String, skuid:String, listener: (orderId: String?) -> Unit) {
//       userId	string	必填	用户id
//       addressId	string	必填	地址id
//       scores	string	必填	积分数
//       shopId	string	必填	店铺id
//       goodsId	string	必填	商品id
//       goodsCount	string	必填	商品数量
//       skuid	string	必填	规格id
//       requestCheck	string	必填	验证请求


        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("addressId", addressId)
        params.put("scores", scores)
        params.put("shopId", shopId)
        params.put("goodsId", goodsId)
        params.put("goodsCount", goodsCount)
        params.put("skuid", skuid)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.CreateScoreOrder)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 店铺主页
     * */
    fun goodsShopData(context: Context,shopId:String, listener: (shopBean: ShopBean?) -> Unit) {
//       userId	string	选填	用户id
//       shopId	string	必填	店铺id
//       requestCheck	string	必填	验证请求
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("shopId", shopId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(shopId, salt).toLowerCase())
        OkGo.post<NetEntity<ShopBean>>(Urls.ShopMain)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ShopBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ShopBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ShopBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 服务确认信息
     */
    fun serviceConfirmData(context: Context,shopId: String, goodsId: String, goodsCount: Long,payMark: String,  listener: (data: ServiceConfirmBean?,index: Long) -> Unit) {
//        userId	string	必填	用户id
//        shopId	string	必填	店铺id
//        goodsId	string	必填	商品id
//        goodsCount	string	必填	商品数量
//        payMark	string	必填	1积分 2金钱
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("shopId",shopId)
        params.put("goodsId", goodsId)
        params.put("goodsCount", goodsCount.toString())
        params.put("payMark", payMark)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<ServiceConfirmBean>>(Urls.VirtuallyOrderInfo)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ServiceConfirmBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ServiceConfirmBean>>) =
                            if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                                listener.invoke(response.body().data,goodsCount)
                            } else {
                                listener.invoke(null,goodsCount)
                            }

                    override fun onError(response: Response<NetEntity<ServiceConfirmBean>>) {
                        super.onError(response)
                        listener.invoke(null,goodsCount)
                    }

                })

    }

    /**
     * 创建服务订单
     * */
    fun createServiceOrder(context: Context,vararg param:String, listener: (orderId: String?) -> Unit) {
//        userId	string	必填	用户id
//        shopId	string	必填	店铺id
//        goodsId	string	必填	商品id
//        goodsCount	string	必填	商品数量
//        payMark	string	必填	1积分 2金钱
//        scores	string	必填	积分数
//        ordersMoney	string	必填	金钱数
//        couponId	string	必填	优惠券id
//        discount	string	必填	优惠金额
//        requestCheck	string	必填	验证请求



        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("shopId", param[0])
        params.put("goodsId", param[1])
        params.put("goodsCount", param[2])
        params.put("payMark", param[3])
        params.put("scores", param[4])
        params.put("ordersMoney", param[5])
        params.put("couponId", param[6])
        params.put("discount", param[7])
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.VirtuallyCreateOrder)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 店铺分类
     * */
    fun goodsShopClassifyData(context: Context,shopId:String, listener: (shopBean: ArrayList<GoodsShopClassifyBean>?) -> Unit) {
//       shopId	string	必填	店铺id
//       requestCheck	string	必填	验证请求
        val params = HashMap<String, String>()
        params.put("shopId", shopId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(shopId, salt).toLowerCase())
        OkGo.post<NetEntity<ArrayList<GoodsShopClassifyBean>>>(Urls.ShopSelfTypeList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<GoodsShopClassifyBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<GoodsShopClassifyBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<GoodsShopClassifyBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 商品详情
     * */
    fun goodsDetailData(context: Context,goodsId:String, listener: (goodsBean: GoodsBean?) -> Unit) {
//        userId	string	选填	用户id
//        goodsId	string	必填	商品id
//        requestCheck	string	必填	验证请求
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("goodsId", goodsId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(goodsId, salt).toLowerCase())
        OkGo.post<NetEntity<GoodsBean>>(Urls.GoodsDetail)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<GoodsBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<GoodsBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<GoodsBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 商品规格
     * */
    fun goodsClassifyData(context: Context,goodsId:String, listener: (goodsBean: SpecBean?) -> Unit) {
//        userId	string	选填	用户id
//        goodsId	string	必填	商品id
//        requestCheck	string	必填	验证请求
        val params = HashMap<String, String>()
        params.put("goodsId", goodsId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(goodsId, salt).toLowerCase())
        OkGo.post<NetEntity<SpecBean>>(Urls.GoodsRank)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<SpecBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<SpecBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<SpecBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 【添加/取消】【关注/收藏】
     * */
    fun editFavoriteData(context: Context,id:String, idMark:String,type:String, listener: (goodsBean: NetEntity<Void>?) -> Unit) {
//        userId	string	必填	用户ID
//        id	string	必填	店铺/商品id(多个id用英文逗号隔开）
//        idMark	string	必填	0:店铺，1:商品
//        type	string	必填	0:取消，1:添加
//        requestCheck	string	必填	验证请求
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("id", id)
        params.put("idMark", idMark)
        params.put("type", type)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+id, salt).toLowerCase())
        OkGo.post<NetEntity<Void>>(Urls.Attention)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(context) {
                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body())
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<Void>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 优惠劵列表
     * */
    fun couponListData(shopId:String, objectId:String, listener: (goodsBean: ArrayList<CouponBean>?) -> Unit) {
//        shopId	string	必填	店铺id
//        objectId	string	必填	商品id/服务id（多个id，用英文逗号隔开）
//        page	string	必填	分页，从第1开始，每页20条
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("shopId", shopId)
        params.put("objectId", objectId)
        params.put("page", "1")
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(shopId, salt).toLowerCase())
        OkGo.post<NetEntity<ArrayList<CouponBean>>>(Urls.CouponList)
                .params(params)
                .tag(this)
                .execute(object : JsonCallback<NetEntity<ArrayList<CouponBean>>>() {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<CouponBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<CouponBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 店铺类目
     * */
    fun serviceClassifyData(context: Context, classMark: String, listener: (serviceListFilterBeans: ArrayList<ServiceListFilterBean>?) -> Unit) {
//        classMark	string	必填	店铺类别：1实物类  2虚拟类
//        requestCheck	string	必填	验证请求
        val params = HashMap<String, String>()
        params.put("classMark", classMark)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(classMark, salt).toLowerCase())
        OkGo.post<NetEntity<ArrayList<ServiceListFilterBean>>>(Urls.ShopCategory)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<ServiceListFilterBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<ServiceListFilterBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<ServiceListFilterBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 服务列表
     * */
    fun serviceListData(context: Context, currentPage: Int, categoryId:String,sequence:String,longitude:String,latitude:String,listener: (scoreStoreBean: List<ServiceShopBean>?) -> Unit) {
//        categoryId	string	必填	店铺分类。【商城】sheet【店铺类目】接口。全部：0
//        sequence	string	必填	排序：0综合排序，1距离优先，2好评优先
//        longitude	string	必填	经度（用户）
//        latitude	string	必填	纬度（用户）
//        page	string	必填	分页，从第1页开始。每页15条数据
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("page", currentPage.toString())
        params.put("categoryId", categoryId)
        params.put("sequence", sequence)
        params.put("longitude",longitude)
        params.put("latitude", latitude)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("ShopList", salt).toLowerCase())
        OkGo.post<NetEntity<List<ServiceShopBean>>>(Urls.ShopList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<ServiceShopBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<ServiceShopBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<ServiceShopBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 服务店铺详情
     * */
    fun serviceShopData(context: Context, shopId:String,listener: (serviceStoreBean: ServiceShopBean?) -> Unit) {
//        userId	string	选填	用户id
//        shopId	string	必填	店铺id
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("shopId", shopId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(shopId, salt).toLowerCase())
        OkGo.post<NetEntity<ServiceShopBean>>(Urls.VirtuallyShopMain)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ServiceShopBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ServiceShopBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ServiceShopBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 服务详情
     * */
    fun serviceGoodsDetailData(context: Context, goodsId:String,longitude:String,latitude:String,listener: (serviceStoreBean:ServiceGoodsBean?) -> Unit) {
//       userId	string	选填	用户id
//        goodsId	string	必填	商品id
//                longitude	string	必填	经度（用户）
//        latitude	string	必填	纬度（用户）
//        requestCheck	string	必填	验证请求


        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("goodsId", goodsId)
        params.put("longitude", longitude)
        params.put("latitude", latitude)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(goodsId, salt).toLowerCase())
        OkGo.post<NetEntity<ServiceGoodsBean>>(Urls.VirtuallyGoodsDetail)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ServiceGoodsBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ServiceGoodsBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ServiceGoodsBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 评价统计
     */
    fun commentCountData(context: Context, id: String, idMark: String, listener: (data: CommentCountBean?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("id", id)
        params.put("idMark", idMark)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(id, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<CommentCountBean>>(Urls.CommentCount)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<CommentCountBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<CommentCountBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<CommentCountBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 商品评价列表
     * */
    fun goodsCommentData(context: Context,currentPage: Int,id: String, idMark: String,type: String,  listener: (scoreStoreBean: List<CommentBean>?) -> Unit) {
//        id	string	必填	商品id
//        idMark	string	必填	1商品 2店铺
//        type	string	必填	评论类型
//        page	string	必填	分页，从第1页开始，每页20条数据
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("id", id)
        params.put("idMark", idMark)
        params.put("type", type)
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(id, salt).toLowerCase())
        OkGo.post<NetEntity<List<CommentBean>>>(Urls.CommentList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<CommentBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<CommentBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<CommentBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 搜索结果页
     * */
    fun searchGoodsResult(context: Context, currentPage: Int, listener: (scoreStoreBean: List<GoodsBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<GoodsBean>>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<GoodsBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<GoodsBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<GoodsBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 商铺搜索结果页
     * */
    fun searchGoodsShopResult(context: Context, currentPage: Int, shopId:String,selfTypeId:String,status:String,search:String,sortType:String,listener: (scoreStoreBean: List<GoodsBean>?) -> Unit) {
//        shopId	string	必填	店铺id
//        selfTypeId	string	选填	店铺自定义商品分类id
//        status	string	选填	1:上新、2:热销
//        search	string	选填	搜索内容(UTF-8编码)
//        sortType	string	必填	排序方式(0:综合排序，1:按价格降序，2:按价格升序，3:按销量排序)
//        page	string	必填	分页，从第1页开始，每页20条数据
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("shopId", shopId)
        params.put("selfTypeId", selfTypeId)
        params.put("status", status)
        params.put("search", search)
        params.put("sortType", sortType)
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(shopId, salt).toLowerCase())
        OkGo.post<NetEntity<List<GoodsBean>>>(Urls.ShopGoodsList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<GoodsBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<GoodsBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<GoodsBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 广告专区
     * */
    fun mainAdsData(context: Context, isPaid: String,currentPage: Int, listener: (scoreStoreBean: List<BannersBean>?) -> Unit) {
//        isPaid	string	必填	0无偿 1有偿
//        page	string	必填	分页，从第1页开始，每页20条数据
//                requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("isPaid",isPaid)
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(currentPage.toString(), salt).toLowerCase())
        OkGo.post<NetEntity<List<BannersBean>>>(Urls.AdsList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<BannersBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<BannersBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<BannersBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 广告专区--广告点击（已登录的用户调用此接口）
     * */
    fun mainAdsData(adsId: String, listener: (msg:String?) -> Unit) {
//        userId	string	必填	用户id
//        adsId	string	必填	广告id
//        requestCheck	string	必填	验证请求 "用户id+广告id+秘钥"的 MD5加密
        val params = HashMap<String, String>()
        params.put("userId",MyApplication.loginUserId)
        params.put("adsId", adsId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+adsId, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.AdsClick)
                .params(params)
                .tag(this)
                .execute(object : JsonCallback<NetEntity<String>>() {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     *红包专区
     * */
    fun mainRedPacketData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<CouponBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<CouponBean>>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<CouponBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<CouponBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<CouponBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 商品订单优惠券领取
     * */
    fun getGoodsCoupon(context: Context, couponId:String, listener: (void: NetEntity<Void>?) -> Unit) {
//        userId	string	必填	用户id
//        couponId	string	必填	优惠劵id
//        requestCheck	string	必填	验证请求
//

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("couponId", couponId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+couponId, salt).toLowerCase())
        OkGo.post<NetEntity<Void>>(Urls.GetCoupon)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(context) {
                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body())
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<Void>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 编辑个人信息
     * */
    fun editPersonInfo(context: Context, key: String, value: String, listener: (data: String?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)//教练id
        if (key == "header")
            params.put(key, EncodeUtils.base64Encode2String(FileIOUtils.readFile2BytesByStream(value)))
        else
            params.put(key, value)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.UpdateUserInfo)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                            context.toast(response.message())
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 是否有支付密码
     */
    fun checkHavePayPwd(context: Context,listener: (data: String?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        val nowMills = TimeUtils.getNowMills().toString()
        params.put("timestamp", nowMills)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+nowMills, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.IsSetPayPwd)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 修改登录密码
     */
    fun changeAccountPwd(context: Context,oldPwd:String ,newPwd:String ,listener: (data: String?) -> Unit) {
//        userId	/**/string	必填	用户ID
//                pwd	string	必填	旧密码
//                newPwd	string	必填	新密码
//                requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("userId", loginUserId)
        params.put("pwd", oldPwd)
        params.put("newPwd", newPwd)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(loginUserId +oldPwd+newPwd, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.ModifyLoginPwd)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        context.toast(response.body().message)
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data?:"")
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 我的余额
     */
    fun accountBalance(context: Context, listener: (data: BalanceBean?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId", loginUserId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(loginUserId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<BalanceBean>>(Urls.MyBalance)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<BalanceBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<BalanceBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<BalanceBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 申请提现
     */
    fun requestWithdrawal(context: Context, price: String, card: String, bankName: String, bankAddress: String, userName: String, userPhone: String, listener: (data: String?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId", loginUserId)
        params.put("applyMoney", price)
        params.put("bankCardNum", card)
        params.put("bankName", bankName)
        params.put("bankAddress", bankAddress)
        params.put("userName", userName)
        params.put("userPhone", userPhone)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(loginUserId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.ApplyWithdraw)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        context.toast(response.body().message)
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data ?: "")
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 余额记录
     * */
    fun balanceRecord(context: Context, currentPage: Int, listener: (scoreStoreBean: List<BalanceRecordBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", loginUserId)
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<List<BalanceRecordBean>>>(Urls.Record)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<BalanceRecordBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<BalanceRecordBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<BalanceRecordBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     *  卡券包列表
     * */
    fun cardPackageListData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<ServiceOrderBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<ServiceOrderBean>>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<ServiceOrderBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<ServiceOrderBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<ServiceOrderBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 卡券包列表详情
     */
    fun cardPackageDetailData(context: Context,listener: (data: ServiceOrderBean?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("id", loginUserId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(loginUserId , MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<ServiceOrderBean>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ServiceOrderBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ServiceOrderBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ServiceOrderBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 商品收藏与取消收藏操作
     * @param context                上下文
     * @param isCollection           1：未收藏，2：已收藏  （给你啥就把状态变成啥）
     * @param listener 是否删除成功
     * @param goodsEntities          实体
     */
    fun favoriteGoodsIsCollection(context: Context, idMark: String, isCollection: String, goodsEntities: Array<GoodsBean>, listener: (goodsEntities: Array<GoodsBean>?) -> Unit) {
        val params = HashMap<String, String>()
        var goodsIds = ""
        for (goodsEntity in goodsEntities) {
            goodsIds += goodsEntity.goodsId + ","
        }
        params.put("userId", MyApplication.loginUserId)
        val subGoodsIds = goodsIds.substring(0, goodsIds.length - 1)
        params.put("id", subGoodsIds)
        params.put("idMark", idMark)//0:店铺，1:商品
        params.put("type", isCollection) //0:取消，1:添加
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+subGoodsIds, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.Attention)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(goodsEntities)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }



    /**
     * 商铺收藏与取消收藏操作
     * @param context                上下文
     * @param isCollection           1：未收藏，2：已收藏  （给你啥就把状态变成啥）
     * @param listener 是否删除成功
     * @param goodsEntities          实体
     */
    fun favoriteShopIsCollection(context: Context, idMark: String, isCollection: String, goodsEntities: Array<GoodsShopBean>, listener: (goodsEntities: Array<GoodsShopBean>?) -> Unit) {
        val params = HashMap<String, String>()
        var goodsIds = ""
        for (goodsEntity in goodsEntities) {
            goodsIds += goodsEntity.shopId + ","
        }
        params.put("userId", MyApplication.loginUserId)
        val subGoodsIds = goodsIds.substring(0, goodsIds.length - 1)
        params.put("id", subGoodsIds)
        params.put("idMark", idMark)//0:店铺，1:商品
        params.put("type", isCollection) //0:取消，1:添加
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+subGoodsIds, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.Attention)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(goodsEntities)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 商品收藏列表
     * */
    fun favoriteGoodsListData(context: Context, state: Int, currentPage: Int, listener: (scoreStoreBean: List<GoodsBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("state", state.toString())
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<List<GoodsBean>>>(Urls.CollectedGoodsList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<GoodsBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<GoodsBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<GoodsBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 商铺收藏列表
     * */
    fun favoriteShopListData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<GoodsShopBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString( MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<List<GoodsShopBean>>>(Urls.CollectedShopList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<GoodsShopBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<GoodsShopBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<GoodsShopBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     *我 的足迹
     * */
    fun footprintData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<FootprintBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<List<FootprintBean>>>(Urls.Footprint)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<FootprintBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<FootprintBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<FootprintBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     *积分变更记录
     * */
    fun mineScoreRecordData(context: Context, currentPage: Int, listener: (scoreRecordBean: List<ScoreRecordBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<List<ScoreRecordBean>>>(Urls.MyScoreRecord)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<ScoreRecordBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<ScoreRecordBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<ScoreRecordBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     *积分兑换订单列表
     * */
    fun scoreOrderListData(context: Context, currentPage: Int, listener: (scoreRecordBean: List<ScoreOrderBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<ScoreOrderBean>>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<ScoreOrderBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<ScoreOrderBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<ScoreOrderBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 积分订单详情
     * */
    fun scoreOrderDetailData(context: Context,orderId:String, listener: (scoreOrderBean: ScoreOrderBean?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("orderId", orderId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<ScoreOrderBean>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ScoreOrderBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ScoreOrderBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ScoreOrderBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 购物车列表
     * */
    fun cartListData(context: Context, currentPage: Int, listener: (goodsCarBean: ArrayList<GoodsCarBean>?) -> Unit) {
//        userId	string	必填	用户id
//        page	string	必填	分页，从第1页开始，每页30个商品
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<ArrayList<GoodsCarBean>>>(Urls.ShopCarList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<GoodsCarBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<GoodsCarBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<GoodsCarBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 一般商品确认信息
     * */
    fun goodsConfirmData(context: Context,info:String, listener: (goodsConfirmBean: GoodsConfirmBean?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("shopInfo", info)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<GoodsConfirmBean>>(Urls.MoneyOrderInfo)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<GoodsConfirmBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<GoodsConfirmBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<GoodsConfirmBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 一般商品确认信息  积分信息
     * */
    fun goodsConfirmScoreData(context: Context,info:String, listener: (goodsConfirmBean: GoodsConfirmScoreBean?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("shopInfo", info)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<GoodsConfirmScoreBean>>(Urls.CanUseScore)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<GoodsConfirmScoreBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<GoodsConfirmScoreBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<GoodsConfirmScoreBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 一般商品生成订单
     * */
    fun createGoodsOrderData(context: Context,addressId:String,scores:String,ordersMoney:String,info:String, listener: (orderId: String?) -> Unit) {
//        userId	string	必填	用户id
//        addressId	string	必填	收货地址id
//        scores	string	必填	抵扣积分数

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("addressId", addressId)
        params.put("scores", scores)
        params.put("ordersMoney", ordersMoney)
        params.put("shopInfo", info)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.CreateMoneyOrder)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     *地址选择列表 15105200983
     * */
    fun addressChooseData(context: Context,  listener: (addressBean: List<AddressBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString( MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<List<AddressBean>>>(Urls.AddressList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<AddressBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<AddressBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<AddressBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 地址编辑，设为默认还是删除
     * */
    fun editAddressState(context: Context,addressId:String, url:String,listener: (addressBean: NetEntity<Void>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("addressId", addressId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+addressId, salt).toLowerCase())
        OkGo.post<NetEntity<Void>>(url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(context) {
                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body())
                        } else {
                            context.toast(response.body().message)
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<Void>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     *  新增收货地址（当用户添加地址时，后台判断该用户是否有其他地址，若没有，将该地址设为默认地址）
     * */
    fun addAddressData(context: Context, name:String, phone:String, provinceId:String, cityId:String, districtId:String, detail:String, listener: (addressBean: String?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("name", name)
        params.put("phone", phone)
        params.put("provinceId", provinceId)
        params.put("cityId", cityId)
        params.put("districtId", districtId)
        params.put("detail", detail)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.AddAddress)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     *  新增收货地址（当用户添加地址时，后台判断该用户是否有其他地址，若没有，将该地址设为默认地址）
     * */
    fun updateAddAddressData(context: Context, addressId:String,name:String, phone:String, provinceId:String, cityId:String, districtId:String, detail:String, listener: (addressBean: String?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("addressId", addressId)
        params.put("name", name)
        params.put("phone", phone)
        params.put("provinceId", provinceId)
        params.put("cityId", cityId)
        params.put("districtId", districtId)
        params.put("detail", detail)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+addressId, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.ModifyAddress)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 搜索结果  --- 筛选菜单数据
     * */
    fun searchFilterData(context: Context,typeId:String,search:String, listener: (searchFilterEntity: ArrayList<SearchFilterEntity>?) -> Unit) {
//        typeId	string	选填	分类id
//      search	string	选填	搜索内容(URL编码)
//      requestCheck	string	必填	验证请求
        val params = HashMap<String, String>()
        if (!TextUtils.isEmpty(typeId))
            params.put("typeId", typeId)
        if (!TextUtils.isEmpty(search))
            params.put("search", search)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("SiftList", salt).toLowerCase())
        OkGo.post<NetEntity<ArrayList<SearchFilterEntity>>>(Urls.SiftList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<SearchFilterEntity>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<SearchFilterEntity>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<SearchFilterEntity>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     *搜索结果  --- 商品列表数据
     * */
    fun searchFilterGoodsData(context: Context, currentPage: Int,typeId:String,search:String,other:String,status:String,filterPopWin:SearchFilterPop, listener: (addressBean: List<GoodsBean>?) -> Unit) {
//       typeId	string	选填	分类id
//       search	string	选填	搜索内容(URL编码)
//       price	string	选填	价格筛选
//       other	string	选填	其他筛选
//       status	string	必填	排序方式(0:综合,1:信用,2:价格降序,3:价格升序,4:销量)
//       page	string	必填	分页，从第1页开始，每页30条数据
//       requestCheck	string	必填	验证请求
        val params = HashMap<String, String>()
        params.put("page", currentPage.toString())
        if (!TextUtils.isEmpty(typeId))
            params.put("typeId", typeId)
        if (!TextUtils.isEmpty(search))
            params.put("search", search)
        if (!TextUtils.isEmpty(filterPopWin.heightPrice)) {
            params.put("price", if (TextUtils.isEmpty(filterPopWin.lowPrice)) "0" + "," + heightPrice else lowPrice.toString() + "," + heightPrice)
        }else if (!TextUtils.isEmpty(filterPopWin.lowPrice)){
             params.put("price", filterPopWin.lowPrice)
        }

        params.put("status", status)


        if (!TextUtils.isEmpty(other)) {
            params.put("other", other.substring(0, "".length - 1))
        }
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(currentPage.toString(), salt).toLowerCase())
        OkGo.post<NetEntity<List<GoodsBean>>>(Urls.GoodsList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<GoodsBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<GoodsBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<GoodsBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     *活动列表
     * */
    fun promotionsListData(context: Context, currentPage: Int,sequence:String,longitude:String?,latitude:String?, listener: (promotionsBean: List<PromotionsBean>?) -> Unit) {
//        userId	string	选填	用户id
//        page	string	必填	分页，从第1页开始，每页10条数据
//        sequence	string	必填	排序：1默认排序,2报名人数由少到多,3报名人数由到到少,4截止日期由近到远,5截止日期由远到近,6加速天数优先,7距离优先
//        longitude	string	必填	经度（用户）
//        latitude	string	必填	纬度（用户）
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("page", currentPage.toString())
        params.put("sequence", sequence)
        params.put("longitude", longitude?:"")
        params.put("latitude",latitude?:"")
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(longitude+latitude, salt).toLowerCase())
        OkGo.post<NetEntity<List<PromotionsBean>>>(Urls.ActivityList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<PromotionsBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<PromotionsBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<PromotionsBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 参加商家活动
     * */
    fun promotionJoin(context: Context,activityId:String, listener: (bean: NetEntity<Void>?) -> Unit) {
//        userId	string	必填	用户id
//        activityId	string	必填	活动id
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("activityId", activityId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString( MyApplication.loginUserId+activityId, salt).toLowerCase())
        OkGo.post<NetEntity<Void>>(Urls.ActivityJoin)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(context) {
                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body())
                        } else {
                            listener.invoke(null)
                        }
                        context.toast(response.body().message)
                    }

                    override fun onError(response: Response<NetEntity<Void>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 商家活动详情
     * */
    fun promotionDetailData(context: Context,activityId:String, longitude:String?,latitude:String?,listener: (promotionsBean: PromotionsBean?) -> Unit) {
//        userId	string	选填	用户id
//        activityId	string	必填	活动id
//        longitude	string	必填	经度（用户）
//        latitude	string	必填	纬度（用户）
//        requestCheck	string	必填	验证请求


        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("activityId", activityId)
        params.put("longitude", longitude?:"")
        params.put("latitude", latitude?:"")
        params.put("requestCheck", EncryptUtils.encryptMD5ToString( activityId, salt).toLowerCase())
        OkGo.post<NetEntity<PromotionsBean>>(Urls.ActivityDetial)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<PromotionsBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<PromotionsBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                        context.toast(response.body().message)
                    }

                    override fun onError(response: Response<NetEntity<PromotionsBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 上传参加的活动图片
     * */
    fun promotionPushData(context: Context,activityId:String, content:String,images:List<String>,listener: (bean: NetEntity<Void>?) -> Unit) {
//        userId	string	必填	用户id
//        activityId	string	必填	活动id
//        content	string	必填	内容
//        image	string	必填	图片
//        requestCheck	string	必填	验证请求
        CustomProgress.show(context, "加载中", false, null)

        Thread{
            pushImgData(images){
                if (it!=null) {
                    val params = HashMap<String, String>()
                    params.put("userId", MyApplication.loginUserId)
                    params.put("activityId", activityId)
                    params.put("content", content)
                    params.put("image", it)
                    params.put("requestCheck", EncryptUtils.encryptMD5ToString( MyApplication.loginUserId+activityId, salt).toLowerCase())
                    OkGo.post<NetEntity<Void>>(Urls.UploadActivity)
                            .params(params)
                            .tag(this)
                            .execute(object : DialogCallback<NetEntity<Void>>(context) {
                                override fun onSuccess(response: Response<NetEntity<Void>>) {
                                    if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                                        listener.invoke(response.body())
                                    } else {
                                        listener.invoke(null)
                                    }
                                    context.toast(response.body().message)
                                }

                                override fun onError(response: Response<NetEntity<Void>>) {
                                    super.onError(response)
                                    listener.invoke(null)
                                }

                            })
                }
            }
        }.start()


    }

    /**
     * 公共方法： 上传图片
     * */
    fun pushImgData(images:List<String>,count:Int=0,listener: (imgName: String?) -> Unit){
//        userId	string	必填	用户id
//        timeStamp	string	必填	时间戳
//        imgBase64	string	必填	图片数据(base64)
//        requestCheck	string	必填	验证请求
        if (images.isNotEmpty()) {
            var str=""

            val params = HashMap<String, String>()
            params.put("userId", MyApplication.loginUserId)
            val time = TimeUtils.getNowMills().toString()
            params.put("timeStamp", time)
            params.put("imgBase64", EncodeUtils.base64Encode2String(FileIOUtils.readFile2BytesByStream(images[count].replace("file:///",""))))
            params.put("requestCheck", EncryptUtils.encryptMD5ToString(  MyApplication.loginUserId+time, salt).toLowerCase())
            OkGo.post<NetEntity<String>>(Urls.UploadImg)
                    .params(params)
                    .tag(this)
                    .execute(object : JsonCallback<NetEntity<String>>() {
                        override fun onSuccess(response: Response<NetEntity<String>>) {
                            if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                                str+=response.body().data+","

                                if (count<images.size-1)
                                    pushImgData(images,count+1){
                                        str+=it
                                        listener.invoke(str)
                                    }
                                else{
                                    listener.invoke(str)
                                }
                            } else {
                                listener.invoke(null)
                            }
                        }

                        override fun onError(response: Response<NetEntity<String>>) {
                            super.onError(response)
                            listener.invoke(null)
                        }

                    })
        }else{
            listener.invoke("")
        }

    }


    /**
     *我的活动列表
     * */
    fun promotionsPersonalData(context: Context, currentPage: Int,state: String, listener: (promotionsBean: List<PromotionsPersonalBean>?) -> Unit) {
//        userId	string	必填	用户id
//        page	string	必填	分页，从第1页开始，每页10条数据
//        state	string	必填	1未开始 2已开始 3审核中 4已通过 5未通过
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params.put("userId",  MyApplication.loginUserId)
        params.put("page",currentPage.toString())
        params.put("state", state)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<List<PromotionsPersonalBean>>>(Urls.MyActivity)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<PromotionsPersonalBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<PromotionsPersonalBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<PromotionsPersonalBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
}