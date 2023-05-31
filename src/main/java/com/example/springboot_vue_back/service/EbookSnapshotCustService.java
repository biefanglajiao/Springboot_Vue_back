package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.EbookSnapshotMapperCust;
import com.example.springboot_vue_back.resp.StatisticResp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/5/16 15:00
 **/
@Service
public class EbookSnapshotCustService {
    @Resource
    private EbookSnapshotMapperCust ebookSnapshotMapperCust;

    @Transactional
    public void doEbookSnapshotMapperCust(){
        ebookSnapshotMapperCust.getNowInfo();
        ebookSnapshotMapperCust.upNowInfo();
        ebookSnapshotMapperCust.upSubInfo();

    }
    public List<StatisticResp> getStatistic(){
        return ebookSnapshotMapperCust.getstatistic();
    } public List<StatisticResp> get30Statistic(){
        return ebookSnapshotMapperCust.get30statistic();
    }
}
