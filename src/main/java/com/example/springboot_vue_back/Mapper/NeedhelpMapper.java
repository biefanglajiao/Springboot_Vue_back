package com.example.springboot_vue_back.Mapper;

import com.example.springboot_vue_back.domain.Needhelp;
import com.example.springboot_vue_back.resp.ApprovalResp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/28 15:28
 **/
@Mapper
public interface NeedhelpMapper {
    public int save(Needhelp needhelp);

   public  int selectone(long docid,String email);
   public  List<ApprovalResp> select(String email);

    public int delete(long docid,String email);

    public int update(long docid,String email,boolean approval);

    public List<ApprovalResp> selectall();

    public List<ApprovalResp> selectallOrderByOptionAscApproval(boolean option);

    public List<ApprovalResp> selectallOrderByOptionDescApproval(boolean option);

    public List<ApprovalResp> selectallOrderByApprovalAscOption(boolean approval);

    public List<ApprovalResp> selectallOrderByApprovalDescOption(boolean approval);



}
