package com.example.springboot_vue_back.Mapper;

import com.example.springboot_vue_back.domain.EbookInvolved;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/27 10:42
 **/
public interface EbookInvolvedMapper {
    int exitInvolved(Long id);
    boolean updateInvolved(@Param("id") Long id, @Param("involved") boolean involved, @Param("option") boolean option);
    boolean insertInvolved(Long id, boolean involved,boolean option);
    boolean deleteInvolved(Long id);
    EbookInvolved selectInvolved(Long id);
}
