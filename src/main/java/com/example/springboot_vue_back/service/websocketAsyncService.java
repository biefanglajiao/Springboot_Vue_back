package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: 常兆海
 * @Description: 异步化websocket消息队列操作 使用异步化时  在启动类上添加注解以后   异步的两个方法不能再一个类中  因为异步是对类再另一个线程生成一个代理类
 * @DateTime: 2023/5/15 20:58
 **/
@Service
public class websocketAsyncService {
    @Resource
    private WebSocketServer webSocketServer;
    @Async //异步化 用来处理websocket消息队列
    public void sendInfo(String message ,String logid  ){
        MDC.put("LOG_ID",logid);//将流水号放入日志
        webSocketServer.sendInfo(message);
    }

}
