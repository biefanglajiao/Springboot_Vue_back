package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.TestMapper;
import com.example.springboot_vue_back.domain.Test;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestService {
    @Resource
    private TestMapper testMapper;

    public List<Test> list(){
        return  testMapper.list();
    }

}
