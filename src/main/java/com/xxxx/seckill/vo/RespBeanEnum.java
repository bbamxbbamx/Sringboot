package com.xxxx.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public enum RespBeanEnum {
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务都异常"),
    Login_ERROR(600,"登录失败"),
    BIND_ERROR(601,"参数校验异常"),
    jiaoyan(602,"未登录"),
    STOCK(603,"秒杀内存不足"),
    CHONGFU(604,"重复抢购");
    private final  Integer code;
    private final  String message;
}

