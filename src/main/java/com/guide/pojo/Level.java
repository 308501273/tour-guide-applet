package com.guide.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "tb_level")
public class Level {
    /**
     * 级别id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 级别名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态(1启用，0未启用）
     */
    private Integer status;

}