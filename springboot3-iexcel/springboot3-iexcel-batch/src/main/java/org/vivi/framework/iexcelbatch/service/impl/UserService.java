package org.vivi.framework.iexcelbatch.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.iexcelbatch.entity.model.User;
import org.vivi.framework.iexcelbatch.entity.query.UserQuery;
import org.vivi.framework.iexcelbatch.entity.query.UserRequest;
import org.vivi.framework.iexcelbatch.mapper.UserMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    public void reqSaveBath(List<UserRequest> requests) {
        log.info("====获取到一批数据正在执行批量插入====");
        if (CollectionUtils.isEmpty(requests)) {
            return;
        }
        List<User> collect = requests.stream().map(request -> {
            User user = new User();
            user.setId(null); // id自增
            user.setName(request.getName());
            user.setSex(request.getSex());
            user.setAge(request.getAge());
            user.setBirthday(LocalDate.now());
            user.setCreateTime(LocalDateTime.now());
            user.setSalary(request.getSalary());
            return user;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
        log.info("获取到一批的数据批量插入完成:{}", requests);
    }

    public Page<User> page(UserQuery query) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        Page<User> page = userMapper.selectPage(new Page<>(query.getCurrent(), query.getSize()), userLambdaQueryWrapper);
//        Page<User> userVOPage = new Page<>();
//        userVOPage.setRecords(BeanUtil.copyToList(page.getRecords(), User.class));
//        userVOPage.setTotal(page.getTotal());
//        userVOPage.setSize(page.getSize());
//        userVOPage.setCurrent(page.getCurrent());
//        userVOPage.setPages(page.getPages());
        return page;
    }

    /**
     * 可以根据数据动态的去渲染表头
     *
     * @return
     */
    public List<List<String>> head(){
        List<List<String>> list = Lists.newArrayList();
        List<String> head0 = Lists.newArrayList();
        head0.add("主键id");
        List<String> head1 = Lists.newArrayList();
        head1.add("姓名");
        List<String> head2 = Lists.newArrayList();
        head2.add("性别");
        List<String> head3 = Lists.newArrayList();
        head3.add("年龄");
        List<String> head4 = Lists.newArrayList();
        head4.add("生日");
        List<String> head5 = Lists.newArrayList();
        head5.add("创建时间");
        List<String> head6 = Lists.newArrayList();
        head6.add("薪水");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        list.add(head3);
        list.add(head4);
        list.add(head5);
        list.add(head6);
        return list;
    }


    /**
     *  可以根据数据动态的去渲染数据
     *
     * @return
     */
    public List<List<Object>> dataUserList(UserQuery query) {
        List<User> records = this.page(query).getRecords();
        List<List<Object>> objectList = new ArrayList<>();
        for (User u : records) {
            List<Object> row = new ArrayList<>();
            row.add(toEmptyString(u.getId()));
            row.add(toEmptyString(u.getName()));
            row.add(toEmptyString(u.getSex()));
            row.add(toEmptyString(u.getAge()));
            row.add(toEmptyString(u.getBirthday()));
            row.add(toEmptyString(u.getCreateTime()));
            row.add(toEmptyString(u.getSalary()));
            objectList.add(row);
        }
        return objectList;
    }

    public  String toEmptyString(Object value) {
        return Objects.nonNull(value) ? value.toString() : "";
    }
}
