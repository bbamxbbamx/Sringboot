package com.xxxx.seckill.vo;

import com.xxxx.seckill.utils.ValidatorUtil;
import com.xxxx.seckill.validator.Ismobile;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class isMobileValidator implements ConstraintValidator<Ismobile,String> {
    private  boolean required=false;
    @Override
    public void initialize(Ismobile constraintAnnotation) {
        required =constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String Value, ConstraintValidatorContext Context) {
        if(required){
            return ValidatorUtil.isMobile(Value);
        }else {
            if(StringUtils.isEmpty(Value)){
                return true;
            }
            else {
                return ValidatorUtil.isMobile(Value);
            }
        }
    }
}
