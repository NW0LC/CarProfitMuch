package com.exz.carprofitmuch.bean;

/**
 * Created by pc on 2017/11/28.
 */

public class CheckResultBean {

    /**
     * classMark : {"value":"虚拟服务类","check":"1"}
     * level : {"id":"1","value":"白金","check":"1"}
     * name : {"value":"张三的店","check":"1"}
     * category : {"id":"1","value":"汽车美容","check":"1"}
     * district : {"id":"1","value":"江苏省徐州市泉山区","check":"1"}
     * detail : {"value":"淮海路120号","check":"1"}
     * longitude : {"value":"","check":"0"}
     * latitude : {"value":"","check":"0"}
     * contact : {"value":"张三","check":"0"}
     * idFrontImg : {"value":"http://123.png","check":"0"}
     * idBackImg : {"value":"http://123.png","check":"0"}
     * idNum : {"value":"3203230002330023","check":"0"}
     * idName : {"value":"张三","check":"0"}
     * businessImg : {"value":"http://123.png","check":"0"}
     */

    private ClassMarkBean classMark;
    private LevelBean level;
    private NameBean name;
    private CategoryBean category;
    private DistrictBean district;
    private DetailBean detail;
    private LongitudeBean longitude;
    private LatitudeBean latitude;
    private ContactBean contact;
    private IdFrontImgBean idFrontImg;
    private IdBackImgBean idBackImg;
    private IdNumBean idNum;
    private IdNameBean idName;
    private BusinessImgBean businessImg;

    public ClassMarkBean getClassMark() {
        return classMark;
    }

    public void setClassMark(ClassMarkBean classMark) {
        this.classMark = classMark;
    }

    public LevelBean getLevel() {
        return level;
    }

    public void setLevel(LevelBean level) {
        this.level = level;
    }

    public NameBean getName() {
        return name;
    }

    public void setName(NameBean name) {
        this.name = name;
    }

    public CategoryBean getCategory() {
        return category;
    }

    public void setCategory(CategoryBean category) {
        this.category = category;
    }

    public DistrictBean getDistrict() {
        return district;
    }

    public void setDistrict(DistrictBean district) {
        this.district = district;
    }

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public LongitudeBean getLongitude() {
        return longitude;
    }

    public void setLongitude(LongitudeBean longitude) {
        this.longitude = longitude;
    }

    public LatitudeBean getLatitude() {
        return latitude;
    }

    public void setLatitude(LatitudeBean latitude) {
        this.latitude = latitude;
    }

    public ContactBean getContact() {
        return contact;
    }

    public void setContact(ContactBean contact) {
        this.contact = contact;
    }

    public IdFrontImgBean getIdFrontImg() {
        return idFrontImg;
    }

    public void setIdFrontImg(IdFrontImgBean idFrontImg) {
        this.idFrontImg = idFrontImg;
    }

    public IdBackImgBean getIdBackImg() {
        return idBackImg;
    }

    public void setIdBackImg(IdBackImgBean idBackImg) {
        this.idBackImg = idBackImg;
    }

    public IdNumBean getIdNum() {
        return idNum;
    }

    public void setIdNum(IdNumBean idNum) {
        this.idNum = idNum;
    }

    public IdNameBean getIdName() {
        return idName;
    }

    public void setIdName(IdNameBean idName) {
        this.idName = idName;
    }

    public BusinessImgBean getBusinessImg() {
        return businessImg;
    }

    public void setBusinessImg(BusinessImgBean businessImg) {
        this.businessImg = businessImg;
    }

    public static class ClassMarkBean {
        /**
         * value : 虚拟服务类
         * check : 1
         */

        private String value;
        private String check;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class LevelBean {
        /**
         * id : 1
         * value : 白金
         * check : 1
         */

        private String id;
        private String value;
        private String check;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class NameBean {
        /**
         * value : 张三的店
         * check : 1
         */

        private String value;
        private String check;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class CategoryBean {
        /**
         * id : 1
         * value : 汽车美容
         * check : 1
         */

        private String id;
        private String value;
        private String check;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class DistrictBean {
        /**
         * id : 1
         * value : 江苏省徐州市泉山区
         * check : 1
         */

        private String id;
        private String value;
        private String check;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class DetailBean {
        /**
         * value : 淮海路120号
         * check : 1
         */

        private String value;
        private String check;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class LongitudeBean {
        /**
         * value :
         * check : 0
         */

        private String value;
        private String check;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class LatitudeBean {
        /**
         * value :
         * check : 0
         */

        private String value;
        private String check;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class ContactBean {
        /**
         * value : 张三
         * check : 0
         */

        private String value;
        private String check;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class IdFrontImgBean {
        /**
         * value : http://123.png
         * check : 0
         */

        private String value;
        private String check;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class IdBackImgBean {
        /**
         * value : http://123.png
         * check : 0
         */

        private String value;
        private String check;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class IdNumBean {
        /**
         * value : 3203230002330023
         * check : 0
         */

        private String value;
        private String check;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class IdNameBean {
        /**
         * value : 张三
         * check : 0
         */

        private String value;
        private String check;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }

    public static class BusinessImgBean {
        /**
         * value : http://123.png
         * check : 0
         */

        private String value;
        private String check;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }
    }
}
