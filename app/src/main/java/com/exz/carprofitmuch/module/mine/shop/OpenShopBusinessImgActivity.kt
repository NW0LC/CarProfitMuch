package com.exz.carprofitmuch.module.mine.shop

import android.content.Intent
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import com.exz.carprofitmuch.R
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.imageloder.GlideImageLoader
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_open_business_img.*
import org.jetbrains.anko.toast

/**
 * Created by pc on 2017/11/23.
 * 营业执照
 */

class OpenShopBusinessImgActivity : BaseActivity(), View.OnClickListener {


    private var img = ""
    override fun initToolbar(): Boolean {
        mTitle.text = "营业执照上传"
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        StatusBarUtil.setMargin(this, llImg)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        return false
    }

    override fun setInflateId(): Int {
        return R.layout.activity_open_business_img
    }

    override fun init() {
        super.init()
        initView()
        initCamera()
    }

    private fun initView() {

        if(intent.hasExtra("userIcon"))img ="file://"+ intent.getStringExtra("userIcon")
        if (!TextUtils.isEmpty(img)) iv_img.setImageURI(img)
        iv_img.setOnClickListener(this)
        tv_submit.setOnClickListener(this)

    }

    override fun onClick(p0: View) {
        when (p0) {
            iv_img -> {
                PermissionCameraWithCheck(Intent(this@OpenShopBusinessImgActivity, ImageGridActivity::class.java), false)
            }
            tv_submit -> {
                if (TextUtils.isEmpty(img)) {
                    mContext.toast("请上传身份证正面!")
                    return
                }
                if (img.contains("http")) {
                    mContext.toast("请修改身份证正面!")
                    return
                }
                setResult(OpenShopActivity.RESULTCODE_OPEN_SHOP, Intent().putExtra("userIcon",img))
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
        imagePicker.isSaveRectangle = false
        //圖片緩存
        imagePicker.imageLoader = GlideImageLoader()
        imagePicker.isMultiMode = true//多选
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
            iv_img.setImageURI("file://" + (images.get(0) as ImageItem).path)
            img =(images.get(0) as ImageItem).path
        }
    }


}