package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.CategoryMapper;
import com.example.springboot_vue_back.Utils.CopyUtils;
import com.example.springboot_vue_back.Utils.SnowFlake;
import com.example.springboot_vue_back.domain.Category;
import com.example.springboot_vue_back.domain.CategoryExample;
import com.example.springboot_vue_back.domain.Ebook;
import com.example.springboot_vue_back.domain.EbookExample;
import com.example.springboot_vue_back.req.CategoryQueryReq;
import com.example.springboot_vue_back.req.CategorySaveReq;
import com.example.springboot_vue_back.req.EbookQueryReq;
import com.example.springboot_vue_back.resp.CategoryQueryResp;
import com.example.springboot_vue_back.resp.EbookQuieryResp;
import com.example.springboot_vue_back.resp.PageResp;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlake snowFlake;

    public List<CategoryQueryResp> all() {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");//根据sort字段升序排列
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        //将Category类型转为CategoryResp类型 使用copyutils工具类
        List<CategoryQueryResp> categoryRespList = CopyUtils.copyList(categoryList, CategoryQueryResp.class);


        return categoryRespList;
    }

//    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
////        分页查询=》查询总数+查当前页数据
//        PageHelper.startPage(req.getPage(), req.getSize());//第一个参数是页码，第二个参数是每页显示的条数 只对第一个查询语句起作用
//
//
//        CategoryExample categoryExample = new CategoryExample();
//        categoryExample.setOrderByClause("sort asc");//根据sort字段升序排列
//       CategoryExample.Criteria criteria = categoryExample.createCriteria();
//        if (!ObjectUtils.isEmpty(req.getName())) {
//            criteria.andNameLike("%" + req.getName() + "%");//createCriteria()相当于while
//        }
//        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
//
//        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
//        pageInfo.getTotal();//返回总条数  配合前端可以通过知道总条数查询分页
//
////        //将Category类型转为CategoryResp类型
////        List<CategoryResp> categoryRespList =new ArrayList<>();
////        for (Category category : categoryList) {
////            CategoryResp categoryResp = new CategoryResp();
////            BeanUtils.copyProperties(category,categoryResp);
////            categoryRespList.add(categoryResp);
////        }
//
//        //将Category类型转为CategoryResp类型 使用copyutils工具类
//        List<CategoryQueryResp> categoryRespList = CopyUtils.copyList(categoryList, CategoryQueryResp.class);
//
//        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
//        pageResp.setTotal(pageInfo.getTotal());
//        pageResp.setList(categoryRespList);
//
//        return pageResp;
//    }
    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
       CategoryExample categoryExample=new CategoryExample();
        categoryExample.setOrderByClause("sort asc");//根据sort字段升序排列
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");//createCriteria()相当于while
        }
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        pageInfo.getTotal();//返回总条数  配合前端可以通过知道总条数查询分页
//        //将Category类型转为CategoryResp类型
//        List<CategoryResp> categoryRespList =new ArrayList<>();
//        for (Category category : categoryList) {
//            CategoryResp categoryResp = new CategoryResp();
//            BeanUtils.copyProperties(category,categoryResp);
//            categoryRespList.add(categoryResp);
//        }

        //将Category类型转为CategoryResp类型 使用copyutils工具类
        List<CategoryQueryResp> categoryRespList = CopyUtils.copyList(categoryList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(categoryRespList);

        return pageResp;
    }


    public void save(CategorySaveReq req) {
        Category category = CopyUtils.copy(req, Category.class);//将请求参数更新为实体
        if (ObjectUtils.isEmpty(req.getId())) {//判断是否存在
            //不存在 新增

            category.setId(snowFlake.nextId());//将生成的雪花算法赋给id
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + category.getId());
            //文档 阅读 点赞数  初始默认0

            categoryMapper.insert(category);
        } else {
            //更新
            categoryMapper.updateByPrimaryKey(category);
        }

    }

    public void delete(long id) {
        categoryMapper.deleteByPrimaryKey(id);

    }

}
