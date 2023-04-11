package com.example.springboot_vue_back.resp;

import java.util.List;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/4/11 19:21
 **/
public class PageResp<T> {
    private long total;

    private List<T> list;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "PageResp{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
