package org.vivi.framework.exceptionhandler.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.exceptionhandler.common.enums.ResponseCodeEnum;
import org.vivi.framework.exceptionhandler.common.response.CommonResponse;
import org.vivi.framework.exceptionhandler.model.UserVO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping
public class DemoController {

    private static final List<UserVO> LIST = new ArrayList<UserVO>() {
        {
            add(UserVO.builder()
                    .userId(1L).username("test01")
                    .email("test01@168.com")
                    .createTime(LocalDateTime.now())
                    .build());
            add(UserVO.builder()
                    .userId(2L).username("test02")
                    .email("test02@163.com")
                    .createTime(LocalDateTime.now())
                    .build());
            add(UserVO.builder()
                    .userId(3L).username("test03")
                    .email("test03@qq.com")
                    .createTime(LocalDateTime.now())
                    .build());
            add(UserVO.builder()
                    .userId(4L).username("test04")
                    .email("test04@163.com")
                    .createTime(LocalDateTime.now())
                    .build());
        }
    };

    @ApiOperationSupport(order = 1)
    @GetMapping("/{userId}")
    @ApiOperation("根据用户id获取用户")
    public CommonResponse<UserVO> get(@PathVariable("userId") Long userId) {
        for (UserVO userVO : LIST) {
            if (userId.equals(userVO.getUserId())) {
                return CommonResponse.success(userVO);
            }
        }

        return CommonResponse.failure(ResponseCodeEnum.USER_ID_NOT_EXIST);
    }

}
