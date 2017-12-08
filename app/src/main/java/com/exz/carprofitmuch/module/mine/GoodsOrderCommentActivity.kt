package com.exz.carprofitmuch.module.mine

import android.app.Activity
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.EncryptUtils
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.GoodsOrderCommentAdapter
import com.exz.carprofitmuch.bean.CommentOrder
import com.exz.carprofitmuch.bean.GoodsOrderCommentBean
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.imageloder.GlideImageLoader
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.view.CustomProgress
import com.szw.framelibrary.view.preview.PreviewActivity
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_oreder_comment_list.*
import kotlinx.android.synthetic.main.footer_goods_cooment.view.*

/**
 * on 2017/10/18.
 * 商品評價
 */

class GoodsOrderCommentActivity : BaseActivity(), View.OnClickListener {
    private lateinit var imagePicker: ImagePicker
    lateinit var mAdapter: GoodsOrderCommentAdapter
    private var position: Int = 0
    private var positionImg: Int = 0
    private var serveStar: String = ""
    private var logisticsStar: String = ""
    private lateinit var mFooterView: View
    private lateinit var mCustomProgress: CustomProgress
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

        toolbar.inflateMenu(R.menu.menu_footprint)
        val actionView = toolbar.menu.getItem(0).actionView
        (actionView as TextView).text = getString(R.string.mine_my_goods_order_comment)
        actionView.setOnClickListener {
            mCustomProgress = CustomProgress.show(mContext, "提交中...", false, null)!!
            val entity = CommentOrder()
            entity.userId = MyApplication.loginUserId
            entity.orderId = orderId
            entity.shopId = shopId
            entity.serveStar = serveStar
            entity.logisticsStar = logisticsStar
            entity.requestCheck = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + orderId, MyApplication.salt).toLowerCase()
            val commentInfo = ArrayList<CommentOrder.CommentInfoBean>()
            for (bean in mAdapter.data) {
                val info = CommentOrder.CommentInfoBean()
                info.goodsId = bean.goodsId
                info.content = bean.content
                info.goodsStar = bean.score
                info.skuid = bean.skuid
                var img = ""
                for (imgUrl in bean.imgUrls) {
                    img += imgUrl + ","
                }
                info.images = if (!TextUtils.isEmpty(img)) "" else img.substring(0, img.length - 1)
                commentInfo.add(info)
            }


            DataCtrlClassXZW.confirmCommentData(mContext, JSON.toJSONString(entity), {
                if (it != null) {
                    mCustomProgress.dismiss()
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
        initData()
    }

    private fun initData() {
        val list = Gson().fromJson<MutableList<GoodsOrderCommentBean>>(json, object : TypeToken<ArrayList<GoodsOrderCommentBean>>() {}.type)
        for (bean in mAdapter.data) {
            bean.photos.add(0, "res://com.exz.carprofitmuch/" + R.mipmap.icon_take_photo)
        }
        mAdapter.setNewData(list)
    }

    private fun initImgRecycler() {
        mAdapter = GoodsOrderCommentAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mFooterView = View.inflate(mContext, R.layout.footer_goods_cooment, null)
        mAdapter.addFooterView(mFooterView)
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
            override fun onItemClickListener(position: Int, positionImg: Int) {
                this@GoodsOrderCommentActivity.position = position
                this@GoodsOrderCommentActivity.positionImg = positionImg
                if (positionImg == mAdapter.data[this@GoodsOrderCommentActivity.position].photos.size - 1) {
                    imagePicker.selectLimit = 6 - mAdapter.data[this@GoodsOrderCommentActivity.position].photos.size
                    PermissionCameraWithCheck(Intent(mContext, ImageGridActivity::class.java), false)
                } else {
                    val intent = Intent(mContext, PreviewActivity::class.java)
                    val imgs = ArrayList<String>()
                    imgs.addAll(mAdapter.data[this@GoodsOrderCommentActivity.position].photos)
                    imgs.removeAt(imgs.lastIndex)
                    intent.putExtra(PreviewActivity.PREVIEW_INTENT_IMAGES, imgs)
                    intent.putExtra(PreviewActivity.PREVIEW_INTENT_SHOW_NUM, true)
                    intent.putExtra(PreviewActivity.PREVIEW_INTENT_IS_CAN_DELETE, true)
                    intent.putExtra(PreviewActivity.PREVIEW_INTENT_POSITION, this@GoodsOrderCommentActivity.position)
                    startActivityForResult(intent, 100)
                }
            }


        })

        mFooterView.rb_serveStar.setOnRatingBarChangeListener { ratingBar, _, _ ->
            serveStar = ratingBar.rating.toString()

        }
        mFooterView.rb_logisticsStar.setOnRatingBarChangeListener { ratingBar, _, _ ->
            logisticsStar = ratingBar.rating.toString()

        }


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
            mCustomProgress = CustomProgress.show(mContext, "提交中...", false, null)!!
            val phoneList = ArrayList<String>()
            for (image in images) {
                mAdapter.data[position].photos.add(mAdapter.data[position].photos.size - 1, (image as ImageItem).path)
            }
            mAdapter.notifyItemChanged(position)
            phoneList.addAll(mAdapter.data[position].photos)
            phoneList.removeAt(0)
            DataCtrlClassXZW.UploadImgData(position, mAdapter, phoneList, mCustomProgress, {})

        } else if (Activity.RESULT_OK == resultCode) {
            val photos = mAdapter.data[position].photos
            val imgUrl = mAdapter.data[position].imgUrls
            val array = data?.getStringArrayListExtra(PreviewActivity.PREVIEW_INTENT_RESULT)
            array?.forEach {
                val index = photos.indexOf(it) - 1
                photos.remove(it)
                imgUrl.removeAt(index)
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        orderId = ""
        shopId = ""
        json = ""
    }

    companion object {
        var orderId = ""
        var shopId = ""
        var json = ""
    }
}