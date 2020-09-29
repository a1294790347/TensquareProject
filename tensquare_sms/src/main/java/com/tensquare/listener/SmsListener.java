package com.tensquare.listener;

import com.tensquare.util.SendSms;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {
    /**
     * 发送短信
     * */
    @RabbitHandler
    public void executeSms(Map<String, String> map){
        String telphone = map.get("mobile");
        String code = map.get("checkcode");
        SendSms.SendCode(telphone,code);
    }
}
