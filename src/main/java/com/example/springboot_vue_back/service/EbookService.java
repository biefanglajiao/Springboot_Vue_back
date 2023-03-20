package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.EbookMapper;

import com.example.springboot_vue_back.Utils.CopyUtils;
import com.example.springboot_vue_back.domain.Ebook;
import com.example.springboot_vue_back.domain.EbookExample;
import com.example.springboot_vue_back.req.EbookReq;
import com.example.springboot_vue_back.resp.EbookResp;
import com.fasterxml.jackson.databind.util.BeanUtil;
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
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
         criteria.andNameLike("%" + req.getName() + "%");//createCriteria()相当于while
        }

        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

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
