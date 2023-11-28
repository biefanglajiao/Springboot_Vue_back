package com.example.springboot_vue_back.domain;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/28 15:12
 **/
public class Needhelp {
    private long id;
    private String context ;
    private String email;
    private String name;
    private String location;
    private  boolean option;

    public boolean isOption() {
        return option;
    }

    public void setOption(boolean option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "Needhelp{" +
                "id=" + id +
                ", context='" + context + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", option=" + option +
                '}';
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
