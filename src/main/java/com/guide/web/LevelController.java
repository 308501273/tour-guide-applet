package com.guide.web;

import com.guide.pojo.Level;
import com.guide.service.LevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("level")
public class LevelController {

    @Autowired
    private LevelService levelService;

    @GetMapping("list")
    public List<Level> getLevelList(){
        return levelService.getLevelList();
    }
}
