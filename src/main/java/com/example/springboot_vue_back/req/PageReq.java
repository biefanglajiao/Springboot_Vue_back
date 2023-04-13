package com.example.springboot_vue_back.req;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/4/11 19:21
 **/
public class PageReq {
    @NotNull(message = "页码不能为空")
    private int page;
@NotNull(message = "页大小不能为空")
@Max(value = 100,message = "页大小不能超过1000")
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
