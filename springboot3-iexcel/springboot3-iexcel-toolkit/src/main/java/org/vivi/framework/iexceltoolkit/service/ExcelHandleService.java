package org.vivi.framework.iexceltoolkit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.iexceltoolkit.mapper.UserMapper;
import org.vivi.framework.iexceltoolkit.model.User;
import org.vivi.framework.iexceltoolkit.web.request.UserRequest;
import org.vivi.framework.iexceltoolkit.toolkit.annotation.IExcelRewrite;

import java.util.List;
import java.util.Map;

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

    /***
     * 规上企业excel导入
     * **/
    @IExcelRewrite(targetParam = "template")
    public Object templateExport(List<Map<String, Object>> data, Map<String, Object> otherVal) {
        return userMapper.selectList(new QueryWrapper<>());
    }

    @IExcelRewrite(targetParam = "dynamic")
    public Object dynamic(List<Map<String, Object>> data, List<String> headers) {
        return userMapper.selectList(new QueryWrapper<>());
    }
}
