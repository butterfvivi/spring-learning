package org.vivi.framework.ureport.store.controller;




import cn.dev33.satoken.stp.StpUtil;
import org.vivi.framework.ureport.store.domain.User;
import org.vivi.framework.ureport.store.domain.bo.UserBo;
import org.vivi.framework.ureport.store.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private IUserService iUserService;

    @GetMapping("/doLogin")
    public String login(UserBo bo){

        User user = iUserService.selectUserByUserName(bo);

        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

        if(user!=null) {

            StpUtil.login(user.getUserName());
            return "登录成功";
        }else{
            return "用户名或密码不正确！";
        }
    }

    @GetMapping("/doLogout")
    public String logout(){
        StpUtil.logout();
        if(StpUtil.isLogin())
        {
            return "登出成功";
        }else{
            return "登出失败";
        }
    }

}
