package com.guide.service;

import com.guide.common.utils.ConstantClassField;
import com.guide.mapper.LevelMapper;
import com.guide.pojo.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class LevelService {
    @Autowired
    private LevelMapper levelMapper;

    public List<Level> getLevelList() {
        Example example = new Example(Level.class);
        example.createCriteria().andEqualTo("status", ConstantClassField.LEVEL_NORMAL);
        return levelMapper.selectByExample(example);
    }
}
