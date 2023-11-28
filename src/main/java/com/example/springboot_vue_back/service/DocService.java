package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.ContentMapper;
import com.example.springboot_vue_back.Mapper.DocMapper;
import com.example.springboot_vue_back.Mapper.DocMapperCust;
import com.example.springboot_vue_back.Mapper.EbookMapperCust;
import com.example.springboot_vue_back.Utils.CopyUtils;
import com.example.springboot_vue_back.Utils.RedisUtil;
import com.example.springboot_vue_back.Utils.RequestContext;
import com.example.springboot_vue_back.Utils.SnowFlake;
import com.example.springboot_vue_back.domain.Content;
import com.example.springboot_vue_back.domain.Doc;
import com.example.springboot_vue_back.domain.DocExample;
import com.example.springboot_vue_back.domain.EbookInvolved;
import com.example.springboot_vue_back.exception.BusinessException;
import com.example.springboot_vue_back.exception.BusinessExceptionCode;
import com.example.springboot_vue_back.req.DocQueryReq;
import com.example.springboot_vue_back.req.DocSaveReq;
import com.example.springboot_vue_back.resp.DocQueryResp;
import com.example.springboot_vue_back.resp.PageResp;
import com.example.springboot_vue_back.websocket.WebSocketServer;
import com.github.pagehelper.PageInfo;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {
    @Resource
    private EbookInvolvedService ebookInvolvedService;
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
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private websocketAsyncService websocketAsyncService;

    @Resource
    private RocketMQTemplate rocketMQTemplate;


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
        DocExample docExample = new DocExample();
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

    /**
     * @param req
     * @功能: 保存
     * @注解功能: 开启事务 与异步化同理: 事务操作与异步相同 添加事务注解的方法需要在另外一个类被调用
     * @事务的作用: 在这个方法中涉及到了对两个表的操作, 添加事务可以让他俩要么都成功  要么都失败
     */
    @Transactional
    public void save(DocSaveReq req) {
        Doc doc = CopyUtils.copy(req, Doc.class);//将请求参数更新为实体
        Content content = CopyUtils.copy(req, Content.class);
        EbookInvolved ebookInvolved = CopyUtils.copy(req, EbookInvolved.class);
        if (ObjectUtils.isEmpty(req.getId())) {//判断是否存在
            //不存在 新增

            doc.setId(snowFlake.nextId());//将生成的雪花算法赋给id
            ebookInvolvedService.insertInvolved(ebookInvolved.isInvolved(), doc.getId() ,ebookInvolved.isOption());//判断是否可参与
            doc.setViewCount(0);
            doc.setVoteCount(0);

            //文档 阅读 点赞数  初始默认0
            docMapper.insert(doc);

            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            //更新
            docMapper.updateByPrimaryKey(doc);

//            System.out.println("doc.getId():" + doc.getId());
//            System.out.println("ebookInvolved.isInvolved():" + ebookInvolved.isInvolved());
//            System.out.println("ebookInvolved.isOption():" + ebookInvolved.isOption());
            ebookInvolvedService.updateInvolved(ebookInvolved.isInvolved(), req.getId(),ebookInvolved.isOption());//判断是否可参与  更新部分
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (count == 0) contentMapper.insert(content);
        }

    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
        //将ids转为long型
        ids.forEach(
                id -> {
                    ebookInvolvedService.deleteInvolved(Long.parseLong(id));//删除对应的可参与权限
                }
        );
        //  ebookInvolvedService.deleteInvolved(Long.parseLong(ids.get(0)));//删除对应的可参与权限

    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        docMapperCust.increaseViewCount(id);//文档阅读数+1
        if (ObjectUtils.isEmpty(content))
            return "";
        return content.getContent();
    }

    public void increaseVoteView(Long id) {
// 旧：
// docMapperCust.increaseVoteCount(id);//文档点赞数+1
        //改进： 远程IP+doc.id作为key，24小时不能重复
        String key = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE" + id + "_" + key, 60 * 60 * 24)) {
            docMapperCust.increaseVoteCount(id);//文档点赞数+1
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
//推送消息
        Doc doc = docMapper.selectByPrimaryKey(id);
        //解决流水号丢失问题
        String logId = MDC.get("LOG_ID");

//旧:      webSocketServer.sendInfo("【"+doc.getName()+"】被点赞");
        //改进 异步化操作
//        websocketAsyncService.sendInfo("【"+doc.getName()+"】被点赞",logId);

        //改进2.0 通过mq来实现
        rocketMQTemplate.convertAndSend("VOTE_TOPIC", "【" + doc.getName() + "】被点赞");

    }

    /**
     * 更新电子书信息(文档数，点赞总数 阅读总数)
     */
    public void updateEbookInfo() {
        docMapperCust.updateEbookInfo();
    }

}
