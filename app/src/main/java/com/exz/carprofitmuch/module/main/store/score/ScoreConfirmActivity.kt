package com.exz.carprofitmuch.module.main.store.score

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.AddressBean
import com.exz.carprofitmuch.bean.ScoreConfirmBean
import com.exz.carprofitmuch.module.mine.address.AddressAddOrUpdateActivity
import com.exz.carprofitmuch.module.mine.address.AddressChooseActivity
import com.exz.carprofitmuch.module.mine.goodsorder.GoodsOrderActivity
import com.exz.carprofitmuch.utils.DialogUtils
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_score_confirm.*
import kotlinx.android.synthetic.main.layout_address.*


/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class ScoreConfirmActivity : BaseActivity(),View.OnClickListener {
    var data= ScoreConfirmBean()
    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.score_confirm_name)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)


        return false
    }

    override fun setInflateId(): Int= R.layout.activity_score_confirm

    override fun init() {
        val scoreConfirmAddressDetail = getString(R.string.score_confirm_address_detail)
        val msp = SpannableString(scoreConfirmAddressDetail+"更换当前号码将从手机发送一条普通短信进行验证")
        msp.setSpan(ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.MaterialGrey700)), 0, scoreConfirmAddressDetail.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_address.text = msp



        initEvent()
        initData()
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }
        bt_confirm.setOnClickListener(this)
        lay_noAddress.setOnClickListener(this)
        bt_address.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0) {
            bt_address -> {//地址选择
                startActivityForResult(Intent(this, AddressChooseActivity::class.java),100)
            }
            lay_noAddress -> {//地址选择
                val intent = Intent(this@ScoreConfirmActivity, AddressAddOrUpdateActivity::class.java)
                intent.putExtra(AddressAddOrUpdateActivity.Intent_AddressType, AddressAddOrUpdateActivity.address_type_3)
                startActivityForResult(intent,200)
            }
            bt_confirm -> {
                if (data.totalScore<data.score)
                DataCtrlClass.createScoreOrder(this,data.address?.id?:"",data.totalScore.toString(),data.shopInfo?.shopId?:"",data.shopInfo?.goodsInfo?.goodsId?:"",data.shopInfo?.goodsInfo?.goodsCount?:"",data.shopInfo?.goodsInfo?.skuid?:""){
                    if (it!=null){
                        DialogUtils.scorePaySuccess(this)  {
                            startActivity(Intent(this, GoodsOrderActivity::class.java))
                            finish()
                        }
                    }
                }
                else{
                    DialogUtils.scorePayFailed(this,getString(R.string.score_pay_failed_default_reason))
                }
            }

        }
    }
     private fun initData() {
        val ids=(intent.getStringExtra(ScoreConfirm_Intent_Ids)?:"").split(",")

        DataCtrlClass.scoreConfirmData(this,ids[0],ids[1],ids[2],ids[3]) {
            if (it != null) {
                this.data=it
                setAddress(it)
                val goodsInfo = it.shopInfo?.goodsInfo
                img.setImageURI(goodsInfo?.imgUrl)
                tv_goodsName.text=goodsInfo?.goodsName
                tv_score_count.text=String.format("%s"+getString(R.string.SCORE),goodsInfo?.goodsPrice)
                tv_goodsCount.text=goodsInfo?.goodsCount
                tv_goodsType.text=goodsInfo?.goodsType
                try {
                    val totalPrice = goodsInfo?.goodsPrice?.toDouble()?.times(goodsInfo.goodsCount.toDouble())
                    it.totalScore=totalPrice?:0.toDouble()
                    tv_goodsTotalScore.text=String.format("%s"+getString(R.string.SCORE), totalPrice)
                } catch (e: Exception) {
                }
                DataCtrlClassXZW.myScoreData(this){
                    tv_goodsCanUseScore.text=String.format("%s"+getString(R.string.SCORE), it?.score)
                    this.data.score=it?.score?.toDouble()?:0.toDouble()
                }
            }
        }
    }
    private fun setAddress(it: ScoreConfirmBean) {
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
        var ScoreConfirm_Intent_Ids="ScoreConfirm_Intent_Ids"
    }
}