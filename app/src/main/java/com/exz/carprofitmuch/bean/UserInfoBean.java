package com.exz.carprofitmuch.bean;

/**
 * Created by pc on 2017/12/1.
 */

public class UserInfoBean {

    /**
     * headerUrl : 图片地址
     * nickname : 昵称
     * phone : 手机号
     * gender : 2
     * wechat : 1233000
     */

    private String headerUrl;
    private String nickname;
    private String phone;
    private String gender;
    private String wechat;

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }
}
