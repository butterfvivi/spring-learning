package org.vivi.framework.dynamic.sqlbatis3;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.vivi.framework.dynamic.sqlbatis3.mapper.UserMapper;
import org.vivi.framework.dynamic.sqlbatis3.model.UserBaseDO;
import org.vivi.framework.dynamic.sqlbatis3.utils.JsonUtil;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Slf4j
public class MybatisSqlExtendTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void  sqlExtendTest(){
        System.err.println("XXXXXX");
        List<UserBaseDO> userBaseDOList = userMapper.findList("test", LocalDateTime.now().plusHours(1L));
        userBaseDOList.forEach(userBaseDO -> {
            log.info("\n {}", JsonUtil.toJson(userBaseDO));
        });
        userBaseDOList = userMapper.findList("test", LocalDateTime.now().plusHours(1L));
        userBaseDOList.forEach(userBaseDO -> {
            log.info("\n {}", JsonUtil.toJson(userBaseDO));
        });
        userBaseDOList = userMapper.findList("test", LocalDateTime.now().plusHours(1L));
        userBaseDOList.forEach(userBaseDO -> {
            log.info("\n {}", JsonUtil.toJson(userBaseDO));
        });
    }

    @Test
    public void  sqlExtendTestOrg(){
        List<UserBaseDO> userBaseDOList = userMapper.findListOrg("test", LocalDateTime.now().plusHours(1L));
        userBaseDOList.forEach(userBaseDO -> {
            log.info("\n {}", JsonUtil.toJson(userBaseDO));
        });
    }

    @Test
    public void  insert(){
        UserBaseDO userBaseDO = new UserBaseDO();
        userBaseDO.setUserName("2333");
        userMapper.insert(userBaseDO);
        System.out.println(userBaseDO.getId());
        //System.out.println(result);
    }
}
