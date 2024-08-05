package org.vivi.framework.iexceltoolkit.service;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.iexceltoolkit.common.utils.BeanUtil;
import org.vivi.framework.iexceltoolkit.mapper.UserMapper;
import org.vivi.framework.iexceltoolkit.model.User;
import org.vivi.framework.iexceltoolkit.web.request.UserRequest;
import org.vivi.framework.iexceltoolkit.toolkit.annotation.IExcelRewrite;

import java.util.List;
import java.util.Map;

@Slf4j
@IExcelRewrite(targetParam = "excel")
@Service(value = "excelHandleService")
public class ExcelHandleService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    /***
     * 规上企业excel导入
     * **/
    @IExcelRewrite(targetParam = "import", entityClass = UserRequest.class)
    public Object analysisImport(List<UserRequest> data) {
        return data;
    }

    /**
     *
     **/
    @IExcelRewrite(targetParam = "template")
    public void templateExport(List<Map<String, Object>> data, Map<String, Object> otherVal) throws Exception {
        List<User> users = userMapper.selectList(new QueryWrapper<>());
        List<Map<String, Object>> maps = BeanUtil.objectList2ListMap(users);

        data.addAll(maps);
    }

    /**
     * {
     *     "dataList":[
     *     {"id": null,"name": "张三14","sex": 2,"age": 22,"birthday": "2023-09-05","createTime": "2023-09-13T20:52:36","salary": 0.0300},
     *     {"id": null,"name": "张三15","sex": 2,"age": 23,"birthday": "2023-09-05","createTime": "2023-09-13T20:52:36","salary": 0.0300},
     *     {"id": null,"name": "张三16","sex": 0,"age": 24,"birthday": "2023-09-05","createTime": "2023-09-13T20:52:36","salary": 0.0300},
     *     {"id": null,"name": "张三17","sex": 0,"age": 25,"birthday": "2023-09-05","createTime": "2023-09-13T20:52:36","salary": 0.0300}
     * ],
     *     "headList":["id","姓名","性别","年龄","生日","创建时间","薪水"],
     *     "config":{
     *         "watermark":"vivi",
     *         "targetParam":"excel@dynamic"
     *     }
     * }
     * @param data
     * @param headers
     * @return
     */
    @IExcelRewrite(targetParam = "dynamic")
    public void dynamic(List<Map<String, Object>> data, List<String> headers) throws Exception {
        log.info("dynamic data list1:{}", JSONUtil.parse(data));
        List<User> users = userMapper.selectList(new QueryWrapper<>());
        List<Map<String, Object>> maps = BeanUtil.objectList2ListMap(users);

        data.addAll(maps);
        //return userMapper.selectList(new QueryWrapper<>());
    }
}
