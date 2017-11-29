package com.exz.carprofitmuch.bean;

/**
 * Created by pc on 2017/11/29.
 */

public class MyCouponBean {

    /**
     * couponId : 优惠劵id
     * discount : 10
     * limitMoney : 200
     * beginDate : 2016.01.25
     * invalidDate : 2016.02.25
     * state : 0未使用，1已使用 2已过期
     * shopId : 店铺id
     * shopName : 店铺名称
     */

    private String couponId;
    private String discount;
    private String limitMoney;
    private String beginDate;
    private String invalidDate;
    private String state;
    private String shopId;
    private String shopName;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getLimitMoney() {
        return limitMoney;
    }

    public void setLimitMoney(String limitMoney) {
        this.limitMoney = limitMoney;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
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
}
