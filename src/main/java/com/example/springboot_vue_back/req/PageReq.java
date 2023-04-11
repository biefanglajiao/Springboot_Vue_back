package com.example.springboot_vue_back.req;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/4/11 19:21
 **/
public class PageReq {
    private int page;

    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }



    public int getSize() {
        return size;
    }

    public void setSize(int  size) {
        this.size = size;
    }


    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", size='" + size + '\'' +
                '}';
    }
}
