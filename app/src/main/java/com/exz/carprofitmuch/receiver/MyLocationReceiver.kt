package com.exz.carprofitmuch.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import com.exz.carprofitmuch.bean.LocationBean
import com.hwangjr.rxbus.RxBus
import com.szw.framelibrary.config.Constants

class MyLocationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null)
            return
        if (intent.action == Constants.Location.INTENT_ACTION_LOCATION) {
            RxBus.get().register(this)
            val entity = LocationBean()
            entity.city = intent.getStringExtra(Constants.Location.INTENT_DATA_LOCATION_CITY)
            entity.latitude = intent.getStringExtra(Constants.Location.INTENT_DATA_LOCATION_LATITUDE)
            entity.longitude = intent.getStringExtra(Constants.Location.INTENT_DATA_LOCATION_LONGITUDE)
            RxBus.get().post(com.exz.carprofitmuch.config.Constants.Receiver_Location, entity)
        }
    }
}// end receiver