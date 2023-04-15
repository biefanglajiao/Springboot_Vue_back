package com.example.springboot_vue_back.req;

import javax.validation.constraints.NotNull;

public class EbookQueryReq extends PageReq {//将请求参数封装成一个类
    private Long id;
    private Long categoryId2;


    public Long getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(Long categoryId2) {
        this.categoryId2 = categoryId2;
    }

    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "EbookQueryReq{" +
                "id=" + id +
                ", categoryId2=" + categoryId2 +
                ", name='" + name + '\'' +
                "} " + super.toString();
    }

}