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
import com.exz.carprofitmuch.adapter.GoodsConfirmScoreBean
import com.exz.carprofitmuch.bean.*
import com.exz.carprofitmuch.module.mine.address.AddressAddOrUpdateActivity
import com.exz.carprofitmuch.module.mine.address.AddressChooseActivity
import com.exz.carprofitmuch.pop.GoodsConfirmCouponPop
import com.exz.carprofitmuch.pop.GoodsConfirmExpressPop
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_goods_confirm.*
import kotlinx.android.synthetic.main.layout_address.*
import razerdp.basepopup.BasePopupWindow
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
        data.score?.isSelect=p1
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
        DataCtrlClass.goodsConfirmData(mContext, "") {
            if (it == null) {
                val it= GoodsConfirmBean()
                it.address= AddressBean()
                it.score= GoodsConfirmScoreBean()
                val arrayList = ArrayList<GoodsConfirmSubBean>()
                val confirmSubBean = GoodsConfirmSubBean()
                val goods = ArrayList<GoodsBean>()
                goods.add(GoodsBean())
                confirmSubBean.goods= goods
                val coupons = ArrayList<CouponBean>()
                coupons.add(CouponBean(couponPrice = "2",couponFullPrice = "2"))
                confirmSubBean.goodsCoupons= coupons
                val express = ArrayList<ExpressBean>()
                express.add(ExpressBean("顺丰","10"))
                express.add(ExpressBean())
                confirmSubBean.sendWays= express
                arrayList.add(confirmSubBean)
                it.goodsConfirmSubs= arrayList





                //设置地址
                setAddress(it)
                //设置积分
                accumulatePoints.visibility = if (it.score != null) View.VISIBLE else View.GONE
                accumulatePoints.text = it.score?.toString(this)
                //默认优惠券选择第一个
                it.goodsConfirmSubs.filterNot { goodsConfirmSub -> goodsConfirmSub.goodsCoupons.any { it.isCheck } }.forEach { it.goodsCoupons.first().isCheck = true }
                it.goodsConfirmSubs.filterNot { goodsConfirmSub -> goodsConfirmSub.sendWays.any { it.isCheck } }.forEach { it.sendWays.first().isCheck = true }

//                设置数据
                this.data = it
                initData(data)
                mAdapter.setNewData(it.goodsConfirmSubs)
            }
        }
    }
    private fun setAddress(it: GoodsConfirmBean) {
        val addressBean = it.address
        if (addressBean != null) {
            lay_noAddress.visibility = View.GONE
            bt_address.visibility = View.VISIBLE

            tv_userName.text = addressBean.userName
            tv_userPhone.text = addressBean.userPhone

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
        couponPop = GoodsConfirmCouponPop(this)
        expressPop = GoodsConfirmExpressPop(this)

        val popDismiss: BasePopupWindow.OnDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                initData(data)
                mAdapter.notifyDataSetChanged()
            }
        }
        couponPop.onDismissListener = popDismiss
        expressPop.onDismissListener = popDismiss
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
//                        changeCount(mAdapter.data[position].goods[0], (mAdapter.data[position].goodsCount.toInt() - 1).toLong()){}
//                    }
//                    R.id.add -> changeCount(mAdapter.data[position].goods[0], (mAdapter.data[position].goodsCount.toInt() + 1).toLong()){}
//                    R.id.count -> DialogUtils.changeNum(mContext, mAdapter.data[position].goodsCount.toLong()){
//                        changeCount(mAdapter.data[position].goods[0], it){}
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
    fun initData(data: GoodsConfirmBean): String {
        //总价格
        var totalPrice = 0.toDouble()
        for (item in data.goodsConfirmSubs) {
            //计算单组商品总价格
            var itemPrice = item.goods.sumByDouble { it.goodsCount.toDouble() * it.price.toDouble() }
            //除去优惠券金额
            val couponPrice = item.goodsCoupons.first { it.isCheck }.couponPrice.toDoubleOrNull()
            item.coupon = item.goodsCoupons.first { it.isCheck }.toString()
            //是否有可选优惠券
            item.isShowCoupon = item.goodsCoupons.isNotEmpty()
            itemPrice -= couponPrice?:0.toDouble()
            //加上运费
            val sendPrice = item.sendWays.first { it.isCheck }.expressPrice.toDoubleOrNull()
            item.sendWay = item.sendWays.first { it.isCheck }.toString()
            //是否有配送方式
            item.isShowExpress = item.sendWays.isNotEmpty()
            itemPrice += sendPrice?:0.toDouble()

            //避免负数
            itemPrice = if (itemPrice < 0) 0.toDouble() else itemPrice

            //给一组总价格赋值
            item.totalPrice = itemPrice
            //给一组商品数量赋值
            item.goodsCount = item.goods.sumBy { it.goodsCount.toInt() }.toString()

            //计入总价
            totalPrice += itemPrice
        }
        //减去积分对应的金额
        totalPrice -= if (data.score?.isSelect == true) (data.score?.scorePrice ?: "0").toDouble() else 0.toDouble()

        //避免负数
        totalPrice = if (totalPrice < 0) 0.toDouble() else totalPrice

        //给总价格赋值
        tv_totalPrice.text = format.format(totalPrice)

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

}