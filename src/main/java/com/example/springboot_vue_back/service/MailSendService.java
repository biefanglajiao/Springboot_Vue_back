package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.domain.ToEmail;
import com.example.springboot_vue_back.resp.ApprovalResp;
import com.example.springboot_vue_back.resp.ComminResp;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/29 14:19
 **/
@Service
public class MailSendService {



    @Resource
    private JavaMailSender mailSender;
    public ComminResp sendMail( String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的
        message.setFrom("1341195069@qq.com");
        //谁要接收
        message.setTo(to);
        //邮件标题
        message.setSubject(subject);
        //邮件内容
        message.setText(content);
        ComminResp comminResp=new ComminResp();
        try {
            mailSender.send(message);
            comminResp.setMessage("审批成功,发送邮件成功");
            comminResp.setSuccess(true);
            return  comminResp;
        } catch (MailException e) {
            e.printStackTrace();
            comminResp.setMessage("审批成功,发送邮件失败");
            comminResp.setSuccess(false);
            return comminResp;
        }

    }

    public ComminResp sendApprovalMail(ApprovalResp approvalResp) {
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的
        message.setFrom("1341195069@qq.com");
        //谁要接收
        message.setTo(approvalResp.getEmail());
        //邮件标题
        message.setSubject("审批结果");
        //邮件内容
        message.setText("关于您在  文章：" + approvalResp.getDocname() + "的申请。您的审批结果为:    您已经通过审核 请按时参与你提交的申请活动");
        ComminResp comminResp = new ComminResp();
        try {
            mailSender.send(message);
            comminResp.setMessage("审批成功,发送邮件成功");
            comminResp.setSuccess(true);
            return comminResp;
        } catch (MailException e) {
            e.printStackTrace();
            comminResp.setMessage("审批成功,发送邮件失败");
            comminResp.setSuccess(false);
            return comminResp;
        }
    }
}
