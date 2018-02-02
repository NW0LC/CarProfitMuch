package com.exz.carprofitmuch.scanner

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Handler
import android.util.TypedValue
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.module.mine.PayQRActivity
import com.google.zxing.Result
import com.google.zxing.client.result.ParsedResult
import com.google.zxing.client.result.ParsedResultType
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.mylhyl.zxing.scanner.OnScannerCompletionListener
import com.mylhyl.zxing.scanner.decode.QRDecode
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.imageloder.GlideImageLoader
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_scanner.*
import org.jetbrains.anko.toast


/**
 * Created by 史忠文
 * on 2018/1/15.
 */
class ScannerActivity: BaseActivity(), OnScannerCompletionListener {


    override fun initToolbar(): Boolean {
        StatusBarUtil.immersive(this)
        return false
    }

    override fun setInflateId()= R.layout.activity_scanner

    private var isFlash=false
    override fun init() {
        mScannerView.setMediaResId(R.raw.weixin_beep)//设置扫描成功的声音

        mScannerView.setOnScannerCompletionListener(this)
        initCamera()
        bt_flash.setOnClickListener {
            mScannerView.toggleLight(!isFlash)//开
            isFlash=!isFlash
        }
        bt_photo.setOnClickListener {
            PermissionCameraWithCheck(Intent(this, ImageGridActivity::class.java), false)
        }
        bt_back.setOnClickListener { finish() }
    }
    private fun initCamera() {
        val imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader = GlideImageLoader()
        //显示相机
        imagePicker.isShowCamera = true
        //是否裁剪
        imagePicker.isCrop = false
        //是否按矩形区域保存裁剪图片
        imagePicker.isSaveRectangle = true
        //圖片緩存
        imagePicker.imageLoader = GlideImageLoader()
        imagePicker.isMultiMode = false//单选
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
            QRDecode.decodeQR((images[0] as ImageItem).path,this)
        }else if (resultCode== Activity.RESULT_OK)
            finish()
    }

    override fun onScannerCompletion(rawResult: Result?, parsedResult: ParsedResult?, barcode: Bitmap?) {
        val type = parsedResult?.type
        when (type) {

            ParsedResultType.TEXT,ParsedResultType.URI -> {
//                toast(rawResult?.text?:"")
                if (rawResult?.text?.isNotEmpty()==true){
                    if (rawResult.text.contains(Urls.url)){
                        Handler().postDelayed({startActivityForResult(Intent(this, PayQRActivity::class.java).putExtra("QRCode",rawResult.text?.split("?")?.get(1)),100)},1000)
                    }else{
                        toast(rawResult.text.toString())
                        mScannerView.restartPreviewAfterDelay(1000)
                    }
                }else{
                    DialogUtils.Warning(this,"识别二维码失败！")
                }
            }

            else -> {
            }
        }
    }
     override fun onResume() {
        mScannerView.onResume()
        super.onResume()
    }

     override fun onPause() {
        mScannerView.onPause()
        super.onPause()
    }
}