package com.guide.pojo;

import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@Table(name = "tb_level_apply")
public class LevelApply {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 导游id
     */
    @Column(name = "guider_id")
    private Integer guiderId;

    /**
     * 级别id
     */
    @Column(name = "level_id")
    private Integer levelId;

    /**
     * 新传证书url
     */
    @Column(name = "qualification_url")
    private String qualificationUrl;

    /**
     * 申请时间
     */
    @Column(name = "apply_time")
    private Date applyTime;

    /**
     * 申请状态（0审核中，1通过，2拒绝）
     */
    private Integer status;

}