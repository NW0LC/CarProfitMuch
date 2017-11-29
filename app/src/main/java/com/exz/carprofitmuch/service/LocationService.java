package com.exz.carprofitmuch.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.szw.framelibrary.config.Constants;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

/**
 * 定位服务
 * by  Swain
 */
public class LocationService extends Service implements TencentLocationListener {
    public TencentLocationRequest request = null;
    public TencentLocationManager locationManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化定位
        request = TencentLocationRequest.create();
        TencentLocationManager locationManager = TencentLocationManager.getInstance(getApplicationContext());
        int error = locationManager.requestLocationUpdates(request, this);
        if (error!=0) {
            Log.v("locationRegister","注册失败code:"+error);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null)
            locationManager.removeUpdates(this);//销毁定位客户端，同时销毁本地定位服务。
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int error, String s) {
        if (TencentLocation.ERROR_OK == error) {
            // 定位成功
            Intent intent = new Intent();
            intent.setAction(Constants.Location.INTENT_ACTION_LOCATION);
            intent.putExtra(Constants.Location.INTENT_DATA_LOCATION_CITY,
                    tencentLocation.getCity());
            intent.putExtra(Constants.Location.INTENT_DATA_LOCATION_LONGITUDE,
                    tencentLocation.getLongitude()+"");
            intent.putExtra(Constants.Location.INTENT_DATA_LOCATION_LATITUDE,
                    tencentLocation.getLatitude()+"");
            this.sendBroadcast(intent);
        } else {
            // 定位失败
            Log.v("locationRegister","定位失败:"+error+s);
        }

    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }
}