package com.exz.carprofitmuch.module.main.store.normal

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.CompoundButton
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsConfirmAdapter
import com.exz.carprofitmuch.adapter.GoodsConfirmBean
import com.exz.carprofitmuch.bean.AddressBean
import com.exz.carprofitmuch.bean.GoodsConfirmSubBean
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Intent_Finish_Type_2
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Pay_Intent_Finish_Type
import com.exz.carprofitmuch.module.main.pay.PayMethodsActivity.Companion.Pay_Intent_OrderId
import com.exz.carprofitmuch.module.mine.address.AddressAddOrUpdateActivity
import com.exz.carprofitmuch.module.mine.address.AddressChooseActivity
import com.exz.carprofitmuch.module.mine.goodsorder.GoodsOrderActivity
import com.exz.carprofitmuch.pop.GoodsConfirmCouponPop
import com.exz.carprofitmuch.pop.GoodsConfirmExpressPop
import com.google.gson.Gson
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_goods_confirm.*
import kotlinx.android.synthetic.main.layout_address.*
import java.text.DecimalFormat

/**
 * Created by 史忠文
 * on 2017/10/17.
 * 确认订单页
 *
 *   立即购买修改数量暂时搁置
 *
 */

class GoodsConfirmActivity : BaseActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        data.scoreInfo?.isSelect=p1
        initData(data)
    }

    lateinit var mAdapter: GoodsConfirmAdapter<GoodsConfirmSubBean>
    var data= GoodsConfirmBean()
    val format = DecimalFormat("0.00")
    lateinit var couponPop: GoodsConfirmCouponPop
    lateinit var expressPop: GoodsConfirmExpressPop

    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.goods_confirm_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)


        return false
    }

    override fun setInflateId(): Int = R.layout.activity_goods_confirm

    override fun init() {

        initEvent()
        initRecycler()
        initPop()
        initData()

    }
    private fun initData(){
        DataCtrlClass.goodsConfirmData(mContext, intent.getStringExtra(GoodsConfirm_Intent_shopInfo)?:"") {
            if (it != null) {
//                val it= GoodsConfirmBean()
//                it.address= AddressBean()
//                it.score= GoodsConfirmScoreBean()
//                val arrayList = ArrayList<GoodsConfirmSubBean>()
//                val confirmSubBean = GoodsConfirmSubBean()
//                val goodsInfo = ArrayList<GoodsBean>()
//                goodsInfo.add(GoodsBean())
//                confirmSubBean.goodsInfo= goodsInfo
//                val coupons = ArrayList<CouponBean>()
//                coupons.add(CouponBean(couponPrice = "2",couponFullPrice = "2"))
//                confirmSubBean.goodsCoupons= coupons
//                val express = ArrayList<ExpressBean>()
//                express.add(ExpressBean("顺丰","10"))
//                express.add(ExpressBean())
//                confirmSubBean.sendWays= express
//                arrayList.add(confirmSubBean)
//                it.goodsConfirmSubs= arrayList





                //设置地址
                setAddress(it)

                //默认优惠券选择第一个
                it.shopInfo.filterNot { goodsConfirmSub -> goodsConfirmSub.couponInfo.any { it.isCheck } }.forEach {
                    it.couponId=it.couponInfo.firstOrNull()?.couponId?:"0"
                    it.discount=it.couponInfo.firstOrNull()?.discount?:"0"
                    it.couponInfo.firstOrNull()?.isCheck = true }
                it.shopInfo.filterNot { goodsConfirmSub -> goodsConfirmSub.transportInfo.any { it.isCheck } }.forEach {
                    it.transportId=it.transportInfo.firstOrNull()?.transportId?:"0"
                    it.transportMoney=it.transportInfo.firstOrNull()?.transportMoney?:"0"
                    it.transportInfo.firstOrNull()?.isCheck = true }

//                设置数据
                this.data = it
                initData(data)
                mAdapter.setNewData(it.shopInfo)
            }
        }
    }
    private fun setAddress(it: GoodsConfirmBean) {
        val addressBean = it.address
        if (addressBean != null) {
            lay_noAddress.visibility = View.GONE
            bt_address.visibility = View.VISIBLE

            tv_userName.text = addressBean.name
            tv_userPhone.text = addressBean.phone

            val scoreConfirmAddressDetail = getString(R.string.score_confirm_address_detail)
            val msp = SpannableString(scoreConfirmAddressDetail + addressBean.toString())
            msp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.MaterialGrey700)), 0, scoreConfirmAddressDetail.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            tv_address.text = msp
        } else {
            lay_noAddress.visibility = View.VISIBLE
            bt_address.visibility = View.GONE
        }
    }

    private fun initPop() {
        couponPop = GoodsConfirmCouponPop(this){
            refreshScore()
        }
        expressPop = GoodsConfirmExpressPop(this){
            refreshScore()
        }

    }

    /**
     * 更新积分信息
     */
    private fun refreshScore(){
        DataCtrlClass.goodsConfirmScoreData(this,Gson().toJson(data.shopInfo)){
            if (it!=null){
                it.isSelect=data.scoreInfo?.isSelect?:false
                data.scoreInfo=it
                initData(data)
                mAdapter.notifyDataSetChanged()
            }
        }
    }
    private fun initRecycler() {
        mAdapter = GoodsConfirmAdapter()
        val arrayList = ArrayList<GoodsConfirmSubBean>()
        mAdapter.setNewData(arrayList)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.isFocusable = false
        mRecyclerView.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                when (view.id) {
//                    R.id.minus -> if (mAdapter.data[position].goodsCount.toInt() > 1) {
//                        changeCount(mAdapter.data[position].goodsInfo[0], (mAdapter.data[position].goodsCount.toInt() - 1).toLong()){}
//                    }
//                    R.id.add -> changeCount(mAdapter.data[position].goodsInfo[0], (mAdapter.data[position].goodsCount.toInt() + 1).toLong()){}
//                    R.id.count -> DialogUtils.changeNum(mContext, mAdapter.data[position].goodsCount.toLong()){
//                        changeCount(mAdapter.data[position].goodsInfo[0], it){}
//                    }
                    R.id.bt_coupon -> {
                        //更改优惠券。刷新数据
                        couponPop.data = mAdapter.data[position]
                        couponPop.showPopupWindow()
                    }
                    R.id.bt_sendWay -> {
                        //更改配送方式。刷新数据
                        expressPop.data = mAdapter.data[position]
                        expressPop.showPopupWindow()
                    }
                    else -> {
                    }
                }
            }
        })
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        lay_noAddress.setOnClickListener(this)
        bt_address.setOnClickListener(this)
        bt_confirm.setOnClickListener(this)
        accumulatePoints.setOnCheckedChangeListener(this)

    }

    /**
     * 计算总价格
     * return 总价格
     */
    //总价格
    var totalPrice=0.toDouble()
    private fun initData(data: GoodsConfirmBean): String {
        //设置积分
        accumulatePoints.visibility = if (data.scoreInfo != null&&data.scoreInfo?.money?.toDoubleOrNull()!=0.toDouble()) View.VISIBLE else View.GONE
        accumulatePoints.text = data.scoreInfo?.toString(this)

        //总价格 置零
        totalPrice = 0.toDouble()
        for (item in data.shopInfo) {
            //计算单组商品总价格
            var itemPrice = item.goodsInfo.sumByDouble { it.goodsCount.toDouble() * it.goodsPrice.toDouble() }
            //除去优惠券金额
            val couponPrice = item.couponInfo.firstOrNull { it.isCheck }?.discount?.toDoubleOrNull()?:0.toDouble()
            item.coupon = item.couponInfo.firstOrNull{ it.isCheck }.toString()
            //是否有可选优惠券
            item.isShowCoupon = item.couponInfo.isNotEmpty()
            itemPrice -= couponPrice
            //加上运费
            val sendPrice = item.transportInfo.firstOrNull { it.isCheck }?.transportMoney?.toDoubleOrNull()?:0.toDouble()
            item.sendWay = item.transportInfo.firstOrNull { it.isCheck }.toString()
            //是否有配送方式
            item.isShowExpress = item.transportInfo.isNotEmpty()
            itemPrice += sendPrice

            //避免负数
            itemPrice = if (itemPrice < 0) 0.toDouble() else itemPrice

            //给一组总价格赋值
            item.totalPrice = itemPrice
            //给一组商品数量赋值
            item.goodsCount = item.goodsInfo.sumBy { it.goodsCount.toInt() }.toString()

            //计入总价
            totalPrice += itemPrice
        }
        //减去积分对应的金额
        totalPrice -= if (data.scoreInfo?.isSelect == true) (data.scoreInfo?.money ?: "0").toDouble() else 0.toDouble()

        //避免负数
        totalPrice = if (totalPrice < 0) 0.toDouble() else totalPrice

        //给总价格赋值
        tv_totalPrice.text = String.format("${getString(R.string.CNY)}%s",format.format(totalPrice))

        return format.format(totalPrice)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            bt_address -> {//地址选择
                startActivityForResult(Intent(this,AddressChooseActivity::class.java),100)
            }
            lay_noAddress -> {//地址选择
                val intent = Intent(this@GoodsConfirmActivity, AddressAddOrUpdateActivity::class.java)
                intent.putExtra(AddressAddOrUpdateActivity.Intent_AddressType, AddressAddOrUpdateActivity.address_type_3)
                startActivityForResult(intent,200)
            }
            bt_confirm -> {
                DataCtrlClass.createGoodsOrderData(this,data.address?.addressId?:"",if (data.scoreInfo?.isSelect==true)data.scoreInfo?.scores?:"0" else "0",totalPrice.toString(),Gson().toJson(data.shopInfo)){
                    if (it!=null){
                        if (totalPrice==0.toDouble()){
                            //跳转订单
                            startActivity(Intent(this,GoodsOrderActivity::class.java))
                        }else{
                            //跳转支付
                            val intent = Intent(this, PayMethodsActivity::class.java)
                            intent.putExtra(Pay_Intent_OrderId, it)
                            intent.putExtra(Pay_Intent_Finish_Type, Intent_Finish_Type_2)
                            startActivity(intent)
                        }
                        finish()
                    }
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK==resultCode) {
            when (requestCode) {
                100 -> {
                    this.data.address=data?.getSerializableExtra(AddressChooseActivity.Intent_Result_Address) as AddressBean
                    //设置地址
                    setAddress(this.data)
                }
                200 -> initData()
            }

        }
    }
    companion object {
        var GoodsConfirm_Intent_shopInfo="GoodsConfirm_Intent_shopInfo"
    }

}