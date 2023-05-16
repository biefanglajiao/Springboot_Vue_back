package com.example.springboot_vue_back.job;

import com.example.springboot_vue_back.Mapper.DocMapperCust;
import com.example.springboot_vue_back.Mapper.EbookSnapshotMapperCust;
import com.example.springboot_vue_back.Utils.SnowFlake;
import com.example.springboot_vue_back.service.EbookSnapshotCustService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/***
 * 定时任务 可以使用定时器来做一些定时的任务，比如定时清理缓存，定时发送邮件等等
 * 比如 电子书数据（文档数 点赞总数，阅读总数）的更新
 */
 @Component
 public class EbookSnapshotJob {

    private static final Logger LOG = LoggerFactory.getLogger(EbookSnapshotJob.class);
@Resource
private EbookSnapshotCustService ebookSnapshotCustService;

@Resource
private SnowFlake   snowFlake;

    /**
     * 自定义cron表达式跑批
     * 没60秒更新一次电子书的附属信息
     */
    @Scheduled(cron = "*/60 * * * * ?")
    public void cron()  {
        //增加日志流水号
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));


       LOG.info("更新电子书快照的附属信息：  开始 ");

       long start=System.currentTimeMillis();
        ebookSnapshotCustService.doEbookSnapshotMapperCust();
         LOG.info("更新电子书快照的附属信息：  结束 ,耗时：{}毫秒",System.currentTimeMillis()-start);
    }

 }
