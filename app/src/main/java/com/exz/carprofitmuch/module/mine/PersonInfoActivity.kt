package com.exz.carprofitmuch.module.mine

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import com.bigkoo.pickerview.OptionsPickerView
import com.blankj.utilcode.util.ScreenUtils
import com.exz.carprofitmuch.DataCtrlClass
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.GenderBean
import com.exz.carprofitmuch.bean.OpenTextBen
import com.exz.carprofitmuch.bean.UserInfoBean
import com.exz.carprofitmuch.module.mine.address.AddressManagerActivity
import com.exz.carprofitmuch.module.mine.shop.OpenShopActivity
import com.exz.carprofitmuch.module.mine.shop.OpenShopInputTextActivity
import com.exz.carprofitmuch.utils.SZWUtils
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.imageloder.GlideImageLoader
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_person_info.*
import java.net.URLDecoder
import java.util.*

/**
 * Created by 史忠文
 * on 2017/10/18.
 */

class PersonInfoActivity : BaseActivity(), View.OnClickListener, OptionsPickerView.OnOptionsSelectListener {
    private lateinit var mTextEntity: OpenTextBen
    private var textType = 0
    private var mUserInfo: UserInfoBean?=null
    private lateinit var pvOptions: OptionsPickerView<GenderBean>
    private lateinit var genderList: ArrayList<GenderBean>
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = getString(R.string.settings_person)
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun init() {
        initGender()
        initCamera()
        initEvent()
        initUserInfo()
    }

    private fun initUserInfo() {
        DataCtrlClassXZW.userInfoData(mContext, {
            if (it != null) {
                mUserInfo = it
                iv_header.setImageURI(it.headerUrl)
                tv_phone.text = it.phone
                tv_nickname.text = URLDecoder.decode(it.nickname, "utf-8")
                tv_wechat.text = it.wechat
                when (it.gender) {
                    "0" -> {
                        tv_gender.text = "女"
                    }
                    "1" -> {
                        tv_gender.text = "男"
                    }
                    "2" -> {
                        tv_gender.text = "保密"
                    }
                }
            }

        })
    }

    private fun initGender() {
        //性别选择器
        genderList = SZWUtils.getGenderData()
        pvOptions = OptionsPickerView(this)
        pvOptions.setPicker(genderList)
        pvOptions.setCyclic(false)
        pvOptions.setOnoptionsSelectListener(this)
    }

    private fun initEvent() {
        bt_header.setOnClickListener(this)
        bt_gender.setOnClickListener(this)
        bt_address.setOnClickListener(this)
        bt_nicename.setOnClickListener(this)
        bt_wechat.setOnClickListener(this)
    }

    private fun initCamera() {
        val w = ScreenUtils.getScreenWidth() * 0.2
        val layoutParams = iv_header.layoutParams
        layoutParams.width = w.toInt()
        layoutParams.height = w.toInt()
        iv_header.layoutParams = layoutParams
        val imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader = GlideImageLoader()
        //显示相机
        imagePicker.isShowCamera = true
        //是否裁剪
        imagePicker.isCrop = true
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

    override fun setInflateId(): Int = R.layout.activity_person_info

    private fun editInfo(key: String, value: String) {
        DataCtrlClass.editPersonInfo(this, key, value) {
            if (it != null) {
                if (key == "header")
                    iv_header.setImageURI(it)
            }
            if (key == "nickname")
                tv_nickname.text = value
            else if (key == "wechat")
                tv_wechat.text = value
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) { //图片选择
            val images = data?.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<*>
            editInfo("header", (images[0] as ImageItem).path)
        } else if (resultCode == OpenShopActivity.RESULTCODE_OPEN_SHOP) {
            when (textType) {
                1 -> {//昵称
                    mTextEntity = data?.getSerializableExtra("text") as OpenTextBen
                    editInfo("nickname", mTextEntity.content)
                }
                2 -> {//微信
                    mTextEntity = data?.getSerializableExtra("text") as OpenTextBen
                    editInfo("wechat", mTextEntity.content)
                }
            }
        }
    }

    override fun onOptionsSelect(options1: Int, option2: Int, options3: Int) {
        tv_gender.text = genderList[options1].value
        editInfo("gender", genderList[options1].key)
    }

    override fun onClick(p0: View?) {
        val b = Bundle()
        when (p0) {
            bt_header -> {
                PermissionCameraWithCheck(Intent(this, ImageGridActivity::class.java), false)
            }
            bt_gender -> {
                pvOptions.show()
            }
            bt_nicename -> {
                textType = 1
                if (mUserInfo != null) mTextEntity = OpenTextBen("修改昵称", URLDecoder.decode(mUserInfo?.nickname, "utf-8"), 15, "*店铺名称请控制长度不要超过15字")
                b.putSerializable("text", mTextEntity)
                startActivityForResult(Intent(mContext, OpenShopInputTextActivity::class.java).putExtras(b), OpenShopActivity.RESULTCODE_OPEN_SHOP)
            }
            bt_wechat -> {
                textType = 2
                if (mUserInfo != null) mTextEntity = OpenTextBen("修改微信号", URLDecoder.decode(mUserInfo?.wechat, "utf-8"), 15, "*微信号请控制长度不要超过15字")
                b.putSerializable("text", mTextEntity)
                startActivityForResult(Intent(mContext, OpenShopInputTextActivity::class.java).putExtras(b), OpenShopActivity.RESULTCODE_OPEN_SHOP)
            }
            bt_address -> {
                startActivity(Intent(mContext, AddressManagerActivity::class.java))
            }
            else -> {
            }
        }
    }


}