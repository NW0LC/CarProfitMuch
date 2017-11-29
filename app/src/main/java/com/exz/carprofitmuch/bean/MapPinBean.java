package com.exz.carprofitmuch.bean;

import java.io.Serializable;

/**
 * Created by pc on 2017/11/28.
 */

public class MapPinBean implements Serializable {

    /**
     * id : 1
     * classMark : 1
     * title : 汽车用户大世界
     * longitude : 店铺经度
     * latitude : 店铺纬度
     */

    private String id;
    private String classMark;
    private String title;
    private double longitude;
    private double latitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassMark() {
        return classMark;
    }

    public void setClassMark(String classMark) {
        this.classMark = classMark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
