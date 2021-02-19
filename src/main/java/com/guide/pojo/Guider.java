package com.guide.pojo;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "tb_guider")
public class Guider {
    /**
     * 主键id
     */
    @Id
    private Integer id;

    /**
     * 微信小程序唯一id
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 服务地址
     */
    private String address;

    /**
     * 收费标准（单位/小时）
     */
    @Column(name = "unitFee")
    private Integer unitfee;

    /**
     * 服务态度评分
     */
    @Column(name = "service_attitude_score")
    private Double serviceAttitudeScore;

    /**
     * 专业度评分
     */
    @Column(name = "professional_score")
    private Double professionalScore;

    /**
     * 综合评分
     */
    @Column(name = "comprehensive_score")
    private Double comprehensiveScore;

    /**
     * 导游提供服务时长（单位/小时）
     */
    @Column(name = "service_time")
    private Integer serviceTime;

    /**
     * 累计服务总人数
     */
    private Integer servedPeopleNum;

    /**
     * 身份证信息(照片url,三张，正反手持)
     */
    @Column(name = "id_card_url")
    private String idCardUrl;

    /**
     * 导游资格证书(图片url）
     */
    @Column(name = "qualification_url")
    private String qualificationUrl;

    /**
     * 账号状态
     */
    private Integer status;

    /**
     * 账号余额
     */
    private Integer money;


}