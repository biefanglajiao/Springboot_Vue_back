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

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "NeedhelpReq{" +
                "id=" + id +
                ", context='" + context + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", option=" + option +
                '}';
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
}
