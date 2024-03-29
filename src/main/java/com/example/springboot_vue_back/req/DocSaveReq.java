package com.example.springboot_vue_back.req;

import javax.validation.constraints.NotNull;

public class DocSaveReq {
    private Long id;
@NotNull(message = "ebookId不能为空")
    private Long ebookId;
@NotNull(message = "parent不能为空")
    private Long parent;
@NotNull(message = "name不能为空")
    private String name;


   private boolean involved;

    @NotNull(message = "[内容]不能为空")
    private String content;
@NotNull(message = "sort不能为空")
    private Integer sort;

    private Integer viewCount;

    private Integer voteCount;

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

    @Override
    public String toString() {
        return "DocSaveReq{" +
                "id=" + id +
                ", ebookId=" + ebookId +
                ", parent=" + parent +
                ", name='" + name + '\'' +
                ", involved=" + involved +
                ", content='" + content + '\'' +
                ", sort=" + sort +
                ", viewCount=" + viewCount +
                ", voteCount=" + voteCount +
                '}';
    }

    public boolean isInvolved() {
        return involved;
    }

    public void setInvolved(boolean involved) {
        this.involved = involved;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}