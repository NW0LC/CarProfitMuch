package com.exz.carprofitmuch.module.mine.shop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.fastjson.JSON
import com.bigkoo.pickerview.OptionsPickerView
import com.blankj.utilcode.util.KeyboardUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.OpenShopAdapter
import com.exz.carprofitmuch.bean.*
import com.exz.carprofitmuch.config.Urls
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_open_shop.*
import kotlinx.android.synthetic.main.footer_open_shop.view.*
import org.jetbrains.anko.toast
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by pc on 2017/11/23.
 * 申请开店
 */

class OpenShopActivity : BaseActivity() {
    private lateinit var pvOptionsAddress: OptionsPickerView<String>
    private lateinit var listAddress: List<CityBean.InfoEntity.ProvincesEntity>
    private val optionsProvinces = java.util.ArrayList<String>()
    private val optionsCities = java.util.ArrayList<java.util.ArrayList<String>>()
    private val optionsCounties = java.util.ArrayList<java.util.ArrayList<java.util.ArrayList<String>>>()
    private var optionsAddress1 = 0
    private var optionsAddress2 = 0
    private var optionsAddress3 = 0
    private var classMark = "1"

    private lateinit var mOptions: OptionsPickerView<String>
    private val ShopLevelStr = java.util.ArrayList<String>()
    private var ShopLevel = ArrayList<ShopLevelBean>()

    private var cardImg = OpenShopCardImgBean()
    private var businessImgBean = BusinessImgBean()
    private val data = ArrayList<OpenShopListBean>()
    private lateinit var adapter: OpenShopAdapter
    private lateinit var footerView: View
    private var position = 0
    private var openState = "3"//开店状态：0开店审核中 1开店审核通过 2开店审核未通过 3未开店
    private var locationBean: OpenShopLocationBean? = null
    private var levelId = ""          //店铺等级id
    private var name = ""//店铺名称
    private var categoryId = ""//店铺类目id。【商城】sheet【店铺类目】接口
    private var districtId = ""//区id
    private var detail = ""//详细地址
    private var longitude = ""//经度（用户）
    private var latitude = ""//纬度（用户）
    private var contact = ""//联系人
    private var idFrontImg = ""//身份证正面照（base64)
    private var idBackImg = ""//身份证反面照（base64)
    private var idNum = ""//身份证号
    private var idName = ""//身份证上的姓名
    private var businessImg = ""//营业执照
    private var url = ""

    override fun initToolbar(): Boolean {
        mTitle.text = getString(R.string.main_open_shop)
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 10f)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        return false
    }


    override fun setInflateId(): Int = R.layout.activity_open_shop

    override fun init() {
        super.init()
        iniView()
        initPicker()
        initEvent()
        if (!openState.equals("3")) initCheckResult() else  initShopLevel()
    }

    private fun initCheckResult() {

        DataCtrlClassXZW.CheckResultData(mContext, {
            if (it != null) {
                for (bean in adapter.data) {
                    when (bean.k) {
                        "店铺类型" -> {
                            classMark = if (it.classMark.value.equals("实体商品类")) "1" else "2"
                            adapter.data.get(adapter.data.indexOf(bean)).v = it.classMark.value
                            // check 0 未通过审核 1 通过审核   check[0]=adapter.data.state[3]  check[1]=adapter.data.state[4]
                            adapter.data.get(adapter.data.indexOf(bean)).state = if (it.classMark.check.equals("0")) "3" else "4"
                        }
                        "店铺等级" -> {
                            adapter.data.get(adapter.data.indexOf(bean)).v = it.level.value
                            // check 0 未通过审核 1 通过审核   check[0]=adapter.data.state[3]  check[1]=adapter.data.state[4]
                            adapter.data.get(adapter.data.indexOf(bean)).state = if (it.level.check.equals("0")) "3" else "4"
                            initShopLevel()
                        }
                        "店铺名称" -> {
                            adapter.data.get(adapter.data.indexOf(bean)).v = it.name.value
                            // check 0 未通过审核 1 通过审核   check[0]=adapter.data.state[3]  check[1]=adapter.data.state[4]
                            adapter.data.get(adapter.data.indexOf(bean)).state = if (it.name.check.equals("0")) "3" else "4"
                        }
                        "店铺类目" -> {
                            adapter.data.get(adapter.data.indexOf(bean)).v = it.category.value
                            // check 0 未通过审核 1 通过审核   check[0]=adapter.data.state[3]  check[1]=adapter.data.state[4]
                            adapter.data.get(adapter.data.indexOf(bean)).state = if (it.category.check.equals("0")) "3" else "4"
                        }
                        "所在地" -> {
                            adapter.data.get(adapter.data.indexOf(bean)).v = it.district.value
                            // check 0 未通过审核 1 通过审核   check[0]=adapter.data.state[3]  check[1]=adapter.data.state[4]
                            adapter.data.get(adapter.data.indexOf(bean)).state = if (it.district.check.equals("0")) "3" else "4"
                        }
                        "详细地址" -> {
                            adapter.data.get(adapter.data.indexOf(bean)).v = "已填写"
                            // check 0 未通过审核 1 通过审核   check[0]=adapter.data.state[3]  check[1]=adapter.data.state[4]
                            adapter.data.get(adapter.data.indexOf(bean)).state = if (it.detail.check.equals("0")) "3" else "4"
                            locationBean = OpenShopLocationBean(it.longitude.check, it.latitude.check, it.detail.check)
                            locationBean!!.latitude=it.latitude.check
                            locationBean!!.longitudCheck=it.longitude.check
                            locationBean!!.addressCheck=it.detail.check
                            if (it.detail.check.equals("0") || it.latitude.check.equals("0") || it.longitude.check.equals("0")) {
                                adapter.data.get(adapter.data.indexOf(bean)).state = "3"
                            } else {
                                adapter.data.get(adapter.data.indexOf(bean)).state = "4"
                            }

                        }
                        "联系人" -> {
                            adapter.data.get(adapter.data.indexOf(bean)).v = it.contact.value
                            // check 0 未通过审核 1 通过审核   check[0]=adapter.data.state[3]  check[1]=adapter.data.state[4]
                            adapter.data.get(adapter.data.indexOf(bean)).state = if (it.contact.check.equals("0")) "3" else "4"
                        }
                        "身份证正反面照片" -> {
                            cardImg = OpenShopCardImgBean()
                            cardImg.cardImg = it.idFrontImg.value
                            cardImg.cardBackImg = it.idBackImg.value
                            cardImg.cardNum = it.idNum.value
                            cardImg.cardName = it.idName.value

                            cardImg.cardImgCheck = it.idFrontImg.check
                            cardImg.cardBackImgCheck = it.idBackImg.check
                            cardImg.cardNumCheck = it.idNum.check
                            cardImg.cardNameCheck = it.idName.check
                            if (it.idFrontImg.check.equals("0") || it.idBackImg.check.equals("0") || it.idNum.check.equals("0") || it.idName.check.equals("0")) {
                                adapter.data.get(adapter.data.indexOf(bean)).state = "3"
                            } else {
                                adapter.data.get(adapter.data.indexOf(bean)).state = "4"
                            }
                            adapter.data.get(adapter.data.indexOf(bean)).v = "已填写"

                        }
                        "营业执照照片" -> {
                            adapter.data.get(adapter.data.indexOf(bean)).v ="已填写"
                            businessImgBean= BusinessImgBean()
                            businessImgBean.businessImg=it.businessImg.value
                            businessImgBean.check=it.businessImg.check

                            // check 0 未通过审核 1 通过审核   check[0]=adapter.data.state[3]  check[1]=adapter.data.state[4]
                            adapter.data.get(adapter.data.indexOf(bean)).state = if (it.businessImg.check.equals("0")) "3" else "4"

                        }
                    }
                    adapter.notifyDataSetChanged()
                }

            }

        })
    }

    private fun initEvent() {
        tv_submit.setOnClickListener {

            if (openState.equals("3")) {
                for (bean in adapter.data) {
                    if (bean.v.equals("请选择") || bean.v.equals("请填写") || bean.v.equals("未填写")) {
                        mContext.toast(bean.v + bean.k)
                        return@setOnClickListener
                    }
                }
                url = Urls.ConfirmInfo
            } else {
                url = Urls.ModifyInfo
                for (bean in adapter.data) {
                    if (bean.v.equals("请选择") || bean.v.equals("请填写") || bean.v.equals("未填写")||bean.v.equals("")) {
                        mContext.toast(bean.v + bean.k)
                        return@setOnClickListener
                    }
                    if (bean.state.equals("3")) {
                        mContext.toast("请修改" + bean.k)
                        return@setOnClickListener
                    }
                }
            }
            /**
             * 提交申请资料
             * classMark	string	必填	店铺类型：1实体商品类 2虚拟服务类
             * levelId	string	必填	店铺等级id
             * name	string	必填	店铺名称
             * categoryId	string	必填	店铺类目id。【商城】sheet【店铺类目】接口
             * districtId	string	必填	区id
             * detail	string	必填	详细地址
             * longitude	string	必填	经度（用户）
             * latitude	string	必填	纬度（用户）
             * contact	string	必填	联系人
             * idFrontImg	string	必填	身份证正面照（base64)
             * idBackImg	string	必填	身份证反面照（base64)
             * idNum	string	必填	身份证号
             * idName	string	必填	身份证上的姓名
             * businessImg	string	必填	营业执照（base64)
             * */
            DataCtrlClassXZW.confirmInfoData(mContext, classMark, levelId, name, categoryId, districtId, detail, longitude, latitude, contact,idFrontImg,
                    idBackImg, idNum, idName, businessImg, url, {
                if (it != null) {
                    setResult(Activity.RESULT_OK)
                    finish()
                }

            })

        }

    }

    private fun iniView() {
        openState = intent.getStringExtra(OPENSTATE)
        adapter = OpenShopAdapter()
        data.add(OpenShopListBean("店铺类型", "实体商品类", "2"))
        data.add(OpenShopListBean("店铺等级", "请选择", "1"))
        data.add(OpenShopListBean("店铺名称", "请填写", "1"))
        data.add(OpenShopListBean("店铺类目", "请选择", "1"))
        data.add(OpenShopListBean("所在地", "请选择", "1"))
        data.add(OpenShopListBean("详细地址", "请填写", "1"))
        data.add(OpenShopListBean("联系人", "请填写", "1"))
        data.add(OpenShopListBean("身份证正反面照片", "未填写", "1"))
        data.add(OpenShopListBean("营业执照照片", "未填写", "1"))
        adapter.setNewData(data)
        adapter.bindToRecyclerView(mRecyclerView)
        footerView = View.inflate(mContext, R.layout.footer_open_shop, null)
        adapter.addFooterView(footerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 1, ContextCompat.getColor(mContext, R.color.app_bg)))

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(a: BaseQuickAdapter<*, *>?, view: View?, pos: Int) {
                position = pos
                val entity = adapter.data.get(position)
                val b = Bundle()
                when (entity.k) {
                    "店铺类型" -> {
                        b.putSerializable("keyValue", OpenShopKeyValueBean(entity.v, entity.id, true))
                        startActivityForResult(Intent(mContext, OpenShopKeyValueActivity::class.java).putExtra("className", entity.k).putExtras(b), RESULTCODE_OPEN_SHOP)
                    }
                    "店铺等级" -> {
                        mOptions.setCyclic(false)
                        mOptions.show()
                    }
                    "店铺名称" -> {
                        b.putSerializable("text", OpenTextBen("店铺名称", if (entity.state.equals("1")) "" else entity.v, 40, "*店铺名称请控制长度不要超过40字"))
                        startActivityForResult(Intent(mContext, OpenShopInputTextActivity::class.java).putExtras(b), RESULTCODE_OPEN_SHOP)
                    }
                    "店铺类目" -> {
                        b.putSerializable("keyValue", OpenShopKeyValueBean(entity.v, entity.id, true))
                        startActivityForResult(Intent(mContext, OpenShopKeyValueActivity::class.java).putExtra("className", entity.k).putExtras(b).putExtra("classMark", classMark), RESULTCODE_OPEN_SHOP)
                    }
                    "所在地" -> {
                        KeyboardUtils.hideSoftInput(this@OpenShopActivity)
                        pvOptionsAddress.setPicker(optionsProvinces, optionsCities, optionsCounties,
                                true)
                        pvOptionsAddress.setSelectOptions(optionsAddress1, optionsAddress2, optionsAddress3)
                        //三级选择器
                        pvOptionsAddress.setCyclic(false)
                        pvOptionsAddress.show()
                    }
                    "详细地址" -> {
                        if (locationBean == null) {
                            locationBean = OpenShopLocationBean("", "", "")
                        }
                        b.putSerializable("location", locationBean)
                        startActivityForResult(Intent(mContext, OpenShopAddressActivity::class.java).putExtras(b), RESULTCODE_OPEN_SHOP)
                    }
                    "联系人" -> {
                        b.putSerializable("text", OpenTextBen("联系人", if (entity.state.equals("1")) "" else entity.v, 15, "*联系人请控制长度不要超过15字"))
                        startActivityForResult(Intent(mContext, OpenShopInputTextActivity::class.java).putExtras(b), RESULTCODE_OPEN_SHOP)
                    }
                    "身份证正反面照片" -> {
                        b.putSerializable("cardImg", cardImg)
                        startActivityForResult(Intent(mContext, OpenShopCardImgActivity::class.java).putExtras(b), RESULTCODE_OPEN_SHOP)
                    }
                    "营业执照照片" -> {

                        b.putSerializable("businessImg",  businessImgBean)
                        startActivityForResult(Intent(mContext, OpenShopBusinessImgActivity::class.java).putExtras(b), RESULTCODE_OPEN_SHOP)
                    }
                }


            }
        })
    }

    private fun initPicker() {
        pvOptionsAddress = OptionsPickerView(this)
        Thread {
            val cityBean = JSON.parseObject<CityBean>(getJson(), CityBean::class.java)
            var city: java.util.ArrayList<String>
            var counties: java.util.ArrayList<String>
            var countiesS: java.util.ArrayList<java.util.ArrayList<String>>

            listAddress = cityBean.info.provinces
            for (p in listAddress) {
                city = java.util.ArrayList()
                countiesS = java.util.ArrayList()
                optionsProvinces.add(p.areaName)
                for (c in p.cities) {
                    city.add(c.areaName)
                    counties = c.counties.mapTo(java.util.ArrayList()) { it.areaName }
                    countiesS.add(counties)
                }
                optionsCities.add(city)
                optionsCounties.add(countiesS)

            }
        }.start()

        pvOptionsAddress.setOnoptionsSelectListener { options1, option2, options3 ->
            try {
                optionsAddress1 = options1
                optionsAddress2 = option2
                optionsAddress3 = options3
                val address = String.format(listAddress[options1].areaName + listAddress[options1].cities[option2].areaName + listAddress[options1].cities[option2].counties[options3].areaName)
                val addressId = listAddress[options1].cities[option2].counties[options3].areaId
                if (adapter.data.get(position).state.equals("1")) {
                    adapter.data.get(position).state = "2"
                } else if (adapter.data.get(position).state.equals("3")) {
                    adapter.data.get(position).state = "4"
                }
                adapter.data.get(position).v = address
                adapter.data.get(position).id = addressId
                adapter.notifyItemChanged(position)

                districtId = addressId
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        mOptions = OptionsPickerView(this)

        mOptions.setOnoptionsSelectListener { options1, option2, options3 ->
            if (adapter.data.get(position).state.equals("1")) {
                adapter.data.get(position).state = "2"
            } else if (adapter.data.get(position).state.equals("3")) {
                adapter.data.get(position).state = "4"
            }
            adapter.data.get(position).v = ShopLevelStr.get(options1)
            adapter.data.get(position).id = ShopLevel.get(options1).levelId
            adapter.notifyItemChanged(position)
            levelId = ShopLevel.get(options1).levelId
            footerView.tv_fee.text = String.format(mContext.getString(R.string.main_open_shop_fee), ShopLevel.get(options1).fee)


        }


    }

    private fun initShopLevel() {
        /*
        *店铺等级
        */
        DataCtrlClassXZW.ShopLevelData(mContext, classMark, {
            if (it != null) {
                ShopLevel.clear()
                ShopLevelStr.clear()
                ShopLevel = it as ArrayList<ShopLevelBean>
                for (bean in it) {
                    ShopLevelStr.add(bean.name)
                }
                mOptions.setPicker(ShopLevelStr)

                //审核被拒 平台管理费
                if (ShopLevelStr.size > 0 && ShopLevelStr.size > 0&&!adapter.data.get(1).v.equals("请选择")) {
                    try {
                        footerView.tv_fee.text = String.format(mContext.getString(R.string.main_open_shop_fee), ShopLevel.get(ShopLevelStr.indexOf(adapter.data.get(1).v)).fee)
                    } catch (e: Exception) {
                    }
                }
            }
        })
    }

    private fun getJson(): String {

        val stringBuilder = StringBuilder()
        try {
            val assetManager = mContext.assets
            val bf = BufferedReader(InputStreamReader(
                    assetManager.open("city.json")))
            var b = true
            while (b) {
                val line = bf.readLine()
                if (line != null) {
                    stringBuilder.append(line)
                } else {
                    b = false
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }


    /*
    * s 1 列表 2 文本输入 3 身份证上传 4 营业执照上传 5详细地址
    */
    fun textChanger(s: String, data: Intent) {
        val entity = adapter.data.get(position)
        when (s) {
            "1" -> {
                val e = data.getSerializableExtra("keyValue") as OpenShopKeyValueBean
                entity.v = e.title
                entity.id = e.id

            }
            "2" -> {
                var e = data.getSerializableExtra("text") as OpenTextBen
                entity.v = e.content
            }
            "3" -> {
                cardImg = data.getSerializableExtra("cardImg") as OpenShopCardImgBean
                entity.v = "已填写"
            }
            "4" -> {

                businessImgBean = data.getSerializableExtra("businessImg") as BusinessImgBean
                entity.v = "已填写"
            }
            "5" -> {
                locationBean = data.getSerializableExtra("location") as OpenShopLocationBean
                entity.v = locationBean!!.address
            }
        }

        if (entity.state.equals("1")) {
            entity.state = "2"
        } else if (entity.state.equals("3")) {
            entity.state = "4"
        }
    }

    companion object {
        var RESULTCODE_OPEN_SHOP = 100
        var OPENSTATE = ""//开店状态：0开店审核中 1开店审核通过 2开店审核未通过 3未开店
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULTCODE_OPEN_SHOP -> {
                val entity = adapter.data.get(position)
                when (entity.k) {
                    "店铺类型" -> {
                        if (data != null) {
                            classMark = (data.getSerializableExtra("keyValue") as OpenShopKeyValueBean).id
                            textChanger("1", data)
                            adapter.data.get(1).v = ""
                            adapter.data.get(3).v = ""
                            adapter.notifyItemChanged(1)
                            adapter.notifyItemChanged(3)
                            initShopLevel()
                        }
                    }
                    "店铺等级" -> {
//                        if (data != null) {
//                            textChanger("1", data)
//                        }
                    }
                    "店铺名称" -> {
                        if (data != null) {
                            textChanger("2", data)
                            name = (data.getSerializableExtra("text") as OpenTextBen).content
                        }
                    }
                    "店铺类目" -> {
                        if (data != null) {
                            textChanger("1", data)
                            categoryId = (data.getSerializableExtra("keyValue") as OpenShopKeyValueBean).id
                        }
                    }
                    "详细地址" -> {
                        if (data != null) {
                            textChanger("5", data)

                            detail = (data.getSerializableExtra("location") as OpenShopLocationBean).address
                            longitude = (data.getSerializableExtra("location") as OpenShopLocationBean).longitude
                            latitude = (data.getSerializableExtra("location") as OpenShopLocationBean).latitude
                        }
                    }
                    "联系人" -> {
                        if (data != null) {
                            textChanger("2", data)
                            contact = (data.getSerializableExtra("text") as OpenTextBen).content
                        }
                    }
                    "身份证正反面照片" -> {
                        if (data != null) {
                            textChanger("3", data)
                            idFrontImg = if(cardImg.cardImg.contains("http")) "" else cardImg.cardImg
                            idBackImg =if(cardImg.cardBackImg.contains("http")) "" else cardImg.cardBackImg
                            idNum = cardImg.cardNum
                            idName = cardImg.cardName
                        }
                    }
                    "营业执照照片" -> {
                        if (data != null) {
                            textChanger("4", data)
                            businessImg =if(businessImgBean.businessImg.contains("http")) "" else businessImgBean.businessImg
                        }
                    }
                }
                adapter.notifyItemChanged(position)
            }
        }
    }
}

