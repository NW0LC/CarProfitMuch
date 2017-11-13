package com.exz.carprofitmuch.module.mine

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ItemOrderCommentImageAdapter
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.imageloder.GlideImageLoader
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.view.preview.PreviewActivity
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_IMAGES
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_IS_CAN_DELETE
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_POSITION
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_RESULT
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_SHOW_NUM
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_service_order_comment.*

/**
 * Created by 史忠文
 * on 2017/10/18.
 */

class ServiceOrderCommentActivity : BaseActivity(), View.OnClickListener {
    private lateinit var imagePicker:ImagePicker
    lateinit var mAdapter: ItemOrderCommentImageAdapter
    var photos = ArrayList<String>()
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = getString(R.string.service_order_comment_name)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun init() {
        initCamera()
        initImgRecycler()
        initEvent()
    }

    private fun initImgRecycler() {
        photos.add(0, "res://com.exz.carprofitmuch/" + R.mipmap.icon_take_photo)
        mAdapter = ItemOrderCommentImageAdapter()
        mAdapter.setNewData(photos)
        mAdapter.bindToRecyclerView(mPhotoRecyclerView)
        mPhotoRecyclerView.layoutManager = GridLayoutManager(mContext, 5)
        mPhotoRecyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        mPhotoRecyclerView.isNestedScrollingEnabled = false
        mPhotoRecyclerView.isFocusable = false
        mPhotoRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (position == mAdapter.data.size - 1) {
                    imagePicker.selectLimit=6-mAdapter.data.size
                    PermissionCameraWithCheck(Intent(this@ServiceOrderCommentActivity, ImageGridActivity::class.java), false)
                } else {
                    val intent = Intent(mContext, PreviewActivity::class.java)
                    val imgs = ArrayList<String>()
                    imgs.addAll(photos)
                    imgs.removeAt(imgs.lastIndex)
                    intent.putExtra(PREVIEW_INTENT_IMAGES, imgs)
                    intent.putExtra(PREVIEW_INTENT_SHOW_NUM, true)
                    intent.putExtra(PREVIEW_INTENT_IS_CAN_DELETE, true)
                    intent.putExtra(PREVIEW_INTENT_POSITION, position)
                    startActivityForResult(intent,100)
                }
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                photos.removeAt(position)
                mAdapter.notifyItemRemoved(position)
            }
        })
    }


    private fun initEvent() {
    }

    private fun initCamera() {
        imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader = GlideImageLoader()
        //显示相机
        imagePicker.isShowCamera = true
        //是否裁剪
        imagePicker.isCrop = true
        //是否按矩形区域保存裁剪图片
        imagePicker.isSaveRectangle = true
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

    override fun setInflateId(): Int = R.layout.activity_service_order_comment


    override fun onClick(p0: View?) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) { //图片选择
            val images = data?.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<*>
            for (image in images) {
                photos.add(photos.size-1, "file://" + (image as ImageItem).path)
                mAdapter.notifyItemChanged(photos.size-1)
                mAdapter.notifyItemChanged(photos.size-2)
            }
        } else if (Activity.RESULT_OK==resultCode){
            val array=data?.getStringArrayListExtra(PREVIEW_INTENT_RESULT)
            array?.forEach { photos.remove(it) }
            mAdapter.notifyDataSetChanged()
        }
    }
}