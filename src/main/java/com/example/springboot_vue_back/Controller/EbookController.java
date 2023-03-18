package com.example.springboot_vue_back.Controller;

import com.example.springboot_vue_back.domain.Ebook;
import com.example.springboot_vue_back.req.EbookReq;
import com.example.springboot_vue_back.resp.ComminResp;
import com.example.springboot_vue_back.resp.EbookResp;
import com.example.springboot_vue_back.service.EbookService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

@Resource
private EbookService ebookService;

 @GetMapping("/list")//模糊查询书籍
    public ComminResp list(EbookReq req){
     ComminResp<List<EbookResp>> objectComminResp = new ComminResp<>();
     List<EbookResp> list=ebookService.list(req);
    objectComminResp.setContent(list);
    return  objectComminResp;



 }
}
