package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.ContentMapper;
import com.example.springboot_vue_back.Mapper.DocMapper;
import com.example.springboot_vue_back.Mapper.DocMapperCust;
import com.example.springboot_vue_back.Mapper.EbookMapperCust;
import com.example.springboot_vue_back.Utils.CopyUtils;
import com.example.springboot_vue_back.Utils.SnowFlake;
import com.example.springboot_vue_back.domain.Content;
import com.example.springboot_vue_back.domain.Doc;
import com.example.springboot_vue_back.domain.DocExample;
import com.example.springboot_vue_back.req.DocQueryReq;
import com.example.springboot_vue_back.req.DocSaveReq;
import com.example.springboot_vue_back.resp.DocQueryResp;
import com.example.springboot_vue_back.resp.PageResp;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {
    @Resource
    private DocMapper docMapper;
    @Resource
    private EbookMapperCust ebookMapperCust;

    @Resource
    private DocMapperCust docMapperCust;
    @Autowired
    private SnowFlake snowFlake;
    @Resource
    private ContentMapper contentMapper;

    public List<DocQueryResp> all() {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");//根据sort字段升序排列
        List<Doc> docList = docMapper.selectByExample(docExample);
        //将Doc类型转为DocResp类型 使用copyutils工具类
        List<DocQueryResp> docRespList = CopyUtils.copyList(docList, DocQueryResp.class);

        return docRespList;
    }
    public List<DocQueryResp> allbyid(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");//根据sort字段升序排列
        List<Doc> docList = docMapper.selectByExample(docExample);
        ebookMapperCust.increaseViewCount(ebookId);//增加书的阅读量
        //将Doc类型转为DocResp类型 使用copyutils工具类
        List<DocQueryResp> docRespList = CopyUtils.copyList(docList, DocQueryResp.class);

        return docRespList;
    }

//    public PageResp<DocQueryResp> list(DocQueryReq req) {
////        分页查询=》查询总数+查当前页数据
//        PageHelper.startPage(req.getPage(), req.getSize());//第一个参数是页码，第二个参数是每页显示的条数 只对第一个查询语句起作用
//
//
//        DocExample docExample = new DocExample();
//        docExample.setOrderByClause("sort asc");//根据sort字段升序排列
//       DocExample.Criteria criteria = docExample.createCriteria();
//        if (!ObjectUtils.isEmpty(req.getName())) {
//            criteria.andNameLike("%" + req.getName() + "%");//createCriteria()相当于while
//        }
//        List<Doc> docList = docMapper.selectByExample(docExample);
//
//        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
//        pageInfo.getTotal();//返回总条数  配合前端可以通过知道总条数查询分页
//
////        //将Doc类型转为DocResp类型
////        List<DocResp> docRespList =new ArrayList<>();
////        for (Doc doc : docList) {
////            DocResp docResp = new DocResp();
////            BeanUtils.copyProperties(doc,docResp);
////            docRespList.add(docResp);
////        }
//
//        //将Doc类型转为DocResp类型 使用copyutils工具类
//        List<DocQueryResp> docRespList = CopyUtils.copyList(docList, DocQueryResp.class);
//
//        PageResp<DocQueryResp> pageResp = new PageResp<>();
//        pageResp.setTotal(pageInfo.getTotal());
//        pageResp.setList(docRespList);
//
//        return pageResp;
//    }
    public PageResp<DocQueryResp> list(DocQueryReq req) {
       DocExample docExample=new DocExample();
        docExample.setOrderByClause("sort asc");//根据sort字段升序排列
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");//createCriteria()相当于while
        }
        List<Doc> docList = docMapper.selectByExample(docExample);
        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        pageInfo.getTotal();//返回总条数  配合前端可以通过知道总条数查询分页
//        //将Doc类型转为DocResp类型
//        List<DocResp> docRespList =new ArrayList<>();
//        for (Doc doc : docList) {
//            DocResp docResp = new DocResp();
//            BeanUtils.copyProperties(doc,docResp);
//            docRespList.add(docResp);
//        }

        //将Doc类型转为DocResp类型 使用copyutils工具类
        List<DocQueryResp> docRespList = CopyUtils.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(docRespList);

        return pageResp;
    }


    public void save(DocSaveReq req) {
        Doc doc = CopyUtils.copy(req, Doc.class);//将请求参数更新为实体
        Content content = CopyUtils.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {//判断是否存在
            //不存在 新增

            doc.setId(snowFlake.nextId());//将生成的雪花算法赋给id
           doc.setViewCount(0);
           doc.setVoteCount(0);

            //文档 阅读 点赞数  初始默认0
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            //更新
            docMapper.updateByPrimaryKey(doc);
            int count=contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count==0)contentMapper.insert(content);
        }

    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);

    }
    public String findContent(Long id ){
        Content content = contentMapper.selectByPrimaryKey(id);
        docMapperCust.increaseViewCount(id) ;//文档阅读数+1
                if (ObjectUtils.isEmpty(content))
            return "";
        return content.getContent();
    }

    public void increaseVoteView(Long id){
        docMapperCust.increaseVoteCount(id);//文档点赞数+1
    }

}
