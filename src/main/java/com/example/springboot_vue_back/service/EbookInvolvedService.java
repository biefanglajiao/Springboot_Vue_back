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


    //更新是否可参与权限
    public boolean isInvolved(DocSaveReq req, long id) {
        EbookInvolved ebookInvolved = CopyUtils.copy(req, EbookInvolved.class);
        ebookInvolved.setId(id);//将文章id改为小标题id


        //如果存在  就修改
        if (ebookInvolvedMapper.exitInvolved(ebookInvolved.getId()) > 0) {
            return ebookInvolvedMapper.updateInvolved(ebookInvolved.getId(), ebookInvolved.isInvolved());
        } else {
            //如果不存在  就添加
            return ebookInvolvedMapper.insertInvolved(ebookInvolved.getId(), ebookInvolved.isInvolved());
        }

    }
}
