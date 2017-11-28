package com.exz.carprofitmuch.module.mine.shop

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
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.adapter.OpenShopAdapter
import com.exz.carprofitmuch.bean.*
import com.exz.carprofitmuch.utils.RecycleViewDivider
import com.exz.carprofitmuch.utils.SZWUtils
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_open_shop.*
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

    private var cardImg = OpenShopCardImgBean()
    private var businessImg = ""
    private val data = ArrayList<OpenShopListBean>()
    private lateinit var adapter: OpenShopAdapter
    private lateinit var footerView: View
    private var position = 0
    private var locationBean: OpenShopLocationBean? = null
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

    override fun setInflateId(): Int {
        return R.layout.activity_open_shop
    }

    override fun init() {
        super.init()
        iniView()
        initPicker()
        initEvent()
    }

    private fun initEvent() {
        tv_submit.setOnClickListener {
            for (bean in adapter.data) {
                if(bean.v.equals("请选择")||bean.v.equals("请填写")||bean.v.equals("未填写")){
                    mContext.toast(bean.v+bean.k)
                    return@setOnClickListener
                }
            }
        }

    }

    private fun iniView() {

        adapter = OpenShopAdapter()
        data.add(OpenShopListBean("店铺类型", "请选择", "1"))
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
                var entity = adapter.data.get(position)
                var b = Bundle()
                when (entity.k) {
                    "店铺类型" -> {
                        b.putSerializable("keyValue", OpenShopKeyValueBean(entity.v, entity.id, true))
                        startActivityForResult(Intent(mContext, OpenShopKeyValueActivity::class.java).putExtra("className", entity.k).putExtras(b), RESULTCODE_OPEN_SHOP)
                    }
                    "店铺等级" -> {
                        b.putSerializable("keyValue", OpenShopKeyValueBean(entity.v, entity.id, true))
                        startActivityForResult(Intent(mContext, OpenShopKeyValueActivity::class.java).putExtra("className", entity.k).putExtras(b), RESULTCODE_OPEN_SHOP)
                    }
                    "店铺名称" -> {
                        b.putSerializable("text", OpenTextBen("店铺名称", if (entity.state.equals("1")) "" else entity.v, 40, "*店铺名称请控制长度不要超过40字"))
                        startActivityForResult(Intent(mContext, OpenShopInputTextActivity::class.java).putExtras(b), RESULTCODE_OPEN_SHOP)
                    }
                    "店铺类目" -> {
                        b.putSerializable("keyValue", OpenShopKeyValueBean(entity.v, entity.id, true))
                        startActivityForResult(Intent(mContext, OpenShopKeyValueActivity::class.java).putExtra("className", entity.k).putExtras(b), RESULTCODE_OPEN_SHOP)
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
                        if (locationBean == null){
                            locationBean= OpenShopLocationBean("","","")
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

                        startActivityForResult(Intent(mContext, OpenShopBusinessImgActivity::class.java).putExtra("img", businessImg), RESULTCODE_OPEN_SHOP)
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
                var address = String.format(listAddress[options1].areaName + listAddress[options1].cities[option2].areaName + listAddress[options1].cities[option2].counties[options3].areaName)
                var addressId = listAddress[options1].cities[option2].counties[options3].areaId
                adapter.data.get(position).v = address
                adapter.data.get(position).id = addressId
                adapter.notifyItemChanged(position)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
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
        var entity = adapter.data.get(position)
        when (s) {
            "1" -> {
                var e = data.getSerializableExtra("keyValue") as OpenShopKeyValueBean
                entity.v = e.name
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
                businessImg = data.getStringExtra("img")
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            RESULTCODE_OPEN_SHOP -> {
                var entity = adapter.data.get(position)
                when (entity.k) {
                    "店铺类型" -> {
                        if (data != null) {
                            textChanger("1", data)
                        }
                    }
                    "店铺等级" -> {
                        if (data != null) {
                            textChanger("1", data)
                        }
                    }
                    "店铺名称" -> {
                        if (data != null) {
                            textChanger("2", data)
                        }
                    }
                    "店铺类目" -> {
                        if (data != null) {
                            textChanger("1", data)
                        }
                    }
                    "详细地址" -> {
                        if (data != null) {
                            textChanger("5", data)
                        }
                    }
                    "联系人" -> {
                        if (data != null) {
                            textChanger("2", data)
                        }
                    }
                    "身份证正反面照片" -> {
                        if (data != null) {
                            textChanger("3", data)
                        }
                    }
                    "营业执照照片" -> {
                        if (data != null) {
                            textChanger("4", data)
                        }
                    }
                }
                adapter.notifyItemChanged(position)
            }
        }
    }
}
