package com.exz.carprofitmuch.module.main.promotion

import android.app.Activity
import android.content.Intent
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.PromotionsAdapter.Companion.setStateColorAndStr
import com.exz.carprofitmuch.bean.PromotionsBean
import com.exz.carprofitmuch.module.MainActivity
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_promotions_detail.*

/**
 * Created by 史忠文
 * on 2017/11/8.
 */
class PromotionsDetailActivity : BaseActivity(), View.OnClickListener {

    private var promotionsBean: PromotionsBean? = null
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = getString(R.string.promotion_detail)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_promotions_detail

    override fun init() {
        bottom_bar.visibility = View.GONE
        img.layoutParams.height = ScreenUtils.getScreenWidth() * 5 / 13
        bt_submit.setOnClickListener(this)
        iniData()
    }

    fun iniData() {
        DataCtrlClass.promotionDetailData(this, intent.getStringExtra(PromotionsDetail_Intent_PromotionId), MainActivity.locationEntity?.longitude, MainActivity.locationEntity?.latitude) {
            if (it != null) {
                promotionsBean = it
                img.setImageURI(it.imgUrl)
                tv_title.text = it.title
                tv_time.text = String.format(getString(R.string.promotions_detail_startTime), it.limitDate)
                tv_speed.text = String.format("%s" + mContext.getString(R.string.DAY), it.dayCount)
                tv_distance.text = it.distance
                myWeb.loadUrl(it.contentUrl)

                tv_peopleCount.text = String.format(it.already + "/" + it.total)
                val already = it.already.toDoubleOrNull() ?: 0.toDouble()
                val total = it.total.toDoubleOrNull() ?: 0.toDouble()
                progressBar.progress= ((already/ total) * 100).toInt()
                bottom_bar.visibility = View.VISIBLE
                setStateColorAndStr(mContext, it.isJoin + it.state+it.isUpload, view = *arrayOf(bt_submit))
            }
        }
    }

    override fun onClick(view: View) {
        if (promotionsBean != null) {
            when (promotionsBean?.isJoin + promotionsBean?.state+ promotionsBean?.isUpload) {
                "010" -> {
                    DataCtrlClass.promotionJoin(this@PromotionsDetailActivity, promotionsBean?.id ?: "") {
                        setResult(Activity.RESULT_OK)
                        iniData() }
                }
                "140" -> {

                    val intent = Intent(this, PromotionsPushActivity::class.java)
                    intent.putExtra(PromotionsDetail_Intent_PromotionId,getIntent().getStringExtra(PromotionsDetail_Intent_PromotionId))
                    intent.putExtra(PromotionsDetail_Intent_PromotionTitle,getIntent().getStringExtra(promotionsBean?.title))
                    intent.putExtra(PromotionsDetail_Intent_PromotionUrl,getIntent().getStringExtra(promotionsBean?.contentUrl))
                    startActivityForResult(intent,100)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK)
            iniData()
        }
    }
    companion object {
        val PromotionsDetail_Intent_PromotionId = "PromotionsDetail_Intent_PromotionId"
        val PromotionsDetail_Intent_PromotionTitle = "PromotionsDetail_Intent_PromotionTitle"
        val PromotionsDetail_Intent_PromotionUrl = "PromotionsDetail_Intent_PromotionUrl"
    }

}