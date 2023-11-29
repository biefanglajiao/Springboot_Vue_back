package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.NeedhelpMapper;
import com.example.springboot_vue_back.Utils.SnowFlake;
import com.example.springboot_vue_back.domain.Needhelp;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/28 15:26
 **/
@Service
public class NeedhelpService {
    @Resource
    private NeedhelpMapper needhelpMapper;
    @Resource
    private SnowFlake snowFlake;

    //    保存反馈数据
    public void save(Needhelp needhelp) {

        needhelp.setId(snowFlake.nextId());
        needhelpMapper.save(needhelp);
    }

    //查询所有数据


    //查询单个数据
    public  int selectone(long docid,String email){
        System.out.println("docid:"+docid+"email:"+email);
        return  needhelpMapper.selectone(docid,email);

    }

    //根据参与种类查询 已审批在前 未审批在后


    //根据参与种类查询 已审批在后 未审批在前

    //审批操作

}
