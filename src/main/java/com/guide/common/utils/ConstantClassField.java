package com.guide.common.utils;

import java.util.concurrent.TimeUnit;

public class ConstantClassField {
    public static final Double FEE_PROFIT=0.95;
    public static final long EXPIRATION_DATE=15;
    public static final TimeUnit EXPIRATION_TIME_UNIT=TimeUnit.MINUTES;
    public static final Integer WECHATLOGINFAIL_CODE = 40029;//登录失败
    public static final Integer WECHATLOGINSUCCESS_CODE = 0;//登录成功
    public static final Integer WECHATLOGINTIMEOUT_CODE = -1;//登录超时
    public static final Integer WECHATLOGINFREQUENT_CODE = 45011;//获取频繁
    public static final Integer DEFAULT_CREDIT = 100;//获取频繁
    public static final Integer UPDATE_SUCCESSFLAG = 1;//更新成功
    public static final Integer UPDATE_FAILFLAG = 0;//更新失败
    public static final Integer UNAUTHORIZED = 401;//无权操作
    public static final Integer RESOURCES_CANNOT_FIND = 404;//找不到资源
    public static final String GET_AVATARURL_ERROR="error";
    public static final String KEY_PREFIX="o2o-order:";
    public static final String CREAT_ORDER_FAILED="您有一笔待支付的订单";
    public static final Integer PAYJS_CHECK_REQUEST_SUCCESS=1;
    public static final Integer PAYJS_PAY_SUCCESS=1;                    //支付成功的
    public static final Integer PAYJS_REFUND_SUCCESS=1;
    public static final Integer PAYJS_REFUND_STATUS=2;              //退款的
    public static final Integer ORDER_ACCEPTED=3;                           //接单的
    public static final Integer ORDER_FINISHED=4;                              //已经完成的订单
    public static final Integer ORDER_WAIT_REFUND=5;                    //等待退款
    public static final Integer FINISHED_ORDER_WAIT_REFUND=6;                    //已经完成的订单退款
    public static final Integer ORDER_WAIT_EVALUATE=0;              //未评价
    public static final Integer ORDER_FINASH_EVALUATE=1;         //评价完成
    public static final Integer SATISFACTION_DEGREE_FULL=100;
    public static final Integer REFUND_FEE=0;
    public static final Integer WAIT_TO_PAY=0;
    public static final Double DEFAULT_LONGITUDE= 116.40;
    public static final Double DEFAULT_LATITUDE=39.90;
    public static final String DEFAULT_ADDRESS = "北京市";
    public static final Integer GUIDER_STATUS_NORMAL=1;              //导游账号正常状态
    public static final Integer GUIDER_STATUS_WAITING_AUDIT=0;      //导游认证等待审核状态
    public static final Integer  GUIDER_MALE=1;
    public static final Integer  GUIDER_FEMALE=0;
    public static final Integer LEVEL_APPLY_WAITING=0;
    public static final Integer LEVEL_APPLY_APPROVED=1;
    public static final Integer LEVEL_APPLY_REFUSED=2;
    public static final Integer LEVEL_NOT_ENABLED=0;
    public static final Integer LEVEL_NORMAL=1;
    public static final String USER_UPDATE_PHONE_HEAD = "USER_UPDATE_PHONE";
    public static final String GUIDER_UPDATE_PHONE_HEAD = "GUIDER_UPDATE_PHONE";
}