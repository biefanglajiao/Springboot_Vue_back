package com.example.springboot_vue_back.service;

import com.example.springboot_vue_back.Mapper.EbookMapper;

import com.example.springboot_vue_back.domain.Ebook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<Ebook> list() {
        return ebookMapper.selectByExample(null);
    }

}
