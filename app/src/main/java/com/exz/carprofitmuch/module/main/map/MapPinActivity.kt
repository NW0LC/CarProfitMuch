package com.exz.carprofitmuch.module.main.map

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.exz.carprofitmuch.DataCtrlClassXZW
import com.exz.carprofitmuch.R
import com.exz.carprofitmuch.bean.MapPinBean
import com.exz.carprofitmuch.config.Urls.MapPacket
import com.exz.carprofitmuch.config.Urls.MapTreasure
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.map.geolocation.TencentLocationManager
import com.tencent.map.geolocation.TencentLocationRequest
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory
import com.tencent.mapsdk.raster.model.LatLng
import com.tencent.mapsdk.raster.model.Marker
import com.tencent.mapsdk.raster.model.MarkerOptions
import com.tencent.tencentmap.mapsdk.map.TencentMap
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_map_pin.*


/**
 * Created by pc on 2017/11/22.
 * 地图大头针
 */

class MapPinActivity : BaseActivity(), TencentLocationListener, TencentMap.OnMarkerClickListener {

    private var myLocation: Marker? = null
    private lateinit var locationManager: TencentLocationManager
    private lateinit var locationRequest: TencentLocationRequest
    private lateinit var tencentMap: TencentMap
    private var icLction = ""
    private var url = ""
    private var data: List<MapPinBean>? = null
    private var bottomContent = ""
    override fun initToolbar(): Boolean {
        mTitle.text = intent.getStringExtra("className")

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        //状态栏透明和间距处理
        toolbar.setNavigationOnClickListener {
            finish()
        }
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_map_pin

    override fun init() {
        initView()

    }


    private fun initView() {
        //获取TencentMap实例
        tencentMap = mapview.getMap()
        //注册定位
        locationManager = TencentLocationManager.getInstance(this)
        locationRequest = TencentLocationRequest.create()

        locationManager.requestLocationUpdates(
                locationRequest, this)

        if (intent.getStringExtra("className").equals(mContext.getString(R.string.main_treasure_get))) {
            icLction = "ic_treasure.ico" //宝藏地图页数据
            url = MapTreasure
            bottomContent = mContext.getString(R.string.home_map_treasure_num)
            tv_get.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.mipmap.icon_map_treasure2), null, null, null)

        } else {
            url = MapPacket //红包地图页数据
            icLction = "ic_redpacket.ico"
            bottomContent = mContext.getString(R.string.home_map_packet_num)
            tv_get.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.mipmap.icon_map_red_packet2), null, null, null)
        }



        btn_show_location.setOnClickListener {
            locationManager.requestLocationUpdates(
                    locationRequest, this)
        }
//设置缩放级别
        tencentMap.setZoom(13)

        //标记点击事件
        tencentMap.setOnMarkerClickListener(this)

    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val entity = marker.tag as MapPinBean
        val b = Bundle()
        b.putSerializable(MAP_BEAN, entity)
        //宝藏领取
        if (intent.getStringExtra("className") == mContext.getString(R.string.main_treasure_get)) {
            startActivity(Intent(mContext, MapTreasureActivity::class.java).putExtras(b))
        } else { //紅包领取
            startActivity(Intent(mContext, MapRedPacketActivity::class.java).putExtras(b))
        }

        return true
    }


    companion object {
        var MAP_BEAN = "Map_Bean"
    }


    override fun onDestroy() {
        super.onDestroy()
        locationManager.removeUpdates(this)
    }


    override fun onLocationChanged(arg0: TencentLocation, arg1: Int, arg2: String) {
        if (arg1 == TencentLocation.ERROR_OK) {
            val latLng = LatLng(arg0.latitude, arg0.longitude)
            //设置地图中心点
            tencentMap.setCenter(latLng)
            myLocation = if (myLocation != null) {
                myLocation?.remove()
                tencentMap.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_red_location)).anchor(0.5f, 0.5f))
            } else {
                tencentMap.addMarker(MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_red_location)).anchor(0.5f, 0.5f))
            }
            myLocation?.position = latLng
            myLocation?.rotation = arg0.bearing //仅当定位来源于gps有效，或者使用方向传感器

            initMapPacket(latLng)
        } else {
            Log.e("location", "location failed:" + arg2)
        }
    }

    /*
    *
    * 红包宝藏地图页数据
    */
    private fun initMapPacket(latLng: LatLng) {
        DataCtrlClassXZW.mapPinData(mContext, url, latLng.longitude.toString(),latLng.latitude.toString(),  {
            if (it != null) {
                data = it
                for (bean in it) {
                    tencentMap.addMarker(MarkerOptions()
                            .position(LatLng(bean.latitude, bean.longitude))
                            .icon(BitmapDescriptorFactory.fromAsset(icLction)).tag(bean))
                }
                tv_get.text = String.format(bottomContent, it.size)
            }
        })

    }

    override fun onStatusUpdate(arg0: String, arg1: Int, arg2: String) {
        var desc = ""
        when (arg1) {
            TencentLocationListener.STATUS_DENIED -> desc = "权限被禁止"
            TencentLocationListener.STATUS_DISABLED -> desc = "模块关闭"
            TencentLocationListener.STATUS_ENABLED -> desc = "模块开启"
            TencentLocationListener.STATUS_GPS_AVAILABLE -> desc = "GPS可用，代表GPS开关打开，且搜星定位成功"
            TencentLocationListener.STATUS_GPS_UNAVAILABLE -> desc = "GPS不可用，可能 gps 权限被禁止或无法成功搜星"
            TencentLocationListener.STATUS_LOCATION_SWITCH_OFF -> desc = "位置信息开关关闭，在android M系统中，此时禁止进行wifi扫描"
            TencentLocationListener.STATUS_UNKNOWN -> {
            }
        }
        Log.e("location", "location status:$arg0, $arg2 $desc")
    }


}
