package com.exz.carprofitmuch

import android.content.Context
import com.blankj.utilcode.util.EncryptUtils
import com.exz.carprofitmuch.bean.CommentBean
import com.exz.carprofitmuch.bean.CouponBean
import com.exz.carprofitmuch.bean.GoodsOrderDetailEntity
import com.exz.carprofitmuch.bean.MyOrderBean
import com.exz.carprofitmuch.config.Urls
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
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
     * 我的评价列表
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
     * 确认收货
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


}

