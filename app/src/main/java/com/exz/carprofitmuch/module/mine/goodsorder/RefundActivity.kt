package com.exz.carprofitmuch.module.mine.goodsorder

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.TypedValue
import android.view.View
import com.bigkoo.pickerview.OptionsPickerView
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.FileIOUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.ItemOrderCommentImageAdapter
import com.exz.carprofitmuch.bean.ReturnGoodsReasonBean
import com.exz.carprofitmuch.bean.ReturnGoodsTypeBean
import com.exz.carprofitmuch.utils.SZWUtils
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.imageloder.GlideImageLoader
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.view.preview.PreviewActivity
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_refund.*
import org.jetbrains.anko.toast
import java.net.URLEncoder

/**
 * Created by pc on 2017/11/21.
 * 申请退款
 */

class RefundActivity : BaseActivity(), View.OnClickListener {


    private lateinit var imagePicker: ImagePicker
    private var photos = ArrayList<String>()
    private var returnGoodsTypeStr = ArrayList<String>()
    private var returnGoodsType = ArrayList<ReturnGoodsTypeBean>()
    private var reasonStr = ArrayList<String>()
    private var mPickerType = ""
    private var reasonList = ArrayList<ReturnGoodsReasonBean>()
    private lateinit var mAdapter: ItemOrderCommentImageAdapter
    private lateinit var mPickerView: OptionsPickerView<String>
    private var returnTypeId = ""
    private var reasonId = ""

    override fun initToolbar(): Boolean {
        mTitle.text = mContext.getString(R.string.mine_return_total)
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


    override fun setInflateId(): Int = R.layout.activity_refund

    override fun init() {
        super.init()
        initCamera()
        initView()
        initReturnType()
        initReturnReason()
    }


    private fun initReturnType() {
        DataCtrlClassXZW.ReturnGoodsTypeListData(mContext, {
            if (it != null) {
                returnGoodsType = it as ArrayList<ReturnGoodsTypeBean>
                for (bean in returnGoodsType) {
                    returnGoodsTypeStr.add(bean.typeContent)
                }

            }

        })
    }

    private fun initReturnReason() {
        DataCtrlClassXZW.returnGoodsReasonListData(mContext, intent.getStringExtra(Refund_Intent_OrderId), {
            if (it != null) {
                reasonList = it as ArrayList<ReturnGoodsReasonBean>
                for (bean in reasonList) {
                    reasonStr.add(bean.reasonContent)
                }

            }

        })
    }

    private fun initView() {

        SZWUtils.matcherSearchTitle(tv_star1, mContext.getString(R.string.mine_return_Policy), mContext.getString(R.string.mine_return_Policy).indexOf("*"), mContext.getString(R.string.mine_return_Policy).length, ContextCompat.getColor(mContext, R.color.Red))
        SZWUtils.matcherSearchTitle(tv_star3, mContext.getString(R.string.mine_refund_cause), mContext.getString(R.string.mine_refund_cause).indexOf("*"), mContext.getString(R.string.mine_return_Policy).length, ContextCompat.getColor(mContext, R.color.Red))
        photos.add(0, Uri.parse("android.resource://" + applicationContext.packageName + "/" +R.mipmap.icon_take_photo).toString())
        mAdapter = ItemOrderCommentImageAdapter()
        mAdapter.setNewData(photos)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = GridLayoutManager(mContext, 5)
        mRecyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        mRecyclerView.isNestedScrollingEnabled = false
        mRecyclerView.isFocusable = false
        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (position == mAdapter.data.size - 1) {
                    imagePicker.selectLimit = 6 - mAdapter.data.size
                    PermissionCameraWithCheck(Intent(mContext, ImageGridActivity::class.java), false)
                } else {
                    val intent = Intent(mContext, PreviewActivity::class.java)
                    val imgs = ArrayList<String>()
                    imgs.addAll(photos)
                    imgs.removeAt(imgs.lastIndex)
                    intent.putExtra(PreviewActivity.PREVIEW_INTENT_IMAGES, imgs)
                    intent.putExtra(PreviewActivity.PREVIEW_INTENT_SHOW_NUM, true)
                    intent.putExtra(PreviewActivity.PREVIEW_INTENT_IS_CAN_DELETE, true)
                    intent.putExtra(PreviewActivity.PREVIEW_INTENT_POSITION, position)
                    startActivityForResult(intent, 100)
                }
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                photos.removeAt(position)
                mAdapter.notifyItemRemoved(position)
            }
        })
        mPickerView = OptionsPickerView(mContext)
        mPickerView.setOnoptionsSelectListener { options1, _, _ ->
            if (mPickerType == "1") {
                tv_refund_type.text = returnGoodsTypeStr[options1]
                returnTypeId = returnGoodsType[options1].typeId
            } else {
                tv_refund_cause.text = reasonStr[options1]
                reasonId = reasonList[options1].reasonId
            }
        }
        tv_refund_type.setOnClickListener(this)
        tv_refund_cause.setOnClickListener(this)
        tv_submit.setOnClickListener(this)
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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_refund_type -> {
                mPickerType = "1"
                if (returnGoodsTypeStr.isNotEmpty()) {
                    mPickerView.setPicker(returnGoodsTypeStr)
                    mPickerView.setCyclic(false)
                    mPickerView.show()
                }

            }
            R.id.tv_refund_cause -> {
                mPickerType = "2"
                if (returnGoodsTypeStr.isNotEmpty()) {
                mPickerView.setPicker(reasonStr)
                mPickerView.setCyclic(false)
                mPickerView.show()
            }
            }

            R.id.tv_submit -> {
                if (TextUtils.isEmpty(returnTypeId)) {
                    toast(mContext.getString(R.string.mine_select_return_Policy))
                    return
                }

                if (TextUtils.isEmpty(reasonId)) {

                    toast(mContext.getString(R.string.mine_input_refund_cause))
                    return
                }
                val issue = URLEncoder.encode(ed_issue.text.toString().trim(),"utf-8")
                var img = ""
                mAdapter.data
                        .filterNot { it.contains("android.resource://") }
                        .forEach { img += EncodeUtils.base64Encode2String(
                                FileIOUtils.readFile2BytesByStream(
                                        it.replace("file://", ""))) + "," }
                DataCtrlClassXZW.submitRefundData(mContext, intent.getStringExtra(Refund_Intent_OrderId), returnTypeId, reasonId, issue, img, {
                    if (it != null) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                })
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) { //图片选择
            val images = data?.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<*>
            for (image in images) {
                photos.add(photos.size - 1, "file://" + (image as ImageItem).path)
                mAdapter.notifyItemChanged(photos.size - 1)
                mAdapter.notifyItemChanged(photos.size - 2)
            }
        } else if (Activity.RESULT_OK == resultCode) {
            val array = data?.getStringArrayListExtra(PreviewActivity.PREVIEW_INTENT_RESULT)
            array?.forEach { photos.remove(it) }
            mAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        val Refund_Intent_OrderId = "Refund_Intent_OrderId"
    }

}
