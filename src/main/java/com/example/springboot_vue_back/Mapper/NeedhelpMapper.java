package com.example.springboot_vue_back.Mapper;

import com.example.springboot_vue_back.domain.Needhelp;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/28 15:28
 **/
@Mapper
public interface NeedhelpMapper {
    public int save(Needhelp needhelp);
}
