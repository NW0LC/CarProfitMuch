package com.exz.carprofitmuch.bean;

import java.util.List;

/**
 * Created by pc on 2017/11/29.
 */

public class MyCommentBean {


    /**
     * goodsId : 商品id
     * goodsName : 商品名称
     * imgUrl : 商品图片地址
     * score : 评分
     * content : 评论内容
     * images : ["image1","image1"]
     * date : 评价时间
     */

    private String goodsId;
    private String goodsName;
    private String imgUrl;
    private String score;
    private String content;
    private String date;
    private List<String> images;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
