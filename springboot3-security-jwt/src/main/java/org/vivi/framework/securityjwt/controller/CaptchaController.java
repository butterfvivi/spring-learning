package org.vivi.framework.securityjwt.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.securityjwt.common.response.R;
import org.vivi.framework.securityjwt.model.req.VoCode;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/getCaptcha")
@CrossOrigin
public class CaptchaController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

@GetMapping
public R getCaptcha(){
    System.out.println("----------------------------------");

//    生成验证码，并放入redis
    CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(150, 50, 4, 2);
    String codeValue = circleCaptcha.getCode();
    String imageBase64 = circleCaptcha.getImageBase64();

    String codeKey = UUID.randomUUID().toString();
    redisTemplate.opsForValue().set(codeKey,codeValue,5, TimeUnit.MINUTES);
//    "data:images/png;base64,"+imageBase64    直接显示
    VoCode voCode=new VoCode(codeKey,"data:images/png;base64,"+imageBase64);
    return R.ok(voCode);


}



}
