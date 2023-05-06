package com.example.springboot_vue_back.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springboot_vue_back.Utils.SnowFlake;
import com.example.springboot_vue_back.req.UserLoginReq;
import com.example.springboot_vue_back.req.UserQueryReq;
import com.example.springboot_vue_back.req.UserResetPasswordReq;
import com.example.springboot_vue_back.req.UserSaveReq;
import com.example.springboot_vue_back.resp.ComminResp;
import com.example.springboot_vue_back.resp.UserLoginResp;
import com.example.springboot_vue_back.resp.UserQueryResp;
import com.example.springboot_vue_back.resp.PageResp;
import com.example.springboot_vue_back.service.UserService;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    private SnowFlake snowFlake;//雪花算法生成token


    @GetMapping("/list")//模糊查询书籍
    public ComminResp list(@Valid UserQueryReq req) {
        ComminResp<PageResp<UserQueryResp>> objectComminResp = new ComminResp<>();
        PageResp<UserQueryResp> list = userService.list(req);
        objectComminResp.setContent(list);
        return objectComminResp;
    }

    @PostMapping("/save")//保存书籍  一般保存类用post
    public ComminResp save(@RequestBody @Valid UserSaveReq req) {//json格式的数据要用@RequestBody 注解  from表单格式 就可以直接提交
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));//密码加密 将前端的明文密码转为32位MD5

        ComminResp objectComminResp = new ComminResp<>();
        userService.save(req);

        return objectComminResp;
    }

    @PostMapping("/reset-password")//保存书籍  一般保存类用post
    public ComminResp resetPassword(@RequestBody @Valid UserResetPasswordReq req) {//json格式的数据要用@RequestBody 注解  from表单格式 就可以直接提交
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));//密码加密 将前端的明文密码转为32位MD5

        ComminResp objectComminResp = new ComminResp<>();
        userService.resetPassword(req);

        return objectComminResp;
    }

    @DeleteMapping("/delete/{id}")//删除书籍  一般保存类用post
    public ComminResp delete(@PathVariable long id) {//@PathVariable 用于获取url中的数据
        ComminResp objectComminResp = new ComminResp<>();
        userService.delete(id);

        return objectComminResp;
    }

    @PostMapping("/login")//保存书籍  一般保存类用post
    public ComminResp login(@RequestBody @Valid UserLoginReq req) {//json格式的数据要用@RequestBody 注解  from表单格式 就可以直接提交
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));//密码加密 将前端的明文密码转为32位MD5

        ComminResp<UserLoginResp> resp = new ComminResp<>();
        UserLoginResp login = userService.login(req);

        Long token = snowFlake.nextId();
        LOG.info("生成单点登录token： {}，并放入redis", token);
                login.setToken(token.toString());//将token存入login中
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(login), 3600 * 24, TimeUnit.SECONDS);//将token存入redis中 有效期为一天

        resp.setContent(login);
        return resp;
    }
}
