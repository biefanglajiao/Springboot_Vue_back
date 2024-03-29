package com.example.springboot_vue_back.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/4/17 10:59
 **/
public class DocQueryResp {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Long ebookId;

    private Long parent;

    private String name;

    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;
    private  boolean involved;

    private boolean option;

    public boolean isOption() {
        return option;
    }

    public void setOption(boolean option) {
        this.option = option;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isInvolved() {
        return involved;
    }

    public void setInvolved(boolean involved) {
        this.involved = involved;
    }

    @Override
    public String toString() {
        return "DocQueryResp{" +
                "id=" + id +
                ", ebookId=" + ebookId +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                ", involved=" + involved +
                ", option=" + option +
                '}';
    }
}
