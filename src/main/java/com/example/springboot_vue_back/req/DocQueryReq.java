package com.example.springboot_vue_back.req;

public class DocQueryReq extends PageReq{//将请求参数封装成一个类

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DocQueryReq{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}