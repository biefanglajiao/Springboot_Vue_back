package com.example.springboot_vue_back.Controller;

import com.example.springboot_vue_back.req.EbookQueryReq;
import com.example.springboot_vue_back.req.EbookSaveReq;
import com.example.springboot_vue_back.resp.ComminResp;
import com.example.springboot_vue_back.resp.EbookQuieryResp;
import com.example.springboot_vue_back.resp.PageResp;
import com.example.springboot_vue_back.service.EbookService;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/list")//模糊查询书籍
    public ComminResp list(@Valid EbookQueryReq req) {
        ComminResp<PageResp<EbookQuieryResp>> objectComminResp = new ComminResp<>();
        PageResp<EbookQuieryResp> list = ebookService.list(req);
        objectComminResp.setContent(list);
        return objectComminResp;
    }

    @PostMapping("/save")//保存书籍  一般保存类用post
    public ComminResp save(@RequestBody @Valid EbookSaveReq req) {//json格式的数据要用@RequestBody 注解  from表单格式 就可以直接提交
        ComminResp objectComminResp = new ComminResp<>();
        ebookService.save(req);

        return objectComminResp;
    }

    @DeleteMapping("/delete/{id}")//删除书籍  一般保存类用post
    public ComminResp delete(@PathVariable long id) {//@PathVariable 用于获取url中的数据
        ComminResp objectComminResp = new ComminResp<>();
        ebookService.delete(id);

        return objectComminResp;
    }
}
