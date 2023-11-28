package com.example.springboot_vue_back.domain;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/27 10:38
 **/
public class EbookInvolved {
    private Long id;
    private boolean involved;

    private boolean option;

    public boolean isOption() {
        return option;
    }

    public void setOption(boolean option) {
        this.option = option;
    }

    public boolean isInvolved() {
        return involved;
    }

    public void setInvolved(boolean involved) {
        this.involved = involved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
