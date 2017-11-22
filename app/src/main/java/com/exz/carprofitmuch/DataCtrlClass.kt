package com.exz.carprofitmuch

import android.content.Context
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.FileIOUtils
import com.exz.carprofitmuch.adapter.GoodsConfirmBean
import com.exz.carprofitmuch.bean.*
import com.exz.carprofitmuch.config.Urls
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.app.MyApplication.Companion.loginUserId
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import org.jetbrains.anko.toast
import java.util.*

/**
 * Created by 史忠文
 * on 2017/10/18.
 */

object DataCtrlClass {
    /**
     * 获取验证码
     * */
    fun getSecurityCode(context: Context, mobile: String, attribute: String, listener: (errorMsg: String?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("mobile", mobile)
        params.put("attribute", attribute)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(mobile + MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
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
    fun login(context: Context, mobile: String, pwd: String, listener: (user: User?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("phone", mobile)
        params.put("pwd", pwd)
        params.put("loginDeviceType", "0")
//      params.put("jpushToken", JPushInterface.getRegistrationID(this))
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(mobile, salt).toLowerCase())
        OkGo.post<NetEntity<User>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<User>>(context) {
                    override fun onSuccess(response: Response<NetEntity<User>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<User>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 注册
     * */
    fun register(context: Context, mobile: String, code: String, pwd: String, invitePhone: String, listener: (user: User?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("phone", mobile)
        params.put("code", code)
        params.put("pwd", pwd)
        params.put("invitePhone", invitePhone)
        params.put("loginDeviceType", "0")
//        params.put("jpushToken", JPushInterface.getRegistrationID(this))
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(mobile + code, salt).toLowerCase())
        OkGo.post<NetEntity<User>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<User>>(context) {
                    override fun onSuccess(response: Response<NetEntity<User>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<User>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 忘记密码
     * */
    fun forgetPwd(context: Context, mobile: String, code: String, pwd: String, pwd2: String, listener: (user: User?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("phone", mobile)
        params.put("code", code)
        params.put("pwd", pwd)
        params.put("pwd2", pwd2)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(mobile + code, salt).toLowerCase())
        OkGo.post<NetEntity<User>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<User>>(context) {
                    override fun onSuccess(response: Response<NetEntity<User>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                        context.toast(response.body().message)
                    }

                    override fun onError(response: Response<NetEntity<User>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 首页数据
     * */
    fun mainData(context: Context, listener: (mainBean: MainBean?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<MainBean>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<MainBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<MainBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<MainBean>>) {
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
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<MainStoreBean>>(Urls.url)
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
    fun scoreStoreData(context: Context, listener: (scoreStoreBean: List<ScoreStoreBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<ScoreStoreBean>>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<ScoreStoreBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<ScoreStoreBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<ScoreStoreBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 积分详情
     * */
    fun scoreGoodsDetailData(context: Context, listener: (scoreStoreBean: List<ScoreStoreBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<ScoreStoreBean>>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<ScoreStoreBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<ScoreStoreBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<ScoreStoreBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 服务列表
     * */
    fun serviceListData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<ServiceStoreBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<ServiceStoreBean>>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<ServiceStoreBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<ServiceStoreBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<ServiceStoreBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 服务评价列表
     * */
    fun serviceCommentData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<CommentBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<CommentBean>>>(Urls.url)
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
     * 商品评价列表
     * */
    fun goodsCommentData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<CommentBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<CommentBean>>>(Urls.url)
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
    fun searchGoodsShopResult(context: Context, currentPage: Int, listener: (scoreStoreBean: List<GoodsBean>?) -> Unit) {

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
     * 广告专区
     * */
    fun mainAdsData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<String>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<String>>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<String>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<String>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<String>>>) {
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
    fun getGoodsCoupon(context: Context, listener: (scoreStoreBean: String?) -> Unit) {

        val params = HashMap<String, String>()
//        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.url)
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
     * 编辑个人信息
     * */
    fun editPersonInfo(context: Context, key: String, value: String, listener: (data: String?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)//教练id
        if (key == "headerImg")
            params.put(key, EncodeUtils.base64Encode2String(FileIOUtils.readFile2BytesByStream(value)))
        else
            params.put(key, value)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.url)
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
     * 是否有支付密码
     */
    fun checkHavePayPwd(context: Context,listener: (data: String?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("id", MyApplication.loginUserId)
        val time = Calendar.getInstance(Locale.CHINA).timeInMillis.toString()
        params.put("timestamp", time)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + time, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.url)
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
        val params = HashMap<String, String>()
        params.put("id", loginUserId)
        params.put("oldPwd", oldPwd)
        params.put("newPwd", newPwd)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(loginUserId , MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.url)
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
    fun accountBalance(context: Context,listener: (data: String?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("id", loginUserId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(loginUserId , MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data?:"0.00")
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
     * 申请提现
     */
    fun requestWithdrawal(context: Context,price: String,card: String,bankName: String,bankAddress: String,userName: String,userPhone: String,listener: (data: String?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("price", price)
        params.put("card", card)
        params.put("bankName", bankName)
        params.put("bankAddress", bankAddress)
        params.put("userName", userName)
        params.put("userPhone", userPhone)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(loginUserId , MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.url)
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
     * 余额记录
     * */
    fun balanceRecord(context: Context, currentPage: Int, listener: (scoreStoreBean: List<BalanceRecordBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<BalanceRecordBean>>>(Urls.url)
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
    } /**
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
    fun favoriteGoodsIsCollection(context: Context, isCollection: String, goodsEntities: Array<GoodsBean>, listener: (goodsEntities:Array<GoodsBean>?) -> Unit) {
        val params = HashMap<String, String>()
        var goodsIds = ""
        for (goodsEntity in goodsEntities) {
            goodsIds += goodsEntity.id + ","
        }
        params.put("userId", MyApplication.loginUserId)
        val subGoodsIds = goodsIds.substring(0, goodsIds.length - 1)
        params.put("goodsIds", subGoodsIds)
        params.put("isCollection", isCollection)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(subGoodsIds , MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.url)
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
    fun favoriteShopIsCollection(context: Context, isCollection: String, goodsEntities: Array<GoodsShopBean>, listener: (goodsEntities:Array<GoodsShopBean>?) -> Unit) {
        val params = HashMap<String, String>()
        var goodsIds = ""
        for (goodsEntity in goodsEntities) {
            goodsIds += goodsEntity.id + ","
        }
        params.put("userId", MyApplication.loginUserId)
        val subGoodsIds = goodsIds.substring(0, goodsIds.length - 1)
        params.put("goodsIds", subGoodsIds)
        params.put("isCollection", isCollection)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(subGoodsIds , MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<String>>(Urls.url)
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
    fun favoriteGoodsListData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<GoodsBean>?) -> Unit) {

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
     * 商铺收藏列表
     * */
    fun favoriteShopListData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<GoodsShopBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<GoodsShopBean>>>(Urls.url)
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
    fun footprintData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<GoodsBean>?) -> Unit) {

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
     *积分变更记录
     * */
    fun mineScoreRecordData(context: Context, currentPage: Int, listener: (scoreRecordBean: List<ScoreRecordBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<ScoreRecordBean>>>(Urls.url)
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
    } /**
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

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<ArrayList<GoodsCarBean>>>(Urls.url)
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
     * 积分订单详情
     * */
    fun goodsConfirmData(context: Context,info:String, listener: (goodsConfirmBean: GoodsConfirmBean?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("info", info)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<GoodsConfirmBean>>(Urls.url)
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
     *地址选择列表
     * */
    fun addressChooseData(context: Context, currentPage: Int, listener: (addressBean: List<AddressBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<List<AddressBean>>>(Urls.url)
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
    fun editAddressState(context: Context,info:String, listener: (addressBean: AddressBean?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("info", info)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", salt).toLowerCase())
        OkGo.post<NetEntity<AddressBean>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<AddressBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<AddressBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<AddressBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
}