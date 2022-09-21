package com.xxxx.seckill.utils;

import java.util.UUID;

public class UUIDUtil {
    public static String uuid() {
        //JDK自带的生成唯一id序列550e8400-e29b-41d4-a716-446655440000，replace会删除-
        return UUID.randomUUID().toString().replace("-", "");
    }
}
