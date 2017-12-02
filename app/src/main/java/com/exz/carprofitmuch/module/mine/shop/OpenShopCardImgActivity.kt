package com.exz.carprofitmuch.module.mine.shop

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.OpenShopCardImgBean
import com.exz.carprofitmuch.module.mine.shop.OpenShopActivity.Companion.RESULTCODE_OPEN_SHOP
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.imageloder.GlideImageLoader
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_open_shop_card_img.*
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast

/**
 * Created by pc on 2017/11/23.
 * 身份证上传
 */

class OpenShopCardImgActivity : BaseActivity(), View.OnClickListener {


    private var imgType = ""
    private var entity = OpenShopCardImgBean()
    override fun initToolbar(): Boolean {
        mTitle.text = "身份证上传"
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        return false
    }


    override fun setInflateId(): Int {
        return R.layout.activity_open_shop_card_img
    }

    override fun init() {
        super.init()
        initView()
        initCamera()
    }

    private fun initView() {

        entity=intent.getSerializableExtra("cardImg") as OpenShopCardImgBean
        if(!TextUtils.isEmpty(entity.cardImg)) car_img.setImageURI(if(entity.cardImg.contains("http")) entity.cardImg else "file://"+entity.cardImg)
        if(!TextUtils.isEmpty(entity.cardBackImg)) car_back_img.setImageURI(if(entity.cardBackImg.contains("http")) entity.cardBackImg else "file://"+entity.cardBackImg)
        ed_car_name.setText(entity.cardName)
        ed_card_num.setText(entity.cardNum)
        car_img.setOnClickListener(this)
        car_back_img.setOnClickListener(this)
        tv_submit.setOnClickListener(this)

        if(entity.cardNameCheck.equals("0")){
            ed_car_name.textColor=ContextCompat.getColor(mContext,R.color.Red)
        }

        if(entity.cardNumCheck.equals("0")){
            ed_card_num.textColor=ContextCompat.getColor(mContext,R.color.Red)
        }


        ed_car_name.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                ed_car_name.textColor=ContextCompat.getColor(mContext,R.color.MaterialGrey800)
                entity.cardNameCheck="1"
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        ed_card_num.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                ed_card_num.textColor=ContextCompat.getColor(mContext,R.color.MaterialGrey800)
                entity.cardNumCheck="1"
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }

    override fun onClick(p0: View) {
        when (p0) {
            car_img -> {
                imgType = "1"
                PermissionCameraWithCheck(Intent(this@OpenShopCardImgActivity, ImageGridActivity::class.java), false)
            }
            car_back_img -> {
                imgType = "2"
                PermissionCameraWithCheck(Intent(this@OpenShopCardImgActivity, ImageGridActivity::class.java), false)
            }
            tv_submit -> {
                if (TextUtils.isEmpty(entity.cardImg)) {
                    mContext.toast("请上传身份证正面!")
                    return
                }
                if (entity.cardImgCheck.equals("0")) {
                    mContext.toast("请修改身份证正面!")
                    return
                }
                if (TextUtils.isEmpty(entity.cardBackImg)) {
                    mContext.toast("请上传身份证反面!")
                    return
                }
                if (entity.cardBackImgCheck.equals("0")) {
                    mContext.toast("请修改身份证反面!")
                    return
                }
                var carName = ed_car_name.text.toString().trim()
                if (TextUtils.isEmpty(carName)) {
                    ed_car_name.setShakeAnimation()
                    return
                }
                if (entity.cardNameCheck.equals("")) {
                    mContext.toast("请修改身份证姓名!")
                    return
                }
                entity.cardName=carName
                var cardNum = ed_card_num.text.toString().trim()
                if (TextUtils.isEmpty(cardNum)) {
                    ed_car_name.setShakeAnimation()
                    return
                }
                if (entity.cardNameCheck.equals("0")) {
                    mContext.toast("请修改身份号码!")
                    return
                }
                entity.cardNum=cardNum
                var b=Bundle()
                b.putSerializable("cardImg",entity)
                setResult(RESULTCODE_OPEN_SHOP,Intent().putExtras(b))
                finish()
            }


        }
    }

    private fun initCamera() {
        var imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader = GlideImageLoader()
        //显示相机
        imagePicker.isShowCamera = true
        //是否裁剪
        imagePicker.isCrop = true
        //是否按矩形区域保存裁剪图片
        imagePicker.isSaveRectangle = true
        //圖片緩存
        imagePicker.imageLoader = GlideImageLoader()
        imagePicker.isMultiMode = false//多选
        //矩形尺寸
        imagePicker.style = CropImageView.Style.RECTANGLE
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, resources.displayMetrics).toInt()
        imagePicker.focusWidth = width
        imagePicker.focusHeight = width
        //圖片輸出尺寸
        imagePicker.outPutX = width
        imagePicker.outPutY = width
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) { //图片选择
            val images = data?.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<*>
            if (imgType.equals("1")) {
                car_img.setImageURI("file://" + (images.get(0) as ImageItem).path)
                entity.cardImg =  (images.get(0) as ImageItem).path
                entity.cardImgCheck="1"
            } else if (imgType.equals("2")) {
                car_back_img.setImageURI("file://" +  (images.get(0) as ImageItem).path)
                entity.cardBackImg = (images.get(0) as ImageItem).path
                entity.cardBackImgCheck="1"
            }
        }
    }


}
