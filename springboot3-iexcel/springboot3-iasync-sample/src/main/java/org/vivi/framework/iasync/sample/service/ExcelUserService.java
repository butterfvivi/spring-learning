package org.vivi.framework.iasync.sample.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.iasync.sample.mapper.UserMapper;
import org.vivi.framework.iasync.sample.model.User;

import java.util.List;
import java.util.Map;

@Service
public class ExcelUserService extends ServiceImpl<UserMapper, User> {

    public int insertBatch(List<User> list){
        return this.insertBatch(list);
    }

    public List<Map<String, Object>> selectList(){
        return this.listMaps();
    }
}
