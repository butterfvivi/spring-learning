package org.vivi.framework.iasyncexcel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.vivi.framework.iasyncexcel.excel.importer.ImportDataParam;
import org.vivi.framework.iasyncexcel.excel.importer.ImportDto;
import org.vivi.framework.iasyncexcel.excel.importer.ImportHandler;
import org.vivi.framework.iasyncexcel.mapper.UserMapper;
import org.vivi.framework.iasyncexcel.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelUserService implements ImportHandler<User> {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<ImportDto> importData(List<User> list, ImportDataParam param) {
        List<ImportDto> errorList=new ArrayList<>();
        List<User> saveUsers=new ArrayList<>();
        userMapper.insertBatch(saveUsers);
        return errorList;
    }
}
