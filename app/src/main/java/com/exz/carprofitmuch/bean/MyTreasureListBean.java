package com.exz.carprofitmuch.bean;

/**
 * Created by pc on 2017/11/28.
 */

public class MyTreasureListBean {


    /**
     * title : 100元洗车抵用劵一张
     * date : 2017年10月31日
     * invalidDate : 2017年12月31日
     * state : 0未使用，1已使用 2已过期
     * shopId : 店铺id
     * shopName : 店铺名称
     * shopAddress : 店铺名称
     * shopPhone : 店铺联系电话
     */

    private String title;
    private String date;
    private String invalidDate;
    private String state;
    private String shopId;
    private String shopName;
    private String shopAddress;
    private String shopPhone;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(String invalidDate) {
        this.invalidDate = invalidDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }
}
