package com.guide.common.utils;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.guide.conf.rabbitmq.MessageProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class MessageUtils {

    public void sendMessagesCode(String phone, String code, MessageProperties properties) {
        SmsSingleSender ssender = new SmsSingleSender(properties.getAppid(), properties.getAppkey());
        try {
            SmsSingleSenderResult result = ssender.sendWithParam(properties.getNationCode(), phone,
                    properties.getCodeTemplateId() , new ArrayList(Arrays.asList(code,properties.getEffectiveTime())), properties.getSmsSign(), "", "");
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
