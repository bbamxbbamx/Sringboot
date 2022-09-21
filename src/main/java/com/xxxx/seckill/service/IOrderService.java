package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2022-09-11
 */
public interface IOrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goods);
}
