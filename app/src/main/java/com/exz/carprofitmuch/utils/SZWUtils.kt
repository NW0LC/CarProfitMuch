package com.exz.carprofitmuch.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.R.id.progressBar
import com.exz.carprofitmuch.bean.AbsMark
import com.exz.carprofitmuch.bean.GenderBean
import com.exz.carprofitmuch.bean.ServiceListFilterBean
import com.exz.carprofitmuch.module.login.LoginActivity
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity
import com.exz.carprofitmuch.module.main.store.normal.GoodsDetailActivity.Companion.GoodsDetail_Intent_GoodsId
import com.exz.carprofitmuch.module.main.store.normal.GoodsShopActivity
import com.exz.carprofitmuch.module.main.store.normal.GoodsShopActivity.Companion.GoodsShop_Intent_ShopId
import com.exz.carprofitmuch.module.main.store.score.ScoreGoodsDetailActivity
import com.exz.carprofitmuch.module.main.store.service.ServiceDetailActivity
import com.exz.carprofitmuch.module.main.store.service.ServiceDetailActivity.Companion.Service_Intent_ServiceId
import com.exz.carprofitmuch.module.main.store.service.ServiceShopActivity
import com.exz.carprofitmuch.module.main.store.service.ServiceShopActivity.Companion.ServiceShop_Intent_ServiceShopId
import com.exz.carprofitmuch.widget.MyWebActivity
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Title
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Url
import com.lzy.okgo.utils.HttpUtils.runOnUiThread
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.config.Constants.Result.Intent_ClassName
import kotlinx.android.synthetic.main.layout_progress_score.view.*
import java.text.DecimalFormat

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
object SZWUtils {
    /**
     * @param phoneNum 电话号码
     * @return 有隐藏中间
     */
    fun hideMidPhone(phoneNum: String): String {

        return if (TextUtils.isEmpty(phoneNum))
            "暂无电话"
        else if (phoneNum.length != 11)
            phoneNum
        else
            phoneNum.substring(0, 3) + "****" + phoneNum.substring(phoneNum.length - 4, phoneNum.length)
    }

    /**
     * @param mContext 上下文
     * @param intent   事件
     * @return true登录
     */
    fun checkLogin(mContext: Fragment, intent: Intent = Intent(), clazzName: String = ""): Boolean {
        return if (!MyApplication.checkUserLogin()) {
            val login = Intent(mContext.context, LoginActivity::class.java)
            if (clazzName.isNotEmpty()) {
                login.putExtra(Intent_ClassName, clazzName)
            }
            login.putExtras(intent)
            mContext.startActivityForResult(login, 0xc8)
            mContext.activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out)
            false
        } else {
            try {
                mContext.startActivityForResult(intent, 0xc8)
            } catch (e: Exception) {
            }
            true
        }
    }
    /**
     * @param mContext 上下文
     * @param intent   事件
     * @return true登录
     */
    fun checkLogin(mContext: Activity, intent: Intent = Intent(), clazzName: String = ""): Boolean {
        return if (!MyApplication.checkUserLogin()) {
            val login = Intent(mContext, LoginActivity::class.java)
            if (clazzName.isNotEmpty()) {
                login.putExtra(Intent_ClassName, clazzName)
            }
            login.putExtras(intent)
            mContext.startActivityForResult(login, 0xc8)
            mContext.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out)
            false
        } else {
            try {
                mContext.startActivityForResult(intent, 0xc8)
            } catch (e: Exception) {
            }
            true
        }
    }


    fun matcherSearchTitle(textView: TextView, textStart: String, start: Int, end: Int, color: Int) {
        var builder = SpannableStringBuilder(textStart)
        var span = ForegroundColorSpan(color)
        builder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(builder)
    }

    /**
     * string base64
     *  base64 转bitmap
     */
    fun stringtoBitmap(string: String): Bitmap? {
        //将字符串转换成Bitmap类型
        var bitmap: Bitmap? = null
        try {
            val bitmapArray: ByteArray
            bitmapArray = Base64.decode(string, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap
    }





    /**
     * 设置首页 积分进度
     * [progressBar] 进度条
     * [parentLayout] 上方显示的布局的父布局
     * [originalX] 上方显示的布局的原始x坐标
     * [totalScore] 总积分
     * [unlockScore] 解锁积分
     * [realScore] 车险获得积分
     * [start] 实际进度开始位置
     * [end] 实际进度结束位置
     * [secondStart] 可行进度开始位置
     * [secondEnd]  可行进度结束位置
     * [animatorSpeed] 实际进度动画执行速度
     * [secondAnimatorSpeed] 可行进度动画执行速度
     * [delayMillis] 所有动画延迟多久后执行
     * [isHidden] isHidden  Fragment 隐藏与否
     * [repetition] repetition 是否重复
     * [onFinishListener] 动画完成后调用  true 从零开始，false  从上次开始
     */
    private var totalScore = 0f
    private var unlockScore = 0f
    private var realScore = 0f
    var start = 0
    private var end = 60
    var secondStart = 0
    private var secondEnd = 80
    private var originalX = 0f
    private var animatorSpeed = 30L
    private var secondAnimatorSpeed = 15L
    private var delayMillis = 200L
    private var tagThread = Thread {}
    private var secondThread = Thread {}
    fun resetProgress(progressBar: ProgressBar, parentLayout: View, realScore: Float = SZWUtils.realScore, unlockScore: Float = SZWUtils.unlockScore, totalScore: Float = SZWUtils.totalScore, animatorSpeed: Long = SZWUtils.animatorSpeed, secondAnimatorSpeed: Long = SZWUtils.secondAnimatorSpeed, delayMillis: Long = SZWUtils.delayMillis, reset: Boolean = false, onFinishListener: () -> Unit) {
        if (tagThread.isAlive)
            return

        if (reset) {
            runOnUiThread {
                if (originalX != 0f)
                    originalX = parentLayout.bar_tag.x
                val x = ObjectAnimator.ofFloat(parentLayout.bar_tag, "x", parentLayout.bar_tag.x, originalX)
                val animatorSet = AnimatorSet()
                animatorSet.play(x)
                animatorSet.duration = 0
                animatorSet.start()
                progressBar.progress=0
                progressBar.secondaryProgress=0
                SZWUtils.start=0
                parentLayout.bar_tag.bar_tag_text.text ="0.00"
            }
        }


        end = ((realScore / totalScore) * 100).toInt()
        secondEnd = ((unlockScore / totalScore) * 10).toInt() * 10
        parentLayout.tv_totalScore.text = totalScore.toString()
        progressBar.postDelayed({
            secondThread = Thread {
                for (i in secondStart..secondEnd step 10) {
                    progressBar.secondaryProgress = i
                    Thread.sleep(secondAnimatorSpeed)
                }
                resetProgress(progressBar, parentLayout.bar_tag, realScore, start, end, animatorSpeed, reset, onFinishListener)
            }
            secondThread.start()
        }, delayMillis)

    }

    private fun resetProgress(progressBar: ProgressBar, tagView: LinearLayout, realScore: Float, start: Int, end: Int, animatorSpeed: Long, reset: Boolean, listener: () -> Unit) {
        var endNum = if (end <= 5) 5 else end
        if (end == 0) {
            endNum = 0
        }
        val widthUnit = progressBar.measuredWidth.toFloat() / progressBar.max
        tagThread = Thread {
            if (start+endNum!=0)
            for (i in start..endNum) {
                Log.i("progressNum:", i.toString())
                if (i != 0 && start != end||reset)
                    runOnUiThread {
                        if (originalX != 0f)
                            originalX = tagView.x
                        val nextX = tagView.x + widthUnit
                        Log.i("widthUnit:", nextX.toString() + "," + widthUnit)
                        val x = ObjectAnimator.ofFloat(tagView, "x", tagView.x, nextX)
                        val animatorSet = AnimatorSet()
                        animatorSet.play(x)
                        animatorSet.duration = 0
                        animatorSet.start()
                        val decimalFormat = DecimalFormat("0.00")
                        tagView.bar_tag_text.text = decimalFormat.format((realScore / if (end==0)1 else end) * i )
                    }
                if (i >= 5)
                    progressBar.progress = i
                Thread.sleep(animatorSpeed)

            }
            SZWUtils.start = end
            SZWUtils.secondStart = secondEnd

            listener.invoke()
        }
        tagThread.start()
    }

    /**
     * 增加固定外边距
     */
    fun setMargin(view: View, size: Float) {
        val lp = view.layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.topMargin += SizeUtils.dp2px(size)
        }

        view.layoutParams = lp

    }

    /**
     * 增加固定内边距
     */
    fun setPaddingSmart(view: View, size: Float) {
        val lp = view.layoutParams
        if (lp != null && lp.height > 0) {
            lp.height += SizeUtils.dp2px(size)
        }
        view.setPadding(view.paddingLeft, view.paddingTop + SizeUtils.dp2px(size), view.paddingRight, view.paddingBottom)

    }

    /**
     * 减少固定外边距
     */
    fun minusMargin(view: View, size: Float) {
        val lp = view.layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.topMargin -= SizeUtils.dp2px(size)
        }

        view.layoutParams = lp

    }

    /**
     * 减少固定内边距
     */
    fun minusPaddingSmart(view: View, size: Float) {
        val lp = view.layoutParams
        if (lp != null && lp.height > 0) {
            lp.height -= SizeUtils.dp2px(size)
        }
        view.setPadding(view.paddingLeft, view.paddingTop - SizeUtils.dp2px(size), view.paddingRight, view.paddingBottom)

    }

    /**
     * 获取搜索商品搜索结果分类筛选数据
     */
    fun getSearchGoodsResultClassifyData(): ArrayList<ServiceListFilterBean> {
        val filterBeans = ArrayList<ServiceListFilterBean>()
        filterBeans.add(ServiceListFilterBean("1", "汽车美容"))
        filterBeans.add(ServiceListFilterBean("2", "维修保养"))
        filterBeans.add(ServiceListFilterBean("3", "汽车配件"))
        filterBeans.add(ServiceListFilterBean("4", "汽车用品"))
        return filterBeans
    }

    /**
     * 获取服务列表排序筛选数据
     */
    fun getServiceListSortData(): ArrayList<ServiceListFilterBean> {
        val filterBeans = ArrayList<ServiceListFilterBean>()
        filterBeans.add(ServiceListFilterBean("0", "综合排序"))
        filterBeans.add(ServiceListFilterBean("1", "距离优先"))
        filterBeans.add(ServiceListFilterBean("2", "好评优先"))
        return filterBeans
    }

    /**
     * 获取商家活动列表页排序筛选数据
     * 排序：1默认排序,2报名人数由少到多,3报名人数由到到少,4截止日期由近到远,5截止日期由远到近,6加速天数优先,7距离优先
     */
    fun getPromotionsSortData(): ArrayList<ServiceListFilterBean> {
        val filterBeans = ArrayList<ServiceListFilterBean>()
        filterBeans.add(ServiceListFilterBean("1", "综合排序"))
        filterBeans.add(ServiceListFilterBean("2", "报名人数由少到多"))
        filterBeans.add(ServiceListFilterBean("3", "报名人数由到到少"))
        filterBeans.add(ServiceListFilterBean("4", "截止日期由近到远"))
        filterBeans.add(ServiceListFilterBean("5", "截止日期由远到近"))
        return filterBeans
    }
    /**
     * 获取商品搜索结果排序筛选数据
     */
    fun getSearchGoodsResultSortData(): ArrayList<ServiceListFilterBean> {
//        排序方式(0:综合,1:信用,2:价格降序,3:价格升序,4:销量)
        val filterBeans = ArrayList<ServiceListFilterBean>()
        filterBeans.add(ServiceListFilterBean("0", "综合排序"))
        filterBeans.add(ServiceListFilterBean("1", "信用"))
        filterBeans.add(ServiceListFilterBean("2", "价格降序"))
        filterBeans.add(ServiceListFilterBean("3", "价格升序"))
        return filterBeans
    }

    /**
     * 获取性别数据
     */
    fun getGenderData(): ArrayList<GenderBean> {
        val genderList = ArrayList<GenderBean>()
        genderList.add(GenderBean("0", "保密"))
        genderList.add(GenderBean("1", "男"))
        genderList.add(GenderBean("2", "女"))
        return genderList
    }

    /**
     * 设置灰色还是绿色 箭头
     *
     * @param b true grey  ; false green
     */
    fun setGreyOrGreen(context: Context, view: RadioButton, b: Boolean) {
        if (b) {
            view.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.selector_service_list_triangle_grey), null)
            view.setTextColor(ContextCompat.getColor(context, R.color.MaterialGrey600))
        } else {
            view.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.selector_service_list_triangle_green), null)
            view.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }
    }

    /**
     * 数字过万 变万
     */
    fun getSoldCount(mContext: Context, count: String): String {
        return if (count.isNotEmpty()) {
            val numStr = count.toInt().toString()
            if (numStr.length >= 5) {
                String.format(mContext.getString(R.string.service_list_serviceGoods_soldCount_unit), numStr.substring(0, numStr.length - 4), numStr.substring(numStr.length - 4, numStr.length - 3))
            } else {
                count
            }
        } else {
            ""
        }
    }

    /**
     * 底部button布局滑动从底部,出现or隐藏
     */
    class MyNestedScrollListener(private var bottom_bar: View, private var h: Int) : NestedScrollView.OnScrollChangeListener {

        private var differY = 0f
        private var bottomBarY = (ScreenUtils.getScreenHeight() - h).toFloat()
        override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
            val differ = scrollY - oldScrollY
            differY = if ((differY + differ) in 0..h) {
                differY + differ
            } else {
                when {differY + differ > h -> h.toFloat()
                    differY + differ < 0 -> 0f
                    else -> 0f
                }
            }
            val nextY = bottomBarY + differY
            val y = ObjectAnimator.ofFloat(bottom_bar, "y", bottom_bar.y, nextY)
            val animatorSet = AnimatorSet()
            animatorSet.play(y)
            animatorSet.duration = 0
            animatorSet.start()

        }

    }

    /**
     * 设置刷新 及控制 刷新头 的显示和隐藏
     *
     */
    fun setRefreshAndHeaderCtrl(listener: OnRefreshListener, header: View, refreshLayout: SmartRefreshLayout) {

        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(headerView: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                if (offset == 0)
                    header.visibility = View.GONE
                else
                    header.visibility = View.VISIBLE
            }

            override fun onHeaderReleasing(headerView: RefreshHeader?, percent: Float, offset: Int, footerHeight: Int, extendHeight: Int) {
                if (offset == 0)
                    header.visibility = View.GONE
            }
        })
        refreshLayout.setOnRefreshListener(listener)
    }


    /**
     * Banner  和广告的跳转 意图
     */
    fun <T:AbsMark>getIntent(context: Context, item: T): Intent? {
//            1虚拟类商品 2金钱实物类商品 3积分实物类商品 4虚拟类店铺 5实物类店铺 6网页
        var intent: Intent?=null
        when (item.getMarkId()) {
            "1" -> {
                intent = Intent()
                intent.setClass(context, ServiceDetailActivity::class.java)
                intent.putExtra(Service_Intent_ServiceId, item.getMarkId())
            }
            "2" -> {
                intent = Intent()
                intent.setClass(context, GoodsDetailActivity::class.java)
                intent.putExtra(GoodsDetail_Intent_GoodsId, item.getMarkId())
            }
            "3" -> {
                intent = Intent()
                intent.setClass(context, ScoreGoodsDetailActivity::class.java)
                intent.putExtra(GoodsDetail_Intent_GoodsId, item.getMarkId())
            }
            "4" -> {
                intent = Intent()
                intent.setClass(context, ServiceShopActivity::class.java)
                intent.putExtra(ServiceShop_Intent_ServiceShopId, item.getMarkId())
            }
            "5" -> {
                intent = Intent()
                intent.setClass(context, GoodsShopActivity::class.java)
                intent.putExtra(GoodsShop_Intent_ShopId, item.getMarkId())
            }
            "6" -> {
                intent = Intent()
                intent.setClass(context, MyWebActivity::class.java)
                intent.putExtra(Intent_Url, item.getAdUri())
                intent.putExtra(Intent_Title,"")
            }
            else -> {

            }
        }
        if (item.getAdsID().isNotEmpty())
            DataCtrlClass.mainAdsData(item.getAdsID()){}
        return intent
    }
    /**
     * 商城首页跳转
     */
    fun <T:AbsMark>getMarkIntent(context: Context, item: T): Intent? {
        var intent: Intent?=null
        when (item.getIdM()+item.getClassM()+item.getPayM()) {
            "111","1"->{//积分
                intent = Intent()
                intent.setClass(context, ScoreGoodsDetailActivity::class.java)
                intent.putExtra(GoodsDetail_Intent_GoodsId, item.getMarkId())
            }
            "112","2"->{//商品
                intent = Intent()
                intent.setClass(context, GoodsDetailActivity::class.java)
                intent.putExtra(GoodsDetail_Intent_GoodsId, item.getMarkId())
            }
            "121","122"->{//服务
                intent = Intent()
                intent.setClass(context, ServiceDetailActivity::class.java)
                intent.putExtra(Service_Intent_ServiceId, item.getMarkId())
            }
            "211","212"->{//商品店铺
                intent = Intent()
                intent.setClass(context, GoodsShopActivity::class.java)
                intent.putExtra(GoodsShop_Intent_ShopId, item.getMarkId())
            }
            "221","222"->{//服务店铺
                intent = Intent()
                intent.setClass(context, ServiceShopActivity::class.java)
                intent.putExtra(ServiceShop_Intent_ServiceShopId, item.getMarkId())
            }

        }
        return intent
    }
}