package com.example.springboot_vue_back.Mapper;

import com.example.springboot_vue_back.resp.StatisticResp;

import java.util.List;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/5/16 14:46
 **/
public interface EbookSnapshotMapperCust {

// # #id连续更新方法：
//# 为所有电子书生成一个今天的记录，如果没有
//# 更新总阅读数 总点赞数
//# 更新今日阅读数，点赞数



    /**
     *  为所有电子书生成一个今天的记录，如果没有
     *  insert into `ebook_snapshot` (`ebook_id`, `date`, `view_count`, `vote_count`, `view_increase`, `vote_increase`)
     *     select id, curdate(), 0, 0, 0, 0
     *     from ebook
     *     where not exists (select 1
     *             from ebook_snapshot
     *             where ebook_snapshot.ebook_id = ebook.id and ebook_snapshot.date = curdate());
     * @return
     */
    public int getNowInfo();

    /**
     * 更新总阅读数 总点赞数
     *  update ebook_snapshot t1,ebook t2
     *      *     set t1.view_count = t2.view_count,
     *      *     t1.vote_count = t2.vote_count
     *      *     where t1.ebook_id = t2.id
     *      *     and t1.date = curdate();
     * @return
     */
    public int upNowInfo();

    /**
     *  更新今日阅读数，点赞数
     *  update ebook_snapshot t1 left join (select ebook_id, view_count, vote_count
     *             from ebook_snapshot
     *                                                 where date = date_sub(curdate(), interval 1 day)) t2
     *     on t1.ebook_id = t2.ebook_id
     *     set t1.view_increase = t1.view_count - ifnull(t2.view_count,0),
     *     t1.vote_increase = t1.vote_count - ifnull(t2.vote_count,0)
     *     where  t1.date = curdate();
     */
    public int upSubInfo();

    /**
     *
     #获取昨日数据
     select ebook_id, view_count, vote_count
     from ebook_snapshot
     where date = date_sub(curdate(), interval 1 day);


     */



    public List<StatisticResp> getstatistic();
}
