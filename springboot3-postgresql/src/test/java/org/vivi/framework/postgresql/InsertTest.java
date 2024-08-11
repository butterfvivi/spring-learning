package org.vivi.framework.postgresql;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.vivi.framework.postgresql.model.User;
import org.vivi.framework.postgresql.repository.UserMapper;

@SpringBootTest
public class InsertTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void insertTest() throws Exception {
        userMapper.insert(new User(1L, "test1","123@test.com", 8));
        userMapper.insert(new User(2L, "黄忠","123@test.com", 34));
        userMapper.insert(new User(3L, "张飞","123@test.com", 27));
        userMapper.insert(new User(4L, "貂蝉","123@test.com", 31));
        userMapper.insert(new User(5L, "关羽","123@test.com", 66));
    }

    //根据指定id进行数据查询
    @Test
    public void deleteByIdTest() {
        Assertions.assertEquals(1, userMapper.deleteById(1));
    }


}
