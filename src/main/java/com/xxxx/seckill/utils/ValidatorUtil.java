package com.xxxx.seckill.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Component
public class ValidatorUtil {
//    ^表示起始位置是1，结尾以9个0到9的数字，$去掉意思就不同了，去掉$的话结尾追加任意字符都可以成功匹配,(也是一个完整的正则表达式)，第二位必须是3-9
    private static final Pattern mobile_patten = Pattern.compile("[1]([3-9])[0-9]{9}$");
    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher matcher = mobile_patten.matcher(mobile);
        return matcher.matches();
    }
}
