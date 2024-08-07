package org.vivi.framework.securityjwt.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.securityjwt.common.response.R;
import org.vivi.framework.securityjwt.jwt.util.JwtTokenProvider;
import org.vivi.framework.securityjwt.model.entity.AccountUser;
import org.vivi.framework.securityjwt.model.entity.User;
import org.vivi.framework.securityjwt.model.req.UserLoginRequest;
import org.vivi.framework.securityjwt.service.UserService;

@RestController
@RequestMapping( "/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private JwtTokenProvider jwtUtil;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @RequestMapping("/login")
    public R login(@RequestBody @Validated UserLoginRequest userLoginRequest, HttpServletResponse response) {
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();

        User user = userService.getUserInfo(username);
        if (ObjectUtil.isNull(user)){
            return R.failed("用户名不存在");
        }

        if (!user.getPassword().equals(password)){
            return R.failed("密码错误");
        }

        String token = jwtUtil.generateToken(username);
        response.setHeader("Access-control-Expost-Headers", JwtTokenProvider.HEADER);
        response.setHeader(JwtTokenProvider.HEADER,token);

        return R.ok(token);
    }

    @GetMapping("/logout")
    public R logout(HttpServletRequest request,HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }
        return R.ok();
    }

    @GetMapping("/info")
    public R info(@RequestHeader("token")String token){

        Integer id = jwtUtil.getUsernameFromToken(token);
        String redisUser = redisTemplate.opsForValue().get(String.valueOf(id));
        AccountUser accountUser = JSON.parseObject(redisUser, AccountUser.class);
        return R.ok(accountUser);

    }

}
