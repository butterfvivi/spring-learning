package org.vivi.framework.iexcelbatch.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.common.response.R;
import org.vivi.framework.iexcelbatch.common.utils.EasyExcelUtil2;
import org.vivi.framework.iexcelbatch.entity.model.User;
import org.vivi.framework.iexcelbatch.mapper.UserMapper;

@RestController
@RequestMapping
public class ExcelDynamicController {

    @Resource
    private UserMapper userMapper;

    //@PostMapping(value = "/import", produces = BaseConstant.REQUEST_HEADERS_CONTENT_TYPE)
    @Operation(summary = "测试-导入(全表覆盖)", description = "测试-导入(全表覆盖)")
    public R<Boolean> testImport(@RequestParam(value = "file")  MultipartFile file) {
        return R.success(
                EasyExcelUtil2.importExcel(
                        file,
                        User.class
                )
        );
    }

    //@PostMapping(value = "/import", produces = BaseConstant.REQUEST_HEADERS_CONTENT_TYPE)
    @Operation(summary = "测试-导入(按日期覆盖)", description = "测试-导入(按日期覆盖)")
    public R<Boolean> testImport(@RequestParam(value = "file")  MultipartFile file,  @RequestParam(value = "date") Integer date) {
        return R.success(
                EasyExcelUtil2.importExcel(
                        file,
                        date,
                        User::getAge,
                        User::setAge,
                        userMapper,
                        User.class
                )
        );
    }

    //@PostMapping(value = "/export", produces = BaseConstant.REQUEST_HEADERS_CONTENT_TYPE)
    @Operation(summary = "测试-导出", description = "测试-导出")
    public void testExport() {
        EasyExcelUtil2.exportExcel(
                User.class,
                userMapper.selectList(null)
        );
    }
}
