package com.example.e_commerce_1.email_auth;

public class ReadWriteUserDetails {
    public String  doB, gender, mobile, profileImg;

    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String textDoB, String textGender, String textMobile){
        this.doB = textDoB;
        this.gender = textGender;
        this.mobile = textMobile;
    }

    public String getDoB() {
        return doB;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
