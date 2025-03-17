package org.vivi.framework.codegen.demo.controller;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.vivi.framework.codegen.demo.pojo.ColumnDetail;
import org.vivi.framework.codegen.demo.service.CodeGeneratorService;
import org.vivi.framework.codegen.demo.vo.ResultVo;

import java.util.List;

/**
 * @apiNote 代码生成器控制层
 * @description 代码生成器控制层
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gen/code")
public class CodeGeneratorController {

    private final CodeGeneratorService codeGeneratorService;


    /**
     * 获取所有表信息数据
     *
     * @return List<ColumnDetail>表数据
     */
    @GetMapping("/getAllTables")
    public ResultVo<List<ColumnDetail>> getAllTables() {
        List<ColumnDetail> ColumnDetailList = codeGeneratorService.getAllTableInfo();
        return ResultVo.success(ColumnDetailList);
    }

    /**
     * 生成代码
     *
     * @param tableName   表名（tb_user）
     * @param delPrefix   需要去除的前缀（tb_）
     * @param packageName 文件所在包的包名（cn.molu.generator）
     * @param type        生成类型（java/entity.java、vue3/vue2Page.vue3...）
     * @return String代码
     */
    @GetMapping("/generate/{tableName}")
    public ResultVo<String> getTableInfo(@PathVariable String tableName,
                                         @RequestParam(value = "delPrefix", required = false) String delPrefix,
                                         @RequestParam(value = "packageName", required = false) String packageName,
                                         @RequestParam(value = "type") String type
    ) {
        if (StrUtil.isBlank(tableName) || StrUtil.isBlank(type)) return ResultVo.fail("参数为空");
        List<ColumnDetail> columnDetailList = codeGeneratorService.getColumnDetails(tableName);
        String code = codeGeneratorService.generateCode(columnDetailList, delPrefix, packageName, type);
        System.out.println(code);
        return ResultVo.ok(code);
    }

}

