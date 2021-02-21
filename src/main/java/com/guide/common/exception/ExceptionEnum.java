package com.guide.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author fengenchun
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ExceptionEnum {
    NO_AUTHORIZED(403,"您没有权限访问！"),
    SERVER_ERROR(500,"服务器错误!"),
    CLIENT_CALL_FAIL(500,"调用服务失败"),
    EXCEED_THE_UPPER_LIMIT(500,"超出数量上限"),
    ORDER_UPDATE_STATUS_FAIL(500,"更新订单状态失败"),
    ORDER_NOT_FOUND(404,"并没有找到该订单"),
    ORDER_CREATE_FAIL(500,"订单创建失败"),

    UPLOAD_FILE_ERROR(500,"上传文件失败！"),
    INVALID_FILE_TYPE(400,"不支持的文件类型"),
    UPDATE_FAIL(500,"更新失败"),
    VERIFICATION_CODE_ERROR(403,"验证码错误！"),
    INVALID_REQUEST_PARAM(400,"参数错误"),
    PARSING_FAIL(400,"用户数据解析错误"),
    NO_PRIVILEGES(401,"没有权限"),
    WECHATLOGINTIMEOUT(403,"登录超时"),
    WECHATLOGINFAIL(403,"登录失败"),
    WECHATLOGINFREQUENT(429,"请求频繁，请稍后重试"),
    GUIDER_NOT_FOUND(404,"未查询到导游"),
    GUIDER_INCOMPLETE_CERTIFICATES(400,"身份证件照片不全(正面，反面，手持身份证）");
    ;
    private Integer code;
    private String msg;
}
