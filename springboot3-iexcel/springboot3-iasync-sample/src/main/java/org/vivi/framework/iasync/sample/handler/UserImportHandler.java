package org.vivi.framework.iasync.sample.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.vivi.framework.iasync.sample.model.User;
import org.vivi.framework.iasync.sample.service.ExcelUserService;
import org.vivi.framework.iasyncexcel.core.annotation.ExcelHandle;
import org.vivi.framework.iasyncexcel.core.importer.ImportContext;
import org.vivi.framework.iasyncexcel.core.importer.ImportDataParam;
import org.vivi.framework.iasyncexcel.core.importer.ImportDto;
import org.vivi.framework.iasyncexcel.core.handler.ImportHandler;
import org.vivi.framework.iasyncexcel.core.model.DataParam;
import org.vivi.framework.iasyncexcel.core.model.ExcelContext;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@ExcelHandle
public class UserImportHandler implements ImportHandler<User> {

    @Autowired
    private ExcelUserService excelUserService;

    @Override
    public void init(ExcelContext ctx, DataParam param) {
        ReadSheet readSheet= EasyExcel.readSheet().sheetNo(0).headRowNumber(2).build();
        ImportContext impCtx=(ImportContext) ctx;
        impCtx.setReadSheet(readSheet);
    }

    @Override
    public List<ImportDto> importData(List<User> list, ImportDataParam param) {
        List<ImportDto> errorList=new ArrayList<>();
        log.info(" import data :{}", JSON.toJSONString(list));
        //excelUserService.insertBatch(list);
        return errorList;
    }
}
