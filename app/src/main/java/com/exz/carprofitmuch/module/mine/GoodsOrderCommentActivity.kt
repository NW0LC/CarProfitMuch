package com.exz.carprofitmuch.module.mine

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsOrderCommentAdapter
import com.exz.carprofitmuch.bean.GoodsOrderCommentBean
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.imageloder.GlideImageLoader
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.view.preview.PreviewActivity
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_oreder_comment_list.*

/**
 * on 2017/10/18.
 * 商品評價
 */

class GoodsOrderCommentActivity : BaseActivity(), View.OnClickListener {
    private lateinit var imagePicker: ImagePicker
    lateinit var mAdapter: GoodsOrderCommentAdapter
    private var position: Int = 0
    private var positionImg: Int = 0
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = getString(R.string.mine_my_order_comment)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 10f)

        val actionView = toolbar.menu.getItem(0).actionView
        (actionView as TextView).text = getString(R.string.mine_my_goods_order_comment)
        actionView.setOnClickListener {
            DataCtrlClassXZW.ConfirmCommentData(mContext, "", {

                if (it != null) {
                    finish()
                }
            })
        }
        return false
    }

    override fun init() {
        initCamera()
        initImgRecycler()
        initEvent()
    }

    private val arrayList2 = java.util.ArrayList<GoodsOrderCommentBean>()
    private val arrayList2Item1 = java.util.ArrayList<String>()
    private val arrayList2Item2 = java.util.ArrayList<String>()
    private val arrayList2Item3 = java.util.ArrayList<String>()
    private fun initImgRecycler() {
        arrayList2Item1.add(0, "res://com.exz.carprofitmuch/" + R.mipmap.icon_take_photo)
        arrayList2Item2.add(0, "res://com.exz.carprofitmuch/" + R.mipmap.icon_take_photo)
        arrayList2Item3.add(0, "res://com.exz.carprofitmuch/" + R.mipmap.icon_take_photo)
        arrayList2.add(GoodsOrderCommentBean(arrayList2Item1))
        arrayList2.add(GoodsOrderCommentBean(arrayList2Item2))
        arrayList2.add(GoodsOrderCommentBean(arrayList2Item3))
        mAdapter = GoodsOrderCommentAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setNewData(arrayList2)

        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(headerView: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                header.visibility = View.VISIBLE
            }

            override fun onHeaderReleasing(headerView: RefreshHeader?, percent: Float, offset: Int, footerHeight: Int, extendHeight: Int) {
                if (offset == 0)
                    header.visibility = View.GONE
            }
        })
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 15, ContextCompat.getColor(mContext, R.color.app_bg)))
        mAdapter.setOnItemClickListeners(object : GoodsOrderCommentAdapter.OnItemClick {
            override fun OnItemClickListener(positions: Int, positionImgs: Int) {
                position = positions
                positionImg = positionImgs
                if (positionImgs == mAdapter.data.get(position).photos.size - 1) {
                    imagePicker.selectLimit = 6 - mAdapter.data.get(position).photos.size
                    PermissionCameraWithCheck(Intent(mContext, ImageGridActivity::class.java), false)
                } else {
                    val intent = Intent(mContext, PreviewActivity::class.java)
                    val imgs = ArrayList<String>()
                    imgs.addAll(mAdapter.data.get(position).photos)
                    imgs.removeAt(imgs.lastIndex)
                    intent.putExtra(PreviewActivity.PREVIEW_INTENT_IMAGES, imgs)
                    intent.putExtra(PreviewActivity.PREVIEW_INTENT_SHOW_NUM, true)
                    intent.putExtra(PreviewActivity.PREVIEW_INTENT_IS_CAN_DELETE, true)
                    intent.putExtra(PreviewActivity.PREVIEW_INTENT_POSITION, position)
                    startActivityForResult(intent, 100)
                }
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

    override fun setInflateId(): Int = R.layout.activity_oreder_comment_list


    override fun onClick(p0: View?) {
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) { //图片选择
            val images = data?.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<*>

            for (image in images) {
                mAdapter.data.get(position).photos.add(mAdapter.data.get(position).photos.size - 1, "file://" + (image as ImageItem).path)

            }
            mAdapter.notifyItemChanged(position)
        } else if (Activity.RESULT_OK == resultCode) {
            val photos = mAdapter.data.get(position).photos
            val array = data?.getStringArrayListExtra(PreviewActivity.PREVIEW_INTENT_RESULT)
            array?.forEach { photos.remove(it) }
            mAdapter.notifyDataSetChanged()
        }
    }
}