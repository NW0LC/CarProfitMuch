package com.exz.carprofitmuch.bean;

import java.util.List;

/**
 * Created by pc on 2017/11/30.
 */

public class ReturnGoodsBean  {


    /**
     * returnOrderId : 1
     * returnOrderType : 1
     * shopId : 店铺id
     * shopName : 店铺名称
     * shopPhone : 店铺联系电话
     * returnOrderState : 订单状态
     * returnOrderSubState : 订单子状态
     * returnMoney : 退款金额
     * returnScore : 退款积分
     * actualMoney : 实付款
     * score : 使用积分额
     * transportMoney : 运费
     * goodsInfo : [{"goodsId":"商品id","payMark":"1","goodsName":"商品名称","imgUrl":"商品图片","count":"2","skuid":"规格id","goodsType":"规格内容"}]
     */

    private String returnOrderId;
    private String returnOrderType;
    private String shopId;
    private String shopName;
    private String shopPhone;
    private String returnOrderState;
    private String returnOrderSubState;
    private String returnMoney;
    private String returnScore;
    private String actualMoney;
    private String score;
    private String transportMoney;
    private List<GoodsBean> goodsInfo;

    public String getReturnOrderId() {
        return returnOrderId;
    }

    public void setReturnOrderId(String returnOrderId) {
        this.returnOrderId = returnOrderId;
    }

    public String getReturnOrderType() {
        return returnOrderType;
    }

    public void setReturnOrderType(String returnOrderType) {
        this.returnOrderType = returnOrderType;
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

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public String getReturnOrderState() {
        return returnOrderState;
    }

    public void setReturnOrderState(String returnOrderState) {
        this.returnOrderState = returnOrderState;
    }

    public String getReturnOrderSubState() {
        return returnOrderSubState;
    }

    public void setReturnOrderSubState(String returnOrderSubState) {
        this.returnOrderSubState = returnOrderSubState;
    }

    public String getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(String returnMoney) {
        this.returnMoney = returnMoney;
    }

    public String getReturnScore() {
        return returnScore;
    }

    public void setReturnScore(String returnScore) {
        this.returnScore = returnScore;
    }

    public String getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(String actualMoney) {
        this.actualMoney = actualMoney;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTransportMoney() {
        return transportMoney;
    }

    public void setTransportMoney(String transportMoney) {
        this.transportMoney = transportMoney;
    }

    public List<GoodsBean> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<GoodsBean> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

}
