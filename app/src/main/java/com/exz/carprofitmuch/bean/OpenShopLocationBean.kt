package com.exz.carprofitmuch.bean

import java.io.Serializable

/**
 * Created by pc on 2017/11/23.
 */

class OpenShopLocationBean(var longitude: String,
                           var latitude: String,
                           var address: String    ) : Serializable {

    var longitudCheck = ""
    var latitudeCheck = ""
    var addressCheck = ""

}