package com.guide.pojo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class WeChatProperties {

    @Value("${wechat.appid}")
    private  String appid;

    @Value("${wechat.secret}")
    private String secret;

    @Value("${wechat.granttype}")
    private String grant_type;

    @Value("${wechat.url}")
    private String url;

}
