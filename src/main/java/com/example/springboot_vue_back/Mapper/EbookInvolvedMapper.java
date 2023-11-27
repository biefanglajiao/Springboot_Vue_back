package com.example.springboot_vue_back.Mapper;

/**
 * @Author: 常兆海
 * @Description:
 * @DateTime: 2023/11/27 10:42
 **/
public interface EbookInvolvedMapper {
    int exitInvolved(Long ebookid);
    boolean updateInvolved(Long ebookid, boolean involved);
    boolean insertInvolved(Long ebookid, boolean involved);
}
