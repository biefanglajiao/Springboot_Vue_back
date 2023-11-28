package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.NeedhelpMapper;
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
//    保存反馈数据
    public void save(Needhelp needhelp) {
        needhelpMapper.save(needhelp);
    }
}
