package com.example.springboot_vue_back.domain;

import java.util.Arrays;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/29 14:18
 **/
public class ToEmail {
    /**
     * 邮件接收方，可多人
     */
    private String[] tos;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;

    @Override
    public String toString() {
        return "ToEmail{" +
                "tos=" + Arrays.toString(tos) +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String[] getTos() {
        return tos;
    }

    public void setTos(String[] tos) {
        this.tos = tos;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
