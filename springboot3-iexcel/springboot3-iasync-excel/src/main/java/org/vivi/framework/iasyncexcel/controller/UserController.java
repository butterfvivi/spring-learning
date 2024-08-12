package org.vivi.framework.iasyncexcel.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iasyncexcel.excel.importer.ImportDataParam;
import org.vivi.framework.iasyncexcel.model.User;
import org.vivi.framework.iasyncexcel.service.ExcelService;
import org.vivi.framework.iasyncexcel.service.ExcelUserService;

@RestController
@RequestMapping("/async")
public class UserController {

    @Resource
    ExcelService excelService;

    @PostMapping("/import")
    public Long imports(@RequestBody MultipartFile file) throws Exception{
        ImportDataParam dataImportParam = new ImportDataParam()
                .setStream(file.getInputStream())
                .setModel(User.class)
                //.setBatchSize(3)
                .setFilename("用户导入");
        Long taskId = excelService.doImport(ExcelUserService.class, dataImportParam);
        return taskId;
    }

}
