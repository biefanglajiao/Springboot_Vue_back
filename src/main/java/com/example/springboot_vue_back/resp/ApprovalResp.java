package com.example.springboot_vue_back.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/28 15:12
 **/
public class ApprovalResp {
    @JsonSerialize(using = ToStringSerializer.class)
    private long id;
    private String context ;
    private String email;
    private String name;
    private String location;
    private  boolean option;
    private  boolean approval;
    @JsonSerialize(using = ToStringSerializer.class)

    private long docid;//对应的文档id
    private  String docname;//对应的文档名字

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public boolean isOption() {
        return option;
    }

    public void setOption(boolean option) {
        this.option = option;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getDocid() {
        return docid;
    }

    public void setDocid(long docid) {
        this.docid = docid;
    }

    @Override
    public String toString() {
        return "ApprovalResp{" +
                "id=" + id +
                ", context='" + context + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", option=" + option +
                ", approval=" + approval +
                ", docid=" + docid +
                ", docname='" + docname + '\'' +
                '}';
    }
}
