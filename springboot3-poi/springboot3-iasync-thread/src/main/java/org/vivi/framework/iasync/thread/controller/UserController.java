package org.vivi.framework.iasync.thread.controller;

import com.alibaba.fastjson2.JSONObject;
import org.vivi.framework.iasync.thread.dto.UserExportTaskDto;
import org.vivi.framework.iasync.thread.service.UserSerivce;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LogManager.getLogger(UserController.class);
    
    @Autowired
    private UserSerivce userSerivce;

    
    @GetMapping("/exportList")
    public JSONObject exportList(UserExportTaskDto userExportTaskDto) {
        JSONObject jsonObject = new JSONObject();
        // TODO 系统的获取用户信息
        // String userName = user.getUserName;
        for (int i = 0; i < 3; i++) {
            // 不使用注解的方式
//            this.userSerivce.exportList(userExportTaskDto);
            // 使用注解的方式
            this.userSerivce.exportListByAnnotation(userExportTaskDto);
        }
        return jsonObject;
    }
}

