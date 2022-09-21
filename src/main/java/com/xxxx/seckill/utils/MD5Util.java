package com.xxxx.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    private static final String salt="1a2b3c4d";
//    真正的加密处理,调用这个函数将明文加盐后的值进行加密
    public static  String md5(String src){
        return DigestUtils.md5Hex(src);
    }
//    对前端传进后端的数据进行明文加盐
    public static String inputPassToFromPass(String inputPass){
        String str =salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }
    //    前端盐和后端盐不同,更安全
    public  static  String formPassToDBPass(String formPass,String salt){
        String str =salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(3)+salt.charAt(4);
        return  md5(str);
    }
    public static String inputPassToDBPass(String inputPass, String salt){
        String formPass= inputPassToFromPass(inputPass);
        String dbPass=  formPassToDBPass(formPass,salt);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFromPass("123456"));
        System.out.println(formPassToDBPass("ce21b747de5af71ab5c2e20ff0a60eea","1a2b3c4d"));
            System.out.println(inputPassToDBPass("123456","1a2b3c"));
    }
}
