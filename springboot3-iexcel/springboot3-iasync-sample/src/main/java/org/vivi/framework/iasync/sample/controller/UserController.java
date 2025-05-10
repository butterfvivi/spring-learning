package org.vivi.framework.iasync.sample.controller;

import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iasync.sample.handler.MapExportHandler;
import org.vivi.framework.iasync.sample.handler.UserExportHandler;
import org.vivi.framework.iasyncexcel.core.exporter.DataExportParam;
import org.vivi.framework.iasyncexcel.core.importer.ImportDataParam;
import org.vivi.framework.iasyncexcel.starter.ExcelService;
import org.vivi.framework.iasync.sample.model.User;
import org.vivi.framework.iasync.sample.handler.UserImportHandler;


@RestController
@RequestMapping("/async")
public class UserController {

    @Resource
    ExcelService excelService;

    @PostMapping("/import")
    public Long imports(@RequestParam("file") MultipartFile file) throws Exception{
        ImportDataParam dataImportParam = new ImportDataParam()
                .setStream(file.getInputStream())
                .setModel(User.class)
                //.setBatchSize(3)
                .setFilename("用户导入");
        return excelService.doImport(UserImportHandler.class, dataImportParam);
    }

    @PostMapping("export")
    public Long exports(){
        DataExportParam exportParam = new DataExportParam()
                .setExportFileName("用户导出")
                .setLimit(5);
        return excelService.doExport( UserExportHandler.class,exportParam);
    }

    @PostMapping("export1")
    public Long exports1(){
        DataExportParam exportParam = new DataExportParam()
                .setExportFileName("用户导出")
                .setDynamicHead(true)
                .setHeadList(Lists.newArrayList("姓名", "性别", "年龄", "生日", "薪水", "创建时间"));
        return excelService.doExport(exportParam, MapExportHandler.class);
    }

}
