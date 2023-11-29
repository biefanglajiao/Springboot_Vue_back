package com.example.springboot_vue_back.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.springboot_vue_back.Utils.SnowFlake;
import com.example.springboot_vue_back.domain.Needhelp;
import com.example.springboot_vue_back.req.DocQueryReq;
import com.example.springboot_vue_back.req.DocSaveReq;
import com.example.springboot_vue_back.req.NeedhelpReq;
import com.example.springboot_vue_back.req.UserLoginReq;
import com.example.springboot_vue_back.resp.ComminResp;
import com.example.springboot_vue_back.resp.DocQueryResp;
import com.example.springboot_vue_back.resp.PageResp;
import com.example.springboot_vue_back.resp.UserLoginResp;
import com.example.springboot_vue_back.service.DocService;
import com.example.springboot_vue_back.service.EbookInvolvedService;
import com.example.springboot_vue_back.service.MailSendService;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/doc")
public class DocController {
    @Resource
    private RedisTemplate redisTemplate;
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(UserController.class);
    @Resource
    private DocService docService;

    @Resource
    private  EbookInvolvedService ebookInvolvedService;
    @Resource
    private SnowFlake snowFlake;//雪花算

    @Resource
    private MailSendService mailSendService;



    @GetMapping("/list")//模糊查询书籍
    public ComminResp list(DocQueryReq req) {
        ComminResp<PageResp<DocQueryResp>> objectComminResp = new ComminResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        objectComminResp.setContent(list);
        return objectComminResp;
    }

    /**
     * 点赞
     *
     * @param id
     * @return
     */
    @GetMapping("/increaseVoteView/{id}")
    public ComminResp increaseVoteView(@PathVariable Long id) {
        ComminResp objectComminResp = new ComminResp<>();
        docService.increaseVoteView(id);
        return objectComminResp;
    }

    @GetMapping("/find-content/{id}")
    public ComminResp findContent(@PathVariable Long id) {
        //@PathVariable  注解的作用是将请求URL中的模板变量映射到功能处理方法的参数上
        ComminResp<String> objectComminResp = new ComminResp<>();
        String content = docService.findContent(id);
        boolean b = ebookInvolvedService.selectInvolved(id);
        objectComminResp.setContent(content);
        if (b){objectComminResp.setMessage("有效");}else {objectComminResp.setMessage("无效");}

        return objectComminResp;
    }

    @GetMapping("/all")//模糊查询书籍
    public ComminResp all() {
        ComminResp<List<DocQueryResp>> objectComminResp = new ComminResp<>();
        List<DocQueryResp> list = docService.all();
        objectComminResp.setContent(list);
        return objectComminResp;
    }

    @GetMapping("/allbyid/{ebookId}")// 根据id查询书籍  =》找到对应的文档树
    public ComminResp allbyid(@PathVariable Long ebookId) {
        ComminResp<List<DocQueryResp>> objectComminResp = new ComminResp<>();
        List<DocQueryResp> list = docService.allbyid(ebookId);
        objectComminResp.setContent(list);
        return objectComminResp;
    }

    @PostMapping("/save")//保存书籍  一般保存类用post
    public ComminResp save(@Valid @RequestBody DocSaveReq req) {//json格式的数据要用@RequestBody 注解  from表单格式 就可以直接提交
        ComminResp objectComminResp = new ComminResp<>();
        docService.save(req);


        return objectComminResp;
    }

    @DeleteMapping("/delete/{idsStr}")//保存书籍  一般保存类用post
    public ComminResp delete(@PathVariable String idsStr) {//@PathVariable 用于获取url中的数据
        ComminResp objectComminResp = new ComminResp<>();
        List<String> strings = Arrays.asList(idsStr.split(","));
        docService.delete(strings);

        return objectComminResp;
    }

//    ----反馈相关----
@PostMapping("/reply")//参与表单处理
public ComminResp reply(@RequestBody @Valid NeedhelpReq req) {//json格式的数据要用@RequestBody 注解  from表单格式 就可以直接提交

     Long docid=req.getId();//获取到对应的文档id、
    System.out.println("docid——————————————————————————————————————————————"+docid);
    ComminResp<UserLoginResp> resp = new ComminResp<>();


    Long token = snowFlake.nextId();

    req.setId(token);
    String codes = (String) redisTemplate.opsForValue().get(req.getCode().toString());
    System.out.println("codes——————————————————————————————————————————————"+codes);
    System.out.println("req.getEmail()——————————————————————————————————————————————"+req.getEmail());
    //字符串切割
    codes=codes.substring(1,codes.length()-1);//因为 mybatis里存的数据有双引号  所以要去掉
    if (codes==null){
        resp.setMessage("验证码已过期，请重新获取");
        resp.setSuccess(false);
        return resp;
    }else if (!codes.equals(req.getEmail())){
        resp.setMessage("验证码错误，请重新输入");
        resp.setSuccess(false);
        return resp;
    }

    //todo 存储信息逻辑
    //查对应的文档id  对应的参与种类
    //存储信息
    resp.setMessage("反馈成功");
    return resp;
}


    @GetMapping("/getcheck/{email}")//获取验证码
    public ComminResp getcode(@PathVariable @Valid  String email) {//json格式的数据要用@RequestBody 注解  from表单格式 就可以直接提交
        //对邮箱进行验证
        if (!email.matches("^\\w+@(\\w+\\.)+\\w+$")) {
            ComminResp<UserLoginResp> resp = new ComminResp<>();
            resp.setMessage("邮箱格式不正确");
            resp.setSuccess(false);
            return resp;
        }


        ComminResp<UserLoginResp> resp = new ComminResp<>();
//       生成验证码五位数字的随机验证码
        String code = String.valueOf((int)((Math.random()*9+1)*10000));

        mailSendService.sendMail(email,"参与系统绑定验证码","您的验证码为："+code);
        LOG.info("生成随机验证码： {}，并放入redis", code);
        System.out.println("生成随机验证码： {}，并放入redis——————————————————————————————————"+code);

        redisTemplate.opsForValue().set(code, JSONObject.toJSONString(email), 600 , TimeUnit.SECONDS);//将验证码存入redis中 有效期为十分钟
//前后端都用tostring  才能保证数据一直可以被对比
        resp.setMessage("验证码已发送至您的邮箱，请注意查收");
        return resp;
    }
}
