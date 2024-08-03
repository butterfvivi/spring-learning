package org.vivi.framework.iexceltoolkit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexceltoolkit.common.utils.AssertUtil;
import org.vivi.framework.iexceltoolkit.entity.model.ImportDto;
import org.vivi.framework.iexceltoolkit.toolkit.core.ExcelInvoke;

@Slf4j
@RestController
@RequestMapping
public class ExcelImportController {


    @PostMapping("/excel/import")
    public Object readExcel(@RequestParam("file") MultipartFile file,
                            @RequestParam("targetParam") String targetParam,
                            @RequestParam("headRow") Integer headRow,
                            @RequestParam(value = "remark", defaultValue = "") String remark) {
        ImportDto dto = new ImportDto(targetParam, headRow, remark);
        Object result = null;
        try {
            result = ExcelInvoke.importExcel(file, dto);
        } catch (Exception e) {
            AssertUtil.throwInnerException(e.getMessage());
        }
        return result;
    }

}
