package com.example.springboot_vue_back.req;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/28 15:12
 **/
public class NeedhelpReq {
    @JsonSerialize(using = ToStringSerializer.class)
    private long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private long docid;
    @NotNull(message = "【内容】不能为空")
    private String context ;
    @NotNull(message = "【邮箱】不能为空")
    @Email(message = "【邮箱】格式不正确")
    private String email;
    @NotNull(message = "【姓名】不能为空")
    private String name;
    @NotNull(message = "【地址】不能为空")
    private String location;
    private boolean option;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long code;
    private boolean approval;

    public long getDocid() {
        return docid;
    }

    public void setDocid(long docid) {
        this.docid = docid;
    }

    public boolean isApproval() {
        return approval;
    }

    public void setApproval(boolean approval) {
        this.approval = approval;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
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

    @Override
    public String toString() {
        return "NeedhelpReq{" +
                "id=" + id +
                ", docid=" + docid +
                ", context='" + context + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", option=" + option +
                ", code=" + code +
                ", approval=" + approval +
                '}';
    }
}
