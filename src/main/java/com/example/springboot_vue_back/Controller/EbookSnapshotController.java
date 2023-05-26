package com.example.springboot_vue_back.Controller;

import com.example.springboot_vue_back.resp.ComminResp;
import com.example.springboot_vue_back.resp.StatisticResp;
import com.example.springboot_vue_back.service.EbookSnapshotCustService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/5/26 10:06
 **/
@RestController
@RequestMapping("/ebook-Snapshot")
public class EbookSnapshotController {
    @Resource
    private EbookSnapshotCustService ebookSnapshotCustService;
    @RequestMapping("get-statistic")
    public ComminResp getStatistic(){
        List<StatisticResp> statisticRespList = ebookSnapshotCustService.getStatistic();
        ComminResp<List<StatisticResp>> comminResp = new ComminResp<>();
        comminResp.setContent(statisticRespList);
        return comminResp;
    }
    @RequestMapping("get-30statistic")
    public ComminResp get30Statistic(){
        List<StatisticResp> statisticRespList = ebookSnapshotCustService.get30Statistic();
        ComminResp<List<StatisticResp>> comminResp = new ComminResp<>();
        comminResp.setContent(statisticRespList);
        return comminResp;
    }


}
