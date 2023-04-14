package com.example.springboot_vue_back.req;

import javax.validation.constraints.NotNull;

public class CategoryQueryReq extends PageReq{//将请求参数封装成一个类

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryQueryReq{" +
                "name='" + name + '\'' +
                "} " + super.toString();
    }
}