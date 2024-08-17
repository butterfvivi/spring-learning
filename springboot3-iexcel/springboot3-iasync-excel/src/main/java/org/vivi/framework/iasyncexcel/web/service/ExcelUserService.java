package org.vivi.framework.iasyncexcel.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.vivi.framework.iasyncexcel.core.annotation.ExcelHandle;
import org.vivi.framework.iasyncexcel.core.importer.ImportDataParam;
import org.vivi.framework.iasyncexcel.core.importer.ImportDto;
import org.vivi.framework.iasyncexcel.core.importer.ImportHandler;
import org.vivi.framework.iasyncexcel.web.mapper.UserMapper;
import org.vivi.framework.iasyncexcel.web.model.User;

import java.util.ArrayList;
import java.util.List;

@ExcelHandle
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
