package com.xxxx.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.ObjectName;
@Service
@Slf4j
public class MQsender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
//    public void send(Object msg){
//        log.info("发送消息"+msg);
//        rabbitTemplate.convertAndSend("fanoutExchange","",msg);
//    }
//    public void send01(Object msg){
//        log.info("发送消息"+msg);
//        rabbitTemplate.convertAndSend("direct_exchange","rout_red",msg);
//    }
//    public void  send02(Object msg){
//        log.info("发送消息"+msg);
//        rabbitTemplate.convertAndSend("direct_exchange","rout_green",msg);
//    }
//    /**
//      ROUTING1 = "#.queue1.#";//*代表只能匹配一个，#代表能匹配0个或多个
//      ROUTING2 = "*.queue2.#";
//     */
//    public void send03(Object msg){
//        log.info("发送消息"+msg);
//        rabbitTemplate.convertAndSend("topic_exchange","queue.red",msg);
//    }
//    public void send04(Object msg){
//        log.info("发送消息"+msg);
//        rabbitTemplate.convertAndSend("topic_exchange","message.queue.Green",msg);
//    }
    public void sendSeckill(Object msgSeckill){

        rabbitTemplate.convertAndSend("exchangeSeckill","queue.seckill",msgSeckill);
    }
}
