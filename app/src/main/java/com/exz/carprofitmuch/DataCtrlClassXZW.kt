package com.exz.carprofitmuch

import android.content.Context
import com.blankj.utilcode.util.EncryptUtils
import com.exz.carprofitmuch.bean.*
import com.exz.carprofitmuch.config.Urls
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import org.jetbrains.anko.toast
import java.util.*

/**
 * on 2017/10/18.
 */

object DataCtrlClassXZW {

    /**
     * 优惠券列表
     * */
    fun CouponData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<CouponBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
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
     * 红包列表
     * */
    fun MyCommentData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<CommentBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
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
     * 我的评价列表
     * */
    fun RedPackageData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<CouponBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
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
     * 我的订单列表
     * */
    fun MyOrderData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<MyOrderBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<List<MyOrderBean>>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<MyOrderBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<MyOrderBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<MyOrderBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 订单详情
     * */
    fun OrderDetailData(context: Context, listener: (scoreStoreBean: GoodsOrderDetailEntity?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<GoodsOrderDetailEntity>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<GoodsOrderDetailEntity>>(context) {

                    override fun onSuccess(response: Response<NetEntity<GoodsOrderDetailEntity>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<GoodsOrderDetailEntity>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 取消订单
     * */
    fun CancelOrderDetailData(context: Context, orderId: String, listener: (scoreStoreBean: String?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("orderId", orderId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
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
     * 删除订单
     * */
    fun DeleteOrderDetailData(context: Context, orderId: String, listener: (scoreStoreBean: String?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("orderId", orderId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
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
     * 确认收货
     * */
    fun ConfirmOrderDetailData(context: Context, orderId: String, listener: (scoreStoreBean: String?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("orderId", orderId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
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
     * 评价
     * */
    fun ConfirmCommentData(context: Context, orderId: String, listener: (scoreStoreBean: String?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("orderId", orderId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
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
     * 提交退款申请
     * */
    fun SubmitRefundData(context: Context, orderId: String, s: String, cause: String, issue: String, img: String, listener: (scoreStoreBean: String?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("orderId", orderId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
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
     * 提交物流信息
     * */
    fun SubmitLogisticsCompanyData(context: Context, orderId: String, s: String, cause: String, listener: (scoreStoreBean: String?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("orderId", orderId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
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
     * 申请开店信息
     * */
    fun OpenInfoData(context: Context, orderId: String, listener: (List<OpenShopKeyValueBean>?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("orderId", orderId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<List<OpenShopKeyValueBean>>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<OpenShopKeyValueBean>>>(context) {

                    override fun onSuccess(response: Response<NetEntity<List<OpenShopKeyValueBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<OpenShopKeyValueBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })


    }

    /**
     *  我的保单
     * */
    fun MypolicyData(context: Context, currentPage: Int, listener: (scoreStoreBean: List<MyPolicyBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("currentPage", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString("1", MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<List<MyPolicyBean>>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<MyPolicyBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<MyPolicyBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<MyPolicyBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }


    /**
     * 地图大头针数据
     *
     *
     */
    fun MapPinData(context: Context, url: String, longitude: String, latitude: String, listener: (scoreStoreBean: List<MapPinBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("longitude", longitude)
        params.put("latitude", latitude)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(longitude + latitude, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<List<MapPinBean>>>(url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<MapPinBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<MapPinBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<MapPinBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 店铺的宝藏数据
     */
    fun MapShopTreasureData(context: Context, shopId: String, listener: (scoreStoreBean: List<MapIdBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("shopId", shopId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(shopId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<List<MapIdBean>>>(Urls.ShopTreasure)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<MapIdBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<MapIdBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<MapIdBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }


    /**
     * 领取宝藏
     * */
    fun GetTreasureData(context: Context, shopId: String, treasureId: String, listener: (MapGetTreasurePacketBean?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("shopId", shopId)
        params.put("treasureId", treasureId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(shopId + MyApplication.loginUserId + treasureId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<MapGetTreasurePacketBean>>(Urls.GetTreasure)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<MapGetTreasurePacketBean>>(context) {

                    override fun onSuccess(response: Response<NetEntity<MapGetTreasurePacketBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                            context.toast(response.body().message)
                        }
                    }

                    override fun onError(response: Response<NetEntity<MapGetTreasurePacketBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })


    }

    /**
     * 店铺的宝藏数据
     */
    fun MapMapPacketData(context: Context, shopId: String, listener: (scoreStoreBean: List<MapIdBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("shopId", shopId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(shopId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<List<MapIdBean>>>(Urls.ShopPacket)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<MapIdBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<MapIdBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<MapIdBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 领取红包
     * */
    fun GetPacketData(context: Context, shopId: String, packetId: String, listener: (MapGetTreasurePacketBean?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("shopId", shopId)
        params.put("packetId", packetId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(shopId + MyApplication.loginUserId + packetId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<MapGetTreasurePacketBean>>(Urls.GetPacket)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<MapGetTreasurePacketBean>>(context) {

                    override fun onSuccess(response: Response<NetEntity<MapGetTreasurePacketBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                            context.toast(response.body().message)
                        }
                    }

                    override fun onError(response: Response<NetEntity<MapGetTreasurePacketBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })


    }


    /**
     * 我的宝藏
     * */
    fun MyTreasure(context: Context, state: String, currentPage: Int, listener: (scoreStoreBean: List<MyTreasureListBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("state", state)
        params.put("page", currentPage.toString())
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<List<MyTreasureListBean>>>(Urls.MyTreasure)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<MyTreasureListBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<MyTreasureListBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<MyTreasureListBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 我的宝藏
     * */
    fun ShopLevelData(context: Context, classMark: String, listener: (scoreStoreBean: List<ShopLevelBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId", MyApplication.loginUserId)
        params.put("classMark", classMark)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(classMark, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<List<ShopLevelBean>>>(Urls.ShopLevel)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<ShopLevelBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<ShopLevelBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<ShopLevelBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }

    /**
     * 店铺类目
     * */
    fun ShopCategoryData(context: Context, classMark: String, listener: (scoreStoreBean: List<OpenShopKeyValueBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("classMark", classMark)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(classMark, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<List<OpenShopKeyValueBean>>>(Urls.ShopCategory)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<List<OpenShopKeyValueBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<List<OpenShopKeyValueBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<List<OpenShopKeyValueBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }
                })
    }


    /**
     * 资料审核结果
     * */
    fun CheckResultData(context: Context, listener: (scoreStoreBean: CheckResultBean?) -> Unit) {

        val params = HashMap<String, String>()
        params.put("userId",MyApplication.loginUserId)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, MyApplication.salt).toLowerCase())
        OkGo.post<NetEntity<CheckResultBean>>(Urls.CheckResult)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<CheckResultBean>>(context) {

                    override fun onSuccess(response: Response<NetEntity<CheckResultBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<CheckResultBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }



    /**
     * 提交申请资料
     *
     * userId	string	必填	用户id
     * classMark	string	必填	店铺类型：1实体商品类 2虚拟服务类
     * levelId	string	必填	店铺等级id
     * name	string	必填	店铺名称
     * categoryId	string	必填	店铺类目id。【商城】sheet【店铺类目】接口
     * districtId	string	必填	区id
     * detail	string	必填	详细地址
     * longitude	string	必填	经度（用户）
     * latitude	string	必填	纬度（用户）
     * contact	string	必填	联系人
     * idFrontImg	string	必填	身份证正面照（base64)
     * idBackImg	string	必填	身份证反面照（base64)
     * idNum	string	必填	身份证号
     * idName	string	必填	身份证上的姓名
     * businessImg	string	必填	营业执照（base64)
     * */
    fun ConfirmInfoData(context: Context, classMark:String,levelId:String,name:String ,categoryId:String,districtId:String,detail:String,longitude:String,
                        latitude:String,contact:String,idFrontImg:String,idBackImg:String,idNum:String,idName:String,businessImg:String,
                        listener: (scoreStoreBean: String?) -> Unit) {
        val params = HashMap<String, String>()
        params.put("userId",MyApplication.loginUserId)
        params.put("classMark",classMark)
        params.put("levelId",levelId)
        params.put("name",name)
        params.put("categoryId",categoryId)
        params.put("detail",detail)
        params.put("longitude",longitude)
        params.put("latitude",latitude)
        params.put("contact",contact)
        params.put("idFrontImg",idFrontImg)
        params.put("idBackImg",idBackImg)
        params.put("idNum",idNum)
        params.put("idName",idName)
        params.put("businessImg",businessImg)
        params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, MyApplication.salt).toLowerCase())
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


}

