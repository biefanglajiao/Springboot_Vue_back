package com.example.springboot_vue_back.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
//          @Controller//返回页面 因为前后分离 所以不用
//@ResponseBody//返回字符串或者json对象
public class TEstController {

    @RequestMapping("/hello")
    public String hello(){
        return "helloword!";
    }
}
