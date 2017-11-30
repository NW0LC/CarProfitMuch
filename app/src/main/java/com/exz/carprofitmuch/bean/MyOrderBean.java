package com.exz.carprofitmuch.bean;

import java.util.List;

/**
 * Created by pc on 2017/11/29.
 */

public class MyOrderBean {


    /**
     * orderId : 1
     * orderNum : 订单编号
     * orderState : 订单状态
     * totalCount : 商品总数量
     * actualMoney : 实付款
     * logisticsNum : 物流单号
     * shopId : 1
     * shopPhone : 店铺联系电话
     * payMark : 1
     * goodsInfo : [{"goodsId":"商品id","payMark":"1","goodsName":"商品名称","imgUrl":"商品图片","goodsPrice":"100","count":"2","skuid":"规格id","goodsType":"规格内容"}]
     */

    private String orderId;
    private String orderNum;
    private String orderState;
    private String totalCount;
    private String actualMoney;
    private String logisticsNum;
    private String shopId;
    private String shopPhone;
    private String shopName;
    private String payMark;
    private List<GoodsBean> goodsInfo;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(String actualMoney) {
        this.actualMoney = actualMoney;
    }

    public String getLogisticsNum() {
        return logisticsNum;
    }

    public void setLogisticsNum(String logisticsNum) {
        this.logisticsNum = logisticsNum;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getPayMark() {
        return payMark;
    }

    public void setPayMark(String payMark) {
        this.payMark = payMark;
    }

    public List<GoodsBean> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<GoodsBean> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }





}
