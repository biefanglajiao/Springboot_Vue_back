package com.example.springboot_vue_back.req;

public class UserQueryReq extends PageReq {//将请求参数封装成一个类
    private String loginName;

    @Override
    public String toString() {
        return "UserQueryReq{" +
                "loginName='" + loginName + '\'' +
                "} " + super.toString();
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}