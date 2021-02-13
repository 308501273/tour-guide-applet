package com.guide.pojo;

import lombok.Data;

@Data
public class WeChatResultType {
    private String openid;
    private String session_key;
    private String unionid;
    private Integer errcode;
    private String errmsg;
}
