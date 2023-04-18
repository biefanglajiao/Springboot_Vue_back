package com.example.springboot_vue_back.Controller;

import com.example.springboot_vue_back.req.DocQueryReq;
import com.example.springboot_vue_back.req.DocSaveReq;
import com.example.springboot_vue_back.resp.ComminResp;
import com.example.springboot_vue_back.resp.DocQueryResp;
import com.example.springboot_vue_back.resp.PageResp;
import com.example.springboot_vue_back.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

    @GetMapping("/list")//模糊查询书籍
    public ComminResp list( DocQueryReq req) {
        ComminResp<PageResp<DocQueryResp>> objectComminResp = new ComminResp<>();
        PageResp<DocQueryResp> list = docService.list(req);
        objectComminResp.setContent(list);
        return objectComminResp;
    }

        @GetMapping("/find-content/{id}")//模糊查询书籍
    public ComminResp findContent( @PathVariable  Long id) {
        ComminResp<String> objectComminResp = new ComminResp<>();
      String content = docService.finfContent(id);
        objectComminResp.setContent(content);
        return objectComminResp;
    }

    @GetMapping("/all")//模糊查询书籍
    public ComminResp all() {
        ComminResp<List<DocQueryResp>> objectComminResp = new ComminResp<>();
       List<DocQueryResp> list = docService.all();
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
}
