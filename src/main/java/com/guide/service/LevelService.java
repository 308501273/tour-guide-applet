package com.guide.service;

import com.guide.mapper.LevelMapper;
import com.guide.pojo.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LevelService {
    @Autowired
    private LevelMapper levelMapper;


    public List<Level> getLevelList() {
        return levelMapper.selectAll();
    }
}
