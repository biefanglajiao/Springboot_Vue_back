package com.example.springboot_vue_back.resp;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/5/26 10:09
 **/
public class StatisticResp {
    @JsonFormat(pattern = "MM-dd",timezone = "GMT+8")//返回给前端的格式 日期格式化
    private Date date;
    private int viewCount;
    private int voteCount;
    private int viewIncrease;
    private int voteIncrease;

    @Override
    public String toString() {
        return "StatisticResp{" +
                "date=" + date +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                ", viewIncrease=" + viewIncrease +
                ", voteIncrease=" + voteIncrease +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public int getViewIncrease() {
        return viewIncrease;
    }

    public void setViewIncrease(int viewIncrease) {
        this.viewIncrease = viewIncrease;
    }

    public int getVoteIncrease() {
        return voteIncrease;
    }

    public void setVoteIncrease(int voteIncrease) {
        this.voteIncrease = voteIncrease;
    }
}
