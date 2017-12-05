package com.exz.carprofitmuch.pop

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.chad.library.adapter.base.BaseQuickAdapter
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsDetailClassifyAdapter
import com.exz.carprofitmuch.adapter.GoodsDetailClassifyAdapter.Companion.GoodsDetailClassify
import com.exz.carprofitmuch.bean.GoodsBean
import com.exz.carprofitmuch.bean.GoodsClassifyBean
import com.exz.carprofitmuch.bean.GoodsSubClassifyBean
import com.exz.carprofitmuch.bean.SpecBean
import com.exz.carprofitmuch.module.main.store.score.ScoreConfirmActivity
import com.exz.carprofitmuch.module.main.store.score.ScoreConfirmActivity.Companion.ScoreConfirm_Intent_Ids
import com.exz.carprofitmuch.utils.DialogUtils
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.hwangjr.rxbus.RxBus
import com.hwangjr.rxbus.annotation.Subscribe
import com.hwangjr.rxbus.annotation.Tag
import com.hwangjr.rxbus.thread.EventThread
import com.szw.framelibrary.view.CustomLoadMoreView
import com.szw.framelibrary.view.preview.PreviewActivity
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_IMAGES
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_SHOW_NUM
import kotlinx.android.synthetic.main.pop_classify.view.*
import org.jetbrains.anko.toast
import razerdp.basepopup.BasePopupWindow
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by 史忠文
 * on 2017/1/24.
 */

class GoodsDetailClassifyPop(private val context: Activity, private val listener: (str: String) -> Unit) : BasePopupWindow(context), View.OnClickListener {


    var poolId: String = "0"
    var image: String = ""
    var countIndex: Long = 1
    private val decimalFormat: DecimalFormat
    private val adapter: GoodsDetailClassifyAdapter<GoodsClassifyBean>
    private  var data: SpecBean?=null
    private var maxCount: Long = 0
    private lateinit var inflate: View
    private var state = STATE_NORMAL
    private var clickAble = true
    private var  goodsBean: GoodsBean?=null

    companion object {
        var STATE_NORMAL = "state_normal"//正常  指定状态，区别跳转积分确认，或商品确认
        const val SCORE_STATE_NORMAL = "score_state_normal"//积分正常
        const val GOODS_STATE_NORMAL = "goods_state_normal"//商品正常
        const val STATE_NO_INVENTORY = "state_no_inventory"//无库存
    }

    init {
        try {
            RxBus.get().register(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        popupWindow.isClippingEnabled = false
        decimalFormat = DecimalFormat("0")
        adapter = GoodsDetailClassifyAdapter()
        adapter.bindToRecyclerView(inflate.mRecyclerView)
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN)
        adapter.setLoadMoreView(CustomLoadMoreView())
        inflate.mRecyclerView.layoutManager = LinearLayoutManager(getContext())
        inflate.mRecyclerView.addItemDecoration(RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(getContext(), R.color.White)))
    }

    fun setNewData(data: SpecBean?,goodsBean: GoodsBean) {
        this.data = data
        this.goodsBean=goodsBean
        adapter.setNewData(data?.rankInfo)
        if (goodsBean.mainImgs.size > 0) {
            image = goodsBean.mainImgs[0]
            inflate.img.setImageURI(image)
        }
        inflate.price.text =String.format("${context.getString(R.string.CNY)}%s",goodsBean.goodsPrice)
        inflate.inventory.text = String.format(context.getString(R.string.classify_pop_inventory), if (TextUtils.isEmpty(goodsBean.allStock)) "0" else goodsBean.allStock)
        var chooseStr = if (data?.rankInfo?.size?:0 < 1) "" else context.getString(R.string.classify_pop_chooseStr_please)
        for (goodsClassifyBean in data?.rankInfo?: ArrayList()) {
            chooseStr += " "+goodsClassifyBean.rankName
        }
        maxCount = (if (TextUtils.isEmpty(goodsBean.allStock)) "0" else goodsBean.allStock).toLong()
        chooseClassify(chooseStr, 0)
    }

    private fun chooseClassify(choose: String?, index: Int?) {
        var chooseStr = if (choose?.isEmpty() != false) context.getString(R.string.classify_pop_chooseStr_default) else choose
        if (data?.rankInfo?.size?:0 > 0) {
            chooseStr = context.getString(R.string.classify_pop_chooseStr)
            val selectArray = arrayOfNulls<String>(data?.rankInfo?.size?:0)
            Thread {
                for (indice in data?.rankInfo?.indices?: IntRange(0,0)) {//遍历最外层数组
                    val goodsSubClassify = data?.rankInfo?.get(indice)?.subRank//子类型数据
                    if (goodsSubClassify?.size?:0 > 0) {//子类型数据不止一条
                        if (index == null) { //适配器选择设置数据

                            for (indie in goodsSubClassify?.indices?: IntRange(0,0)) {//遍历子类型数据
                                if (goodsSubClassify?.get(indie)?.goodsSubState == GoodsSubClassifyBean.STATE_1) {// 如果被选中
                                    chooseStr +=  " "+goodsSubClassify[indie].rankName  //已选择子类型的名称
                                    selectArray[indice] = goodsSubClassify[indie].rankId//已选择子类型的id集合
                                }
                            }

                        } else {//指定选择设置数据  默认选择每一种的第一个
                            goodsSubClassify?.get(0)?.goodsSubState= GoodsSubClassifyBean.STATE_1
                            chooseStr += " "+ goodsSubClassify?.get(0)?.rankName
                            selectArray[indice] = goodsSubClassify?.get(0)?.rankId

                        }
                    }
                }
                Arrays.sort(selectArray)
                context.runOnUiThread {
                    //判断两个数组是不是相等，相等就各种赋值
                    inflate.type.text = chooseStr
                    listener.invoke(chooseStr)
                    for (goodsClassifyPoolBean in data?.rankComb?:ArrayList()) {
                        val poolArray = goodsClassifyPoolBean.skuid.split(",").toTypedArray()
                        Arrays.sort(poolArray)
                        if (Arrays.equals(selectArray, poolArray)) {
                            poolId = goodsClassifyPoolBean.rankCombId
                            image = goodsClassifyPoolBean.image
                            inflate.img.setImageURI(image)
                            inflate.price.text = String.format("${context.getString(R.string.CNY)}%s",goodsClassifyPoolBean.price)
                            inflate.inventory.text = String.format(context.getString(R.string.classify_pop_inventory), if (TextUtils.isEmpty(goodsClassifyPoolBean.stock)) "0" else goodsClassifyPoolBean.stock)
                            maxCount = (if (TextUtils.isEmpty(goodsClassifyPoolBean.stock)) "0" else goodsClassifyPoolBean.stock).toLong()
                            onNum(countIndex)
                        }
                    }

                }
            }.start()
        }
        state = if (maxCount == 0L) STATE_NO_INVENTORY else STATE_NORMAL
        clickAble = initBtn()
        inflate.type.text = chooseStr
        listener.invoke(chooseStr)
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = arrayOf(Tag(GoodsDetailClassify)))
    fun classifySelected(tag: String) {
        chooseClassify(null, null)
    }

    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View {
        inflate = View.inflate(getContext(), R.layout.pop_classify, null)
        inflate.bt_close.setOnClickListener(this)
        inflate.add.setOnClickListener(this)
        inflate.count.setOnClickListener(this)
        inflate.minus.setOnClickListener(this)
        inflate.addCar.setOnClickListener(this)
        inflate.buy.setOnClickListener(this)
        inflate.img.setOnClickListener(this)
        return inflate
    }

    override fun initAnimaView(): View = findViewById(R.id.animationView)

    override fun initShowAnimation(): Animation {
        val shakeAnimate = TranslateAnimation(0f, 0f, 600f, 0f)
        shakeAnimate.duration = 300
        return shakeAnimate
    }

    override fun initExitAnimation(): Animation {
        val shakeAnimate = TranslateAnimation(0f, 0f, 0f, 900f)
        shakeAnimate.duration = 300
        return shakeAnimate
    }

    /**
     * 设置操作按钮文字，显示隐藏，不同状态最终返回是否可点击
     *
     */
    private fun initBtn(): Boolean {
        when (state) {
            SCORE_STATE_NORMAL -> {
                inflate.addCar.visibility = View.GONE
                inflate.buy.visibility = View.VISIBLE
                inflate.buy.text = context.getString(R.string.classify_pop_score_confirm)
                return true
            }
            GOODS_STATE_NORMAL -> {
                inflate.addCar.visibility = View.VISIBLE
                inflate.buy.visibility = View.VISIBLE
                inflate.buy.text = context.getString(R.string.classify_pop_buyNow)
                return true
            }
            else -> {
                inflate.addCar.visibility = View.GONE
                inflate.buy.visibility = View.VISIBLE
                inflate.buy.text = context.getString(R.string.classify_pop_noInventory)
                return false
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.minus -> {
                countIndex = if (countIndex <= 1) 1 else --countIndex
                inflate.count.text = decimalFormat.format(countIndex)
            }
            R.id.add -> {
                if (countIndex + 1 > maxCount) {
                    context.toast(context.getString(R.string.classify_pop_toast_outOfDex))
                    return
                }
                countIndex += 1
                inflate.count.text = decimalFormat.format(countIndex)
            }
            R.id.count -> DialogUtils.changeNum(context, countIndex, {
                onNum(it)
            })
            R.id.addCar ->//添加购物车，一般商品才会用到
                if (clickAble)
                    if (TextUtils.isEmpty(poolId) && adapter.data.size > 0) {
                        context.toast(context.getString(R.string.classify_pop_toast_chooseType))
                    } else {
                        if (maxCount == 0L) {
                            context.toast(context.getString(R.string.classify_pop_toast_noInventory))
                            return
                        }
                        //                    GoodsCarFragment.addCar(context, poolId, countIndex + "", data);
                        dismiss()
                    }
            R.id.buy ->
                if (clickAble)
                    if (TextUtils.isEmpty(poolId) && adapter.data.size > 0) {
                        context.toast(context.getString(R.string.classify_pop_toast_chooseType))
                    } else {
                        if (maxCount == 0L) {
                            context.toast(context.getString(R.string.classify_pop_toast_noInventory))
                            return
                        }
                        //判断是商品还是积分。区别跳转
                        when (state) {
                            SCORE_STATE_NORMAL -> {
//                                shopId	string	必填	店铺id
//                                goodsId	string	必填	商品id
//                                goodsCount	string	必填	商品数量
//                                skuid	string	必填	规格id

                                val intent = Intent(context, ScoreConfirmActivity::class.java)
                                intent.putExtra(ScoreConfirm_Intent_Ids,goodsBean?.shopId+","+goodsBean?.goodsId+","+countIndex+","+poolId)
                                context.startActivity(intent)
                            }
                            GOODS_STATE_NORMAL -> {

                            }
                        }


                        //                    Intent intent = new Intent(context, BillingInfoActivity.class);
                        //                    intent.putExtra(Intent_GoodsCar_GoodsId, data.getGoodsId());
                        //                    intent.putExtra(Intent_GoodsCar_GoodsCount, countIndex + "");
                        //                    intent.putExtra(Intent_GoodsCar_PoolId, poolId);
                        //                    context.startActivity(intent);
                        dismiss()
                    }
            R.id.img ->{
                val intent = Intent(context, PreviewActivity::class.java)
                val images=ArrayList<String>()
                images.add(image)
                intent.putExtra(PREVIEW_INTENT_IMAGES, images)
                intent.putExtra(PREVIEW_INTENT_SHOW_NUM, false)
                context.startActivity(intent)
            }
            R.id.bt_close -> dismiss()
        }
    }

    /**
     * 当重新选择商品属性，和输入修改数量时调用。
     */
    private fun onNum(it: Long) {
        countIndex = if (it > maxCount) maxCount else it
        if (countIndex == 0L) {
            if (maxCount >= 0) {
                countIndex = 1
            }
        }
        this@GoodsDetailClassifyPop.inflate.count.text = decimalFormat.format(countIndex)
    }
}
