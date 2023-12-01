package com.example.springboot_vue_back.Controller;

import com.example.springboot_vue_back.req.CategoryQueryReq;
import com.example.springboot_vue_back.req.CategorySaveReq;
import com.example.springboot_vue_back.resp.ComminResp;
import com.example.springboot_vue_back.resp.CategoryQueryResp;
import com.example.springboot_vue_back.resp.PageResp;
import com.example.springboot_vue_back.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

        @GetMapping("/list")//模糊查询书籍
        public ComminResp list( CategoryQueryReq req) {
            ComminResp<PageResp<CategoryQueryResp>> objectComminResp = new ComminResp<>();
            PageResp<CategoryQueryResp> list = categoryService.list(req);
            objectComminResp.setContent(list);
            return objectComminResp;
    }  @GetMapping("/all")//模糊查询书籍
    public ComminResp all() {
        ComminResp<List<CategoryQueryResp>> objectComminResp = new ComminResp<>();
       List<CategoryQueryResp> list = categoryService.all();
        objectComminResp.setContent(list);
        return objectComminResp;
    }

    @PostMapping("/save")//保存书籍  一般保存类用post
    public ComminResp save(@Valid @RequestBody CategorySaveReq req) {//json格式的数据要用@RequestBody 注解  from表单格式 就可以直接提交
        ComminResp objectComminResp = new ComminResp<>();
        categoryService.save(req);

        return objectComminResp;
    }

    @DeleteMapping("/delete/{id}")//保存书籍  一般保存类用post
    public ComminResp delete(@PathVariable long id) {//@PathVariable 用于获取url中的数据
        ComminResp objectComminResp = new ComminResp<>();
        categoryService.delete(id);

        return objectComminResp;
    }
}
