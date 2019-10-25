package rango.tool.androidtool.http.api.bean;

public class LoginUserBean {

    /**
     * user_info : {"gender":"Man","signature":"","user_id":"UUAIMAVYVJJDC","head_image_url":"http://thirdwx.qlogo.cn/mmopen/vi_32/9aLK9Cx6dzfnIPAyyHjVfnAMpwopfJPuRL9AK7xQ0NJ69ib7kHbbbIWUyZHLEGB719WIpHH8c5SVYAIeAU6nEZA/132","name":"ç\u0099½é\u0099¢ä¼\u009f","birthday":""}
     * token : STAIMDYNNGTYA
     */

    private UserInfoBean user_info;
    private String token;

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class UserInfoBean {
        /**
         * gender : Man
         * signature :
         * user_id : UUAIMAVYVJJDC
         * head_image_url : http://thirdwx.qlogo.cn/mmopen/vi_32/9aLK9Cx6dzfnIPAyyHjVfnAMpwopfJPuRL9AK7xQ0NJ69ib7kHbbbIWUyZHLEGB719WIpHH8c5SVYAIeAU6nEZA/132
         * name : ç½é¢ä¼
         * birthday :
         */

        private String gender;
        private String signature;
        private String user_id;
        private String head_image_url;
        private String name;
        private String birthday;

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getHead_image_url() {
            return head_image_url;
        }

        public void setHead_image_url(String head_image_url) {
            this.head_image_url = head_image_url;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }
    }
}
