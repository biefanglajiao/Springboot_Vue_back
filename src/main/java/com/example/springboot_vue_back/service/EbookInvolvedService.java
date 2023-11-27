package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Controller.UserController;
import com.example.springboot_vue_back.Mapper.EbookInvolvedMapper;
import com.example.springboot_vue_back.Utils.CopyUtils;
import com.example.springboot_vue_back.domain.EbookInvolved;
import com.example.springboot_vue_back.req.DocSaveReq;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/27 10:40
 **/
@Service
public class EbookInvolvedService {
    @Resource
    private EbookInvolvedMapper ebookInvolvedMapper;


    //新增是否可参与权限
    public boolean insertInvolved(boolean isInvolved, long id) {


        //如果不存在  就添加
        return ebookInvolvedMapper.insertInvolved(id, isInvolved);
    }
    //修改
    public boolean updateInvolved(boolean isInvolved, long id) {
        //如果不存在  就添加
        return ebookInvolvedMapper.updateInvolved(id, isInvolved);
    }



    //删除文章时删除对应数据
    public boolean deleteInvolved(long id) {
        return ebookInvolvedMapper.deleteInvolved(id);
    }

    //查询是否可参与
    public boolean selectInvolved(long id) {
        EbookInvolved ebookInvolved = ebookInvolvedMapper.selectInvolved(id);
        return ebookInvolved.isInvolved();

    }
}
