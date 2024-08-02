package org.vivi.framework.satokensimple.controller;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson2.JSON;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.satokensimple.controller.dto.LoginOrRegisterDto;
import org.vivi.framework.satokensimple.model.User;
import org.vivi.framework.satokensimple.service.UserService;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate  redisTemplate;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public SaResult login(@RequestBody LoginOrRegisterDto loginDto){
        User user = userService.getUserInfo(loginDto);
        if (user != null && BCrypt.checkpw(loginDto.getPassword(),user.getPassword())){
            StpUtil.login(user.getId());
            String tokenValue = StpUtil.getTokenValue();
            redisTemplate.opsForValue().set(user.getId(), JSON.toJSONString(user),1, TimeUnit.DAYS);
            //StpUtil.getTokenInfo();
            return SaResult.ok().setData(tokenValue);
        }

        return SaResult.error("login failed").setCode(40010);
    }

    @Operation(summary = "注册")
    @PostMapping("/register")
    public SaResult register(@RequestBody @Validated LoginOrRegisterDto registerDto){
        User user = userService.getUserInfo(registerDto);
        if (user != null){
            return SaResult.error("user is already exists");
        }

        user = new User();

        BeanUtils.copyProperties(user, user);
        user.setPassword(BCrypt.hashpw(registerDto.getPassword(),BCrypt.gensalt()));

        userService.save(user);
        return SaResult.ok();
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public SaResult logout(){
        StpUtil.logout();
        return SaResult.ok();
    }

    @Operation(summary = "获取用户信息")
    @PostMapping("/userInfo")
    public SaResult userInfo(){
        String userinfo = redisTemplate.opsForValue().get(StpUtil.getLoginIdAsString());
        User user = JSON.parseObject(userinfo, User.class);
        return SaResult.ok().setData(user);
    }
}
