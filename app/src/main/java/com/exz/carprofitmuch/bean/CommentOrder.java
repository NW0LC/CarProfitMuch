package com.exz.carprofitmuch.bean;

import java.util.List;

/**
 * Created by pc on 2017/11/29.
 */

public class CommentOrder {


    /**
     * userId :
     * orderId :
     * shopId :
     * serveStar : 服务评分
     * logisticsStar : 物流评分
     * commentInfo : [{"goodsId":"","skuid":"","images":"图片名称(多张用英文逗号隔开)","content":"评价内容","goodsStar":"商品评分"}]
     * requestCheck :
     */

    private String userId;
    private String orderId;
    private String shopId;
    private String serveStar;
    private String logisticsStar;
    private String requestCheck;
    private List<CommentInfoBean> commentInfo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getServeStar() {
        return serveStar;
    }

    public void setServeStar(String serveStar) {
        this.serveStar = serveStar;
    }

    public String getLogisticsStar() {
        return logisticsStar;
    }

    public void setLogisticsStar(String logisticsStar) {
        this.logisticsStar = logisticsStar;
    }

    public String getRequestCheck() {
        return requestCheck;
    }

    public void setRequestCheck(String requestCheck) {
        this.requestCheck = requestCheck;
    }

    public List<CommentInfoBean> getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(List<CommentInfoBean> commentInfo) {
        this.commentInfo = commentInfo;
    }

    public static class CommentInfoBean {
        /**
         * goodsId :
         * skuid :
         * images : 图片名称(多张用英文逗号隔开)
         * content : 评价内容
         * goodsStar : 商品评分
         */

        private String goodsId;
        private String skuid;
        private String images;
        private String content;
        private String goodsStar;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getSkuid() {
            return skuid;
        }

        public void setSkuid(String skuid) {
            this.skuid = skuid;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getGoodsStar() {
            return goodsStar;
        }

        public void setGoodsStar(String goodsStar) {
            this.goodsStar = goodsStar;
        }
    }
}
