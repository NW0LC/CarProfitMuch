package com.exz.carprofitmuch.bean;

import java.util.List;

/**
 * Created by pc on 2017/12/1.
 */

public class FootprintBean {

    /**
     * sectionId : 1
     * sectionName : 10月12号
     * goodsList : [{"goodsId":"商品id","goodsName":"商品名称","imgUrl":"商品图片地址","goodsPrice":"商品价格","payMark":"1"}]
     */

    private String sectionId;
    private String sectionName;
    private List<GoodsListBean> goodsList;

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<GoodsListBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<GoodsListBean> goodsList) {
        this.goodsList = goodsList;
    }

    public static class GoodsListBean {
        /**
         * goodsId : 商品id
         * goodsName : 商品名称
         * imgUrl : 商品图片地址
         * goodsPrice : 商品价格
         * payMark : 1
         */

        private String goodsId;
        private String goodsName;
        private String imgUrl;
        private String goodsPrice;
        private String payMark;

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

        public String getGoodsPrice() {
            return goodsPrice;
        }

        public void setGoodsPrice(String goodsPrice) {
            this.goodsPrice = goodsPrice;
        }

        public String getPayMark() {
            return payMark;
        }

        public void setPayMark(String payMark) {
            this.payMark = payMark;
        }
    }
}
