package com.guide.rabbitmq;

import com.guide.common.utils.MailBoxUtils;
import com.guide.common.utils.MessageUtils;
import com.guide.conf.rabbitmq.MailBoxProperties;
import com.guide.conf.rabbitmq.MessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Slf4j
@Component
public class Listener {
    @Autowired
    private MessageUtils messageUtils;
    @Autowired
    private MailBoxUtils mailBoxUtils;
    @Autowired
    private MessageProperties messageProperties;
    @Autowired
    private MailBoxProperties mailBoxProperties;

    /**
     * 发送短信验证码
     * @param msg
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "tourguide.sms.code",durable = "true"),
            exchange = @Exchange(name = "tourguide.sms.exchange", type = ExchangeTypes.TOPIC),
            key = {"tourguide.sms.code"}
    ))
    public void listenToSendMessageCode(Map<String,String> msg){

        if(CollectionUtils.isEmpty(msg)){
            return;
        }
        String phone = msg.remove("phone");
        String code=msg.remove("code");
        if(StringUtils.isBlank(code)|| StringUtils.isBlank(phone)){
            return;
        }
        messageUtils.sendMessagesCode(phone,code,messageProperties);
    }
}
