package com.exz.carprofitmuch.bean;

/**
 * Created by pc on 2017/11/28.
 */

public class ShopLevelBean {

    /**
     * levelId : 1
     * name : 白金店铺
     * fee : 299
     * limit : 20
     */

    private String levelId;
    private String name;
    private String fee;
    private String limit;

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }
}
