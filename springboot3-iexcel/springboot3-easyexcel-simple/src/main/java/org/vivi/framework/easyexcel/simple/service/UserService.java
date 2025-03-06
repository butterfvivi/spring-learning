package org.vivi.framework.easyexcel.simple.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.easyexcel.simple.common.utils.BeanTransformUtils;
import org.vivi.framework.easyexcel.simple.mapper.UserMapper;
import org.vivi.framework.easyexcel.simple.model.dto.UserDto;
import org.vivi.framework.easyexcel.simple.model.entity.User;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper,User>   {


    public void insertBatchOrder(List<UserDto> list) {
        List<User> orderList = BeanTransformUtils.transformList(list, User.class);
        this.saveBatch(orderList);
    }

    public List<UserDto> getAllUser() {
        List<User> list = this.list();
        return BeanTransformUtils.transformList(list, UserDto.class);
    }
}
