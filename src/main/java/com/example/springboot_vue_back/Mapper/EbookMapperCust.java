package com.example.springboot_vue_back.Mapper;

//@Mapper  //因为有了MapperScan注解   所以就不需要写Mapper了
public interface EbookMapperCust {
    public int increaseViewCount(long id);
}
