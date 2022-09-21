package com.xxxx.seckill.controller;

import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IGoodsService goodsService;
    @PostMapping("/toList")
    public Map<String,Object> toList(User user){
//        if(StringUtils.isEmpty(ticket)){
//            return "登录";
//        }
////        User user=(User) session.getAttribute(ticket);
//        User user = userService.getUserByCookie(ticket, request, response);
//        if(null==user){
//            return "登录";
//        }
        if(user!=null){
            List<GoodsVo> list= goodsService.findGoodsVo();
            Map<String,Object> map=new HashMap<>();
            map.put("user",user);
            map.put("商品",list);
            return map;
        }throw new GlobalException(RespBeanEnum.jiaoyan);
    }
    @PostMapping("/goodsDetail")
    public Map<String,Object> goodsDetail(User user,@RequestParam long goodsId){
        if(user==null){
            throw new GlobalException(RespBeanEnum.jiaoyan);
        }
        GoodsVo detail = goodsService.findGoodsDetail(goodsId);
        Date startDate = detail.getStartDate();
        Date endDate = detail.getEndDate();
        Date date=new Date();
        int seckillcount = 0;
        int secKillstatu=0;
        if(date.before(startDate)){
            seckillcount=(int)(startDate.getTime()-date.getTime())/1000;
        }
        else if(date.after(endDate)){
            seckillcount=-1;
            secKillstatu=0;

        }
        else{
            seckillcount=0;
            secKillstatu=1;
        }
        Map<String,Object> map=new HashMap<>();
        map.put("详情",detail);
        map.put("秒杀状态",secKillstatu);
        map.put("秒杀剩余时间",seckillcount);
        return map;
    }
}
