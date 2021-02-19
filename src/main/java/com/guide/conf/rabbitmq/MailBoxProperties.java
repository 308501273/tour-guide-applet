package com.guide.conf.rabbitmq;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class MailBoxProperties {

    @Value("${tourguide.mailbox.authCode}")
    private String authCode;
    @Value("${tourguide.mailbox.emailSender}")
    private String emailSender;
    @Value("${tourguide.mailbox.effectiveTime}")
    private String effectiveTime;

}
