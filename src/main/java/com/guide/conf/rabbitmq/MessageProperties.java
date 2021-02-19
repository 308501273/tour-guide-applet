package com.guide.conf.rabbitmq;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MessageProperties {

    @Value("${tourguide.sms.appid}")
    private int appid;
    @Value("${tourguide.sms.appkey}")
    private String appkey;
    @Value("${tourguide.sms.smsSign}")
    private String smsSign;
    @Value("${tourguide.sms.nationCode}")
    private String nationCode;
    @Value("${tourguide.sms.codeTemplateId}")
    private int codeTemplateId;
    @Value("${tourguide.sms.effectiveTime}")
    private String effectiveTime;

}
