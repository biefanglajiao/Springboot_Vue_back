package com.example.springboot_vue_back.req;

public class EbookQueryReq extends PageReq{//将请求参数封装成一个类
    private Long id;

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




}