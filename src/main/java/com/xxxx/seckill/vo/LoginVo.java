package com.xxxx.seckill.vo;

import com.xxxx.seckill.validator.Ismobile;
import lombok.Data;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class LoginVo {
    @NotNull
    @Ismobile
    public String mobile;
    @NotNull
    @Length(min = 32)
    private String  password;
}
