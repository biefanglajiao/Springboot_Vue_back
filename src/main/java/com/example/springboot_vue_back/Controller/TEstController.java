package com.example.springboot_vue_back.Controller;

import com.example.springboot_vue_back.domain.Test;
import com.example.springboot_vue_back.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
//          @Controller//返回页面 因为前后分离 所以不用
//@ResponseBody//返回字符串或者json对象
public class TEstController {

    @Value("${project.auther}")//z自定义配置项。获取yml文件里的信息
//    @Value("${project.auther：czh}")、//给配置项添加默认值：后的   优先配置项目，后默认值
    private String auther;
@Resource
private TestService testService;


    @RequestMapping("/hello")
    public String hello(){
        return "helloword!"+auther;
    }

 @PostMapping("/hello1")
    public String helo1(String name){
        return "hello1"+name;
 }

 @GetMapping("/list")
    public List<Test> list(){
        return testService.list();
 }
}
