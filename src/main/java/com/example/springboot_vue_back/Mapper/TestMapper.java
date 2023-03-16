package com.example.springboot_vue_back.Mapper;

import com.example.springboot_vue_back.domain.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface TestMapper {
    public List<Test> list();
}
