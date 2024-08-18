package org.vivi.framework.iasync.sample.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iasyncexcel.core.importer.ImportDataParam;
import org.vivi.framework.iasyncexcel.starter.ExcelService;
import org.vivi.framework.iasync.sample.model.User;
import org.vivi.framework.iasync.sample.handler.ExcelUserHandler;


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
        Long taskId = excelService.doImport(ExcelUserHandler.class, dataImportParam);
        return taskId;
    }

}
