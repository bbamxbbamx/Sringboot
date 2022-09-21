package com.xxxx.seckill.service.imp;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.mapper.GoodsMapper;
import com.xxxx.seckill.mapper.OrderMapper;
import com.xxxx.seckill.mapper.SeckillOrderMapper;
import com.xxxx.seckill.pojo.*;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillGoodsService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2022-09-11
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Transactional
    @Override
    public Order seckill(User user, GoodsVo goods) {
        //获取库存,设置sql中新的秒杀商品库存，让sql和redis同步
        //seckillGoodsService.updateById(one);
        //原因在于stock_count-1和更新是分开的，没有保证原子性，可能第一个线程还没更新减去后的值为9，
        // 第二个线程又取到了sql未更新的10，就会多次覆盖为9；而订单只有10，因为在redis中减一和更新是原子性
        // 所以一定不会出现超过10个线程进入秒杀操作
        seckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count=" +
                "stock_count-1").eq("goods_id",goods.getId()));
        //生成订单
        Order order=new Order();
        order.setUserId(user.getId());////订单ID就是用户手机号码
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goods.getSeckilePrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder=new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());
        /**saveorupdate()如果传入的对象在数据库中有就做update操作，如果没有就做save操作。
         save()在数据库中生成一条记录，如果数据库中有，会报错说有重复的记录,还会返回插入记录的主键
         update()就是更新数据库中的记录。*/
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:"+user.getId()+":"+goods.getId(),seckillOrder);
        return order;
    }
}
