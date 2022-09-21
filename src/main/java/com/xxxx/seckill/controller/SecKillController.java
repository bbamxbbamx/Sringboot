package com.xxxx.seckill.controller;

import com.alibaba.fastjson.JSONObject;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQsender;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBeanEnum;
import com.xxxx.seckill.vo.SeckillQueueMessage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * QPS:231
 * Qps:1906/解决超卖后
 * 超卖问题：解决在与判断是否有库存，而不是给生成订单，减少sql数据库中的库存进行加锁操作
 * */
@RestController
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {
    private static Boolean stock_falg;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisScript<Long> script;
    @Autowired
    private MQsender mQsender;
    //方法一
    //通过将库存信息存进redis中来保证原子性，判断redis的库存足够才进行秒杀，因为redis的减一操作是原子性，必定后一个线程取到的是前一个线程减后的库存
    //所以才能防止其他线程进行秒杀
    @GetMapping("/doSeckill")
    @Transactional
    public String  doSeckill (User user, @RequestParam long goodsId){
        //开始之前，将sql中的库存信息都存进了redis中，key：SeckillGoods+商品ID
        if(user==null){
           throw new GlobalException(RespBeanEnum.jiaoyan);
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder =(SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            return "Do not re-order";
        }
        //判断库存
        Long StockCount = redisTemplate.opsForValue().decrement("SeckillGoods" + goodsId);//预减一个库存
        if(StockCount<0){
            redisTemplate.opsForValue().increment("SeckillGoods" + goodsId);
            return "seckill fail";
        }
        //秒杀成功，生成订单
        GoodsVo goods = goodsService.findGoodsDetail(goodsId);
        return orderService.seckill(user,goods).toString();
       /* GoodsVo goods = goodsService.findGoodsDetail(goodsId);
        if(goods.getStockCount()<=0){
            return "Insufficient inventory";
        }
        SeckillOrder seckillOrder =(SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goods.getId());
        try {
            if(seckillOrder!=null){
                return "Do not re-order";
            }
        }catch (Exception e)
        {
            return e.toString();
        }
        return orderService.seckill(user,goods).toString();*/

    }


    //方法二，悲观锁,每次减之前都上锁，防止删除失败（try+过期时间），防止误删锁（随机生成value），解决误删锁不是原子操作(事务)
    @GetMapping("/doSeckillw")
    @Transactional
    public String  doSeckillw (User user, @RequestParam long goodsId){
        //开始之前，将sql中的库存信息都存进了redis中，key：SeckillGoods+商品ID
        if(user==null){
            throw new GlobalException(RespBeanEnum.jiaoyan);
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder =(SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if(seckillOrder!=null){
            return "Do not re-order";
        }
        if(stock_falg){
            return "fail";
        }
        //判断库存
        String value= UUID.randomUUID().toString();
        try {//redis预减
            while (true){
                Boolean lock = redisTemplate.opsForValue().setIfAbsent("k1", value,120, TimeUnit.SECONDS);
                if(lock){
                    Long stock = redisTemplate.opsForValue().decrement("SeckillGoods" + goodsId);
                    if(stock<0){
                        stock_falg=true;
                        redisTemplate.opsForValue().increment("SeckillGoods" + goodsId);
                        return "fail";
                    }
                    else {
                        break;
                    }
                }
            }
        }finally {
//            while (true){
//                redisTemplate.watch("k1");//就是因为判断和事务直接不是原子性的，所以才会需要监听，一旦执行事务之前，key被动
//                //就不去执行事务
//                if(value.equals(redisTemplate.opsForValue().get("k1").toString())){
//                    redisTemplate.setEnableTransactionSupport(true);
//                    redisTemplate.multi();
//                    redisTemplate.delete("k1");
//                    List<Object> exec = redisTemplate.exec();
//                    System.out.println(exec);
//                    if(exec==null) {
//                        continue;
//                    }
//                }
//                redisTemplate.unwatch();
//                break;
//            }
            Object result=redisTemplate.execute(script,Collections.singletonList("k1"),value);
            System.out.println(result.toString());
            if("1".equals(result.toString())){
                System.out.println("success");
            }else {
                System.out.println("fail");
            }
        }
        //秒杀成功，生成订单

//        GoodsVo goods = goodsService.findGoodsDetail(goodsId);
//        return orderService.seckill(user,goods).toString();
        SeckillQueueMessage message =new SeckillQueueMessage(user,goodsId);
        JSONObject Messagejson = (JSONObject) JSONObject.toJSON(message);
        mQsender.sendSeckill(Messagejson);
        return "排队中";
    }

   //专用清理Redis数据
    @GetMapping("/deRedis")
    public void  doSeckill2 (){
        for (int i=0;i<1000;i++){
            Long value=13000000000L+i;
            redisTemplate.delete("order:"+value+":1");
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if(list.isEmpty()){
            return;
        }//每次循环都将list数组里的数存在goodsvo中，创建了两个商品的秒杀库存数
        list.forEach(goodsVo ->
                    redisTemplate.opsForValue().set("SeckillGoods"+goodsVo.getId(),goodsVo.getStockCount())
        );
        stock_falg=false;
    }
}
