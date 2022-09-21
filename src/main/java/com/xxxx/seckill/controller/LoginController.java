package com.xxxx.seckill.controller;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.service.imp.UserServiceImpl;
import com.xxxx.seckill.utils.MD5Util;
import com.xxxx.seckill.utils.ValidatorUtil;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.xxxx.seckill.utils.ValidatorUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Slf4j
public class LoginController {
    @Autowired
    private IUserService userService;
    @PostMapping("/login")
    public RespBean test( @RequestBody  @Validated LoginVo loginVo,
                         HttpServletRequest request, HttpServletResponse response){
        return userService.doLogin(loginVo, request, response);
    }
}
