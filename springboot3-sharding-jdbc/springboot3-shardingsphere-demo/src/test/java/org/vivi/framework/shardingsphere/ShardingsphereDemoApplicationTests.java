package org.vivi.framework.shardingsphere;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.vivi.framework.shardingsphere.entity.Dict;
import org.vivi.framework.shardingsphere.entity.Order;
import org.vivi.framework.shardingsphere.entity.OrderItem;
import org.vivi.framework.shardingsphere.entity.User;
import org.vivi.framework.shardingsphere.mapper.DictMapper;
import org.vivi.framework.shardingsphere.mapper.OrderItemMapper;
import org.vivi.framework.shardingsphere.mapper.OrderMapper;
import org.vivi.framework.shardingsphere.mapper.UserMapper;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
class ShardingsphereDemoApplicationTests {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private DictMapper dictMapper;
    @Resource
    private UserMapper userMapper;


    /**
     * 测试插入单表数据
     */
    @Test
    void testhardingSphere1() {
        for (int i = 0; i < 20; i++) {
            User userBuild = User.builder()
                    .name("test" + i)
                    .age(18)
                    .job("student")
                    .city("beijing")
                    .description("good person" + i)
                    .build();
            userMapper.insert(userBuild);
        }

        List<User> userList = userMapper.selectList(Wrappers.emptyWrapper());
        System.out.println(userList);
    }

    @Test
    public void testSelectUserInfo(){
        List<User> userList = null;
        for (int i = 0; i < 5; i++) {
            userList = userMapper.selectList(Wrappers.emptyWrapper());
        }
        System.out.println(userList);
    }

    /**
     * 测试插入分表表数据
     */
    @Test
    void testhardingSphere2() {
        for (int i = 0; i < 20; i++) {
            Order orderBuild = Order.builder()
                    .orderNo("order" + i)
                    .userId(i)
                    .amount(BigDecimal.TEN)
                    .build();
            orderMapper.insert(orderBuild);

            for (int j = 0; j < 2; j++) {
                OrderItem orderItemBuild = OrderItem.builder()
                        .orderNo("order" + i)
                        .userId((long)j)
                        .price(BigDecimal.TEN)
                        .count(2)
                        .build();
                orderItemMapper.insert(orderItemBuild);
            }
        }

        //查询单表
        List<Order> orders = null;
        for (int i = 0; i < 5; i++) {
            orders = orderMapper.selectList(Wrappers.emptyWrapper());
        }

        //联表查询
        List<Order> orderList = orderMapper.getOrderList();
        System.out.println(orderList);
    }

    @Test
    public void testOrderInfo(){
        //查询单表
        List<Order> orders = null;
        for (int i = 0; i < 5; i++) {
            orders = orderMapper.selectList(Wrappers.emptyWrapper());
        }
        System.out.println( orders);

        //联表查询
        List<Order> orderList = orderMapper.getOrderList();
        System.out.println(orderList);
    }

    /**
     * 广播表，就是每个库里都有一份，不一定去哪个库里查找
     */
    public void testhardingSphere3() {
        Dict dict = new Dict();
        dict.setDictType("test1");
        dictMapper.insert(dict);

        List<Dict> dicts = null;
        for (int i = 0; i < 20; i++) {
            dicts = dictMapper.selectList(Wrappers.emptyWrapper());
        }
        dicts.forEach(System.out::println);
    }
}
