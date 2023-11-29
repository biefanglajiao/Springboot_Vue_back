package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.NeedhelpMapper;
import com.example.springboot_vue_back.Utils.SnowFlake;
import com.example.springboot_vue_back.domain.Needhelp;
import com.example.springboot_vue_back.resp.ApprovalResp;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
    public List<ApprovalResp> selectall() {
        return needhelpMapper.selectall();
    }


    //查询单个数据
    public  int selectone(long docid,String email){
        System.out.println("docid:"+docid+"email:"+email);
        return  needhelpMapper.selectone(docid,email);
    }

    //根据参与种类查询 已审批在前 未审批在后
    public List<ApprovalResp> selectallOrderByOptionAscApproval(boolean option) {
        return needhelpMapper.selectallOrderByOptionAscApproval(option);
    }

    //根据参与种类查询 已审批在后 未审批在前
    public List<ApprovalResp> selectallOrderByOptionDescApproval(boolean option) {
        return needhelpMapper.selectallOrderByOptionDescApproval(option);
    }
    //根据审批结果查询  参与类在前 未参与类在后
    public List<ApprovalResp> selectallOrderByApprovalAscOption(boolean approval) {
        return needhelpMapper.selectallOrderByApprovalAscOption(approval);
    }
    //根据审批结果查询  参与类在后 未参与类在前
    public List<ApprovalResp> selectallOrderByApprovalDescOption(boolean approval) {
        return needhelpMapper.selectallOrderByApprovalDescOption(approval);
    }
    //审批操作
    public int update(long docid,String email,boolean approval) {
        return needhelpMapper.update(docid,email,approval);
    }
    //删除操作
    public int delete(long docid,String email) {
       return needhelpMapper.delete(docid,email);
    }

}
