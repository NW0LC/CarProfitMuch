package com.exz.carprofitmuch.module.mine.shop

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputFilter
import android.text.TextUtils
import android.text.TextWatcher
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.OpenShopLocationBean
import com.exz.carprofitmuch.utils.SZWUtils
import com.exz.carprofitmuch.widget.MyWebActivity
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Title
import com.exz.carprofitmuch.widget.MyWebActivity.Intent_Url
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_tv.*
import kotlinx.android.synthetic.main.activity_open_shop_address.*
import org.jetbrains.anko.textColor
import org.jetbrains.anko.toast
import java.net.URLDecoder

/**
 * Created by pc on 2017/11/24.
 * 详细地址
 */

class OpenShopAddressActivity : BaseActivity() {
    var mNewLat: String = ""
    var mNewLon: String = ""
    private var entity = OpenShopLocationBean("", "", "")
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(scrollView, 10f)

        toolbar.inflateMenu(R.menu.menu_mine_score)
        mTitle.text = "详细地址"
        toolbar.setNavigationOnClickListener {
            finish()
        }
        return false
    }

    override fun setInflateId(): Int {
        return R.layout.activity_open_shop_address
    }

    override fun init() {
        super.init()
        ed_address.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(40));
        entity = (intent.getSerializableExtra("location") as OpenShopLocationBean)
        ed_address.setText(entity.address)
        ed_address.setSelection(entity.address.length)
        if (!TextUtils.isEmpty(entity.latitude) && !TextUtils.isEmpty(entity.longitude)) tv_address.setText("已添加")
        if (entity.addressCheck.equals("0")) {
            ed_address.textColor = ContextCompat.getColor(mContext, R.color.Red)
        }
        if (entity.latitudeCheck.equals("0") || entity.longitudCheck.equals("0")) {
            tv_address.textColor = ContextCompat.getColor(mContext, R.color.Red)
        }

        ed_address.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                tv_length.setText("剩余" + (40 - p0.toString().trim().length) + "/" + 40)
                ed_address.textColor = ContextCompat.getColor(mContext, R.color.MaterialGrey800)
                entity.addressCheck="1"
            }
        })

        tv_address.setOnClickListener {
            var intent = Intent(mContext, MyWebActivity::class.java)
            intent.putExtra(Intent_Url, "https://3gimg.qq.com/lightmap/components/locationPicker2/index.html?search=1&type=0&backurl=http%3A%2F%2F3gimg.qq.com%2Flightmap%2Fcomponents%2FlocationPicker2%2Fback.html&key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77&referer=myapp")
            intent.putExtra(Intent_Title, mContext.getString(R.string.main_open_shop_map_marker))
            startActivityForResult(intent, 100)
        }
        tv_submit.setOnClickListener {
            var address = ed_address.text.toString().trim()
            if (TextUtils.isEmpty(address)) {
                mContext.toast("请填写地址")
                return@setOnClickListener
            }

            if (entity.addressCheck.equals("0")) {
                mContext.toast("请修改详细地址!")
                return@setOnClickListener
            }
            entity.address = address
            if (!tv_address.text.toString().trim().equals("已添加")) {
                mContext.toast("请填加添加地图标记")
                return@setOnClickListener
            }
            if (entity.longitudCheck.equals("0") || entity.latitudeCheck.equals("0")) {
                mContext.toast("请修改地图标记!")
                return@setOnClickListener
            }
            entity.latitude = mNewLat
            entity.latitude = mNewLon

            var b = Bundle()
            b.putSerializable("location", entity)
            setResult(OpenShopActivity.RESULTCODE_OPEN_SHOP, Intent().putExtras(b))
            finish()

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                if (data != null) {


                    var url = data.getStringExtra("url")
                    try {
                        val decode = URLDecoder.decode(url, "UTF-8");
                        val uri = Uri.parse(decode);
                        val latng = uri.getQueryParameter("latng");//纬度在前，经度在后，以逗号分隔
                        val split = latng.split(",");
                        mNewLat = split[0];//纬度
                        mNewLon = split[1];//经度
//                    var mNewAddress = uri.getQueryParameter("addr");
                        tv_address.text = "已添加"
                        entity.latitudeCheck="1"
                        entity.longitudCheck="1"
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }
}
