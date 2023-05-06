package com.example.springboot_vue_back.req;

import javax.validation.constraints.NotNull;

public class UserResetPasswordReq {
    private Long id;

@NotNull(message = "【密码】不能为空")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}