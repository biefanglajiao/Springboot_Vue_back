package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.EbookMapper;

import com.example.springboot_vue_back.Utils.CopyUtils;
import com.example.springboot_vue_back.domain.Ebook;
import com.example.springboot_vue_back.domain.EbookExample;
import com.example.springboot_vue_back.req.EbookReq;
import com.example.springboot_vue_back.resp.EbookResp;
import com.fasterxml.jackson.databind.util.BeanUtil;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req) {
//        分页查询=》查询总数+查当前页数据
        PageHelper.startPage(1,3);//第一个参数是页码，第二个参数是每页显示的条数 只对第一个查询语句起作用



        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
         criteria.andNameLike("%" + req.getName() + "%");//createCriteria()相当于while
        }

        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo=new PageInfo<>(ebookList);
        pageInfo.getTotal();//返回总条数  配合前端可以通过知道总条数查询分页

//        //将Ebook类型转为EbookResp类型
//        List<EbookResp> ebookRespList =new ArrayList<>();
//        for (Ebook ebook : ebookList) {
//            EbookResp ebookResp = new EbookResp();
//            BeanUtils.copyProperties(ebook,ebookResp);
//            ebookRespList.add(ebookResp);
//        }

        //将Ebook类型转为EbookResp类型 使用copyutils工具类
        List<EbookResp> ebookRespList = CopyUtils.copyList(ebookList, EbookResp.class);
        return  ebookRespList;

    }

}
