package com.guide.common.utils;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.guide.conf.rabbitmq.MailBoxProperties;
import com.guide.conf.rabbitmq.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class MessageUtils {
    @Autowired
    private MessageProperties messageProperties;
    @Autowired
    private MailBoxProperties mailBoxProperties;

    public void sendMessagesCode(String phone, String code) {
        SmsSingleSender ssender = new SmsSingleSender(messageProperties.getAppid(), messageProperties.getAppkey());
        try {
            SmsSingleSenderResult result = ssender.sendWithParam(messageProperties.getNationCode(), phone,
                    messageProperties.getCodeTemplateId() , new ArrayList(Arrays.asList(code,messageProperties.getEffectiveTime())), messageProperties.getSmsSign(), "", "");
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
