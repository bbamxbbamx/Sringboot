package com.xxxx.seckill.rabbitmq;

import com.alibaba.fastjson.JSONObject;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import com.xxxx.seckill.vo.SeckillQueueMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQreceiver {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService iOrderService;
    @RabbitListener(queues = "queueSeckill")
    public void receive(JSONObject msg){
        SeckillQueueMessage seckillQueueMessage = JSONObject.toJavaObject(msg, SeckillQueueMessage.class);
        User user=seckillQueueMessage.getUser();
        Long goodsId=seckillQueueMessage.getGoodsId();
        if(user == null){
            return;
        }
        GoodsVo goodsDetail = goodsService.findGoodsDetail(goodsId);
        if(goodsDetail.getStockCount()<1){
            return;
        }
        SeckillOrder seckillOrder =(SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){

            return;
        }
        iOrderService.seckill(user,goodsDetail);
    }


//    @RabbitListener(queues = "queue")
//    public void receive(Object msg)
//    {
//        log.info("接收消息"+msg);
//    }
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg)
//    {
//        log.info("接收消息"+msg);
//    }
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg)
//    {
//        log.info("接收消息"+msg);
//    }
//    @RabbitListener(queues = "queue_Direct01")
//    public void receive03(Object msg){
//        log.info("接收消息"+msg);
//    }
//    @RabbitListener(queues = "queue_Direct02")
//    public void receive04(Object msg){
//        log.info("接收消息"+msg);
//    }
//    @RabbitListener(queues = "queue_topic1")
//    public void receive05(Object msg){
//        log.info("接收消息"+msg);
//    }
//    @RabbitListener(queues = "queue_topic2")
//    public void receive06(Object msg){
//        log.info("接收消息"+msg);
//    }
}
