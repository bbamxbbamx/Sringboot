package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQsender;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2022-09-05
 */
@RestController
@RequestMapping("/test")
public class UserController {
    @Autowired
    private MQsender mQsender;
//    @GetMapping("/user")
//    public RespBean user(User user){
//       return RespBean.success(user);
//    }
//    @RequestMapping("/info")
//    @ResponseBody
//    public void mq(){
//        mQsender.send("hello");
//    }
//    @RequestMapping("/direct01")
//    @ResponseBody
//    public void mq_direct01(){
//        mQsender.send01("hello_red");
//    }
//    @RequestMapping("/direct02")
//    @ResponseBody
//    public void mq_direct02(){
//        mQsender.send02("hello_green");
//    }
//
//    @RequestMapping("/topic01")
//    @ResponseBody
//    public void mq_topic01(){
//        mQsender.send03("hello_*");
//    }//队列1
//    @RequestMapping("/topic02")
//    @ResponseBody
//    public void mq_topic02(){
//        mQsender.send04("hello_#");
//    }
}
