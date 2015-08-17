package com.tizi.quanzi.gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by qixingchen on 15/7/21.
 */
public class Login {


    /**
     * group : [{"allowMatch":"Y","groupNo":"","id":"HTDM004894949bddc8f140439ddfe3f7a76104d0","icon":"http://ww1.sinaimg.cn/thumb180/005OaWctjw1eubh7n0zdvj30500503yo.jpg","createUser":"HTDM00485ff2bf0c11ed41848216571ddbbea781","groupName":"哦哦","validation":"Y","remark":null,"bg":null,"type":"0","notice":"噢耶"},{"allowMatch":"Y","groupNo":"","id":"HTDM00481c1fecfdb8b24838b72a0aa1ee9c9639","icon":"http://ww1.sinaimg.cn/thumb180/005OaWctjw1eubh7n0zdvj30500503yo.jpg","createUser":"HTDM0048efd1df5cacd448e48f86775e454e1981","groupName":"测试","validation":"Y","remark":null,"bg":null,"type":"0","notice":"bb"}]
     * user : {"id":"HTDM0048efd1df5cacd448e48f86775e454e1981","icon":"http://ac-hy5srahi.clouddn.com/q3fLSLIwTbnj8PPM9aJpRKD.png","sex":"0","area":null,"token":"HTDM0048b1c89a47da954a089e49005bcda9d617","account":"1","userName":"1","bg":null,"groupNum":"1","signature":null,"mobile":"1"}
     * success : true
     * msg : null
     */
    private List<GroupEntity> group;
    private UserEntity user;
    private boolean success;
    private String msg;

    public List<GroupEntity> getGroup() {
        return group;
    }

    public UserEntity getUser() {
        return user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public static class GroupEntity {
        /**
         * allowMatch : Y
         * groupNo :
         * id : HTDM004894949bddc8f140439ddfe3f7a76104d0
         * icon : http://ww1.sinaimg.cn/thumb180/005OaWctjw1eubh7n0zdvj30500503yo.jpg
         * createUser : HTDM00485ff2bf0c11ed41848216571ddbbea781
         * groupName : 哦哦
         * validation : Y
         * remark : null
         * bg : null
         * type : 0
         * notice : 噢耶
         */
        private String allowMatch;
        private String groupNo;
        private String id;
        private String icon;
        private String createUser;
        private String groupName;
        private String validation;
        private String remark;
        private String bg;
        private String type;
        private String notice;

        public String getAllowMatch() {
            return allowMatch;
        }

        public String getGroupNo() {
            return groupNo;
        }

        public String getId() {
            return id;
        }

        public String getIcon() {
            return icon;
        }

        public String getCreateUser() {
            return createUser;
        }

        public String getGroupName() {
            return groupName;
        }

        public String getValidation() {
            return validation;
        }

        public String getRemark() {
            return remark;
        }

        public String getBg() {
            return bg;
        }

        public String getType() {
            return type;
        }

        public String getNotice() {
            return notice;
        }
    }

    public static class UserEntity {
        /**
         * id : HTDM0048efd1df5cacd448e48f86775e454e1981
         * icon : http://ac-hy5srahi.clouddn.com/q3fLSLIwTbnj8PPM9aJpRKD.png
         * sex : 0
         * area : null
         * token : HTDM0048b1c89a47da954a089e49005bcda9d617
         * account : 1
         * userName : 1
         * bg : null
         * groupNum : 1
         * signature : null
         * mobile : 1
         */
        private String id;
        private String icon;
        private String sex;
        private String area;
        private String token;
        private String account;
        private String userName;
        private String bg;
        private String groupNum;
        private String signature;
        private String mobile;

        public String getId() {
            return id;
        }

        public String getIcon() {
            return icon;
        }

        public String getSex() {
            return sex;
        }

        public String getArea() {
            return area;
        }

        public String getToken() {
            return token;
        }

        public String getAccount() {
            return account;
        }

        public String getUserName() {
            return userName;
        }

        public String getBg() {
            return bg;
        }

        public String getGroupNum() {
            return groupNum;
        }

        public String getSignature() {
            return signature;
        }

        public String getMobile() {
            return mobile;
        }
    }
}
