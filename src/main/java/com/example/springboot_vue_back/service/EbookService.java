package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.EbookMapper;

import com.example.springboot_vue_back.Utils.CopyUtils;
import com.example.springboot_vue_back.Utils.SnowFlake;
import com.example.springboot_vue_back.domain.Ebook;
import com.example.springboot_vue_back.domain.EbookExample;
import com.example.springboot_vue_back.req.EbookQueryReq;
import com.example.springboot_vue_back.req.EbookSaveReq;
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
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    @Autowired
    private SnowFlake snowFlake;

    public PageResp<EbookQuieryResp> list(EbookQueryReq req) {
//        分页查询=》查询总数+查当前页数据
        PageHelper.startPage(req.getPage(), req.getSize());//第一个参数是页码，第二个参数是每页显示的条数 只对第一个查询语句起作用


        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");//createCriteria()相当于while
        }

        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        pageInfo.getTotal();//返回总条数  配合前端可以通过知道总条数查询分页

//        //将Ebook类型转为EbookResp类型
//        List<EbookResp> ebookRespList =new ArrayList<>();
//        for (Ebook ebook : ebookList) {
//            EbookResp ebookResp = new EbookResp();
//            BeanUtils.copyProperties(ebook,ebookResp);
//            ebookRespList.add(ebookResp);
//        }

        //将Ebook类型转为EbookResp类型 使用copyutils工具类
        List<EbookQuieryResp> ebookRespList = CopyUtils.copyList(ebookList, EbookQuieryResp.class);

        PageResp<EbookQuieryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(ebookRespList);

        return pageResp;
    }




    public void save(EbookSaveReq req){
        Ebook ebook= CopyUtils.copy(req,Ebook.class);//将请求参数更新为实体
        if (ObjectUtils.isEmpty(req.getId())){//判断是否存在
            //不存在 新增

            ebook.setId( snowFlake.nextId());//将生成的雪花算法赋给id
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+ebook.getId());
            //文档 阅读 点赞数  初始默认0
            ebook.setDocCount(0);
            ebook.setViewCount(0);
            ebook.setVoteCount(0);
            ebookMapper.insert(ebook);
        }else {
            //更新
            ebookMapper.updateByPrimaryKey(ebook);
        }

    }

    public void delete(long id){
        ebookMapper.deleteByPrimaryKey(id);

    }

}
