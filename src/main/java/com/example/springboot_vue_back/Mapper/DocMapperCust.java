package com.example.springboot_vue_back.Mapper;

import com.example.springboot_vue_back.domain.Test;

import java.util.List;

//@Mapper  //因为有了MapperScan注解   所以就不需要写Mapper了
public interface DocMapperCust {
    public int increaseViewCount(long id);
    public  int increaseVoteCount(long id);
}
