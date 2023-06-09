package com.example.springboot_vue_back;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

//@ComponentScan({"com.example","com.xxx"})//包扫描接口，方便对保证扫描的管理
@SpringBootApplication
@MapperScan("com.example.springboot_vue_back.Mapper")
@EnableScheduling //定时器
@EnableAsync //异步
public class SpringbootVueBackApplication {
private static final Logger LOG = LoggerFactory.getLogger(SpringbootVueBackApplication.class);
    public static void main(String[] args){
        //修改启动类
        SpringApplication app =new SpringApplication(SpringbootVueBackApplication.class);
        Environment environment=app.run(args).getEnvironment();
        LOG.info("启动成功");
        LOG.info("地址：\thttp://127.0.0.1:{}",environment.getProperty("server.port"));
    }

}
