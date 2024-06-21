package org.vivi.framework.easyexcelsimple.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.easyexcelsimple.common.utils.BeanTransformUtils;
import org.vivi.framework.easyexcelsimple.mapper.UserMapper;
import org.vivi.framework.easyexcelsimple.model.dto.UserDto;
import org.vivi.framework.easyexcelsimple.model.entity.User;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper,User>   {


    public void insertBatchOrder(List<UserDto> list) {
        List<User> orderList = BeanTransformUtils.transformList(list, User.class);
        this.saveBatch(orderList);
    }
}
