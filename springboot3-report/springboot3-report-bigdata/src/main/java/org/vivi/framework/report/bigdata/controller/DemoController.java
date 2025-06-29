package org.vivi.framework.report.bigdata.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.report.bigdata.entity.Cinfo;
import org.vivi.framework.report.bigdata.entity.ItemUser;
import org.vivi.framework.report.bigdata.paging.ExcelUtil;
import org.vivi.framework.report.bigdata.service.CinfoService;
import org.vivi.framework.report.bigdata.service.DemoService;
import org.vivi.framework.report.bigdata.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static cn.hutool.core.util.StrUtil.uuid;
import static org.vivi.framework.report.bigdata.utils.HttpResponseUtil.excelOutput;

@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {
    @Resource
    private DemoService demoService;

    @Resource
    private CinfoService cinfoService;

    @Resource
    private UserService userService;

    /**
     * 导出Excel(先查后写)，<br>
     * 注意：实际应用中，pageSize参数不应对外开放，此处为方便测试，或pageSize需要设置上限，防止恶意用户传一个很大的pageSize值，而撑满内存
     * @param response 响应
     * @param pageSize 页大小
     */
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response,
                            @RequestParam(value = "pageSize", defaultValue = "10000") int pageSize) throws IOException {
        LambdaQueryWrapper<ItemUser> queryWrapper = Wrappers.lambdaQuery(ItemUser.class);
        userService.writeExcel(excelOutput(response, "测试"), pageSize, queryWrapper);
    }

    /**
     * 导出Excel（并发查询，串行写入）<br>
     * 注意：实际应用中，pageSize和parallelNum参数不应对外开放，此处为方便测试，或pageSize需要设置上限，防止恶意用户传一个很大的pageSize值，而撑满内存。
     * @param response 响应
     * @param parallelNum 并发查询线程数
     * @param pageSize 页大小
     */
    @GetMapping("/writeExcelForParallel")
    public void writeExcelForParallel(HttpServletResponse response,
                                  @RequestParam(value = "parallelNum", defaultValue = "3") int parallelNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10000") int pageSize) throws IOException, InterruptedException {
        LambdaQueryWrapper<ItemUser> queryWrapper = Wrappers.lambdaQuery(ItemUser.class);
        userService.writeExcelForParallel(excelOutput(response, "测试"), parallelNum, pageSize, queryWrapper);
    }

    /**
     * 导出Excel[下载速度可能更快]（并发查询，串行写入）<br>
     * 注意：实际应用中，pageSize和parallelNum参数不应对外开放，此处为方便测试，或pageSize需要设置上限，防止恶意用户传一个很大的pageSize值，而撑满内存。
     * @param response 响应
     * @param parallelNum 并发查询线程数
     * @param pageSize 页大小
     */
    @GetMapping("/writeExcelForXParallel")
    public void writeExcelForXParallel(HttpServletResponse response,
                                      @RequestParam(value = "parallelNum", defaultValue = "3") int parallelNum,
                                      @RequestParam(value = "pageSize", defaultValue = "10000") int pageSize) throws IOException, InterruptedException, ExecutionException {
        LambdaQueryWrapper<ItemUser> queryWrapper = Wrappers.lambdaQuery(ItemUser.class);
        userService.writeExcelForXParallel(excelOutput(response, "测试"), parallelNum, pageSize, queryWrapper);
    }

    /**
     * 导出Excel（流式查询）
     * @param response 响应
     */
    @GetMapping("/writeExcelForFetch")
    public void writeExcelForFetch(HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<ItemUser> queryWrapper = Wrappers.lambdaQuery(ItemUser.class);
        userService.writeExcelForFetch(excelOutput(response, "测试"), queryWrapper);
    }

    /**
     * 下载导入模板
     * @param response 响应
     */
    @GetMapping("/downloadImportTemplate")
    public void downloadImportTemplate(HttpServletResponse response) throws IOException {
        demoService.writeExcelImportTemplate(excelOutput(response, "导入模板"));
    }

    /**
     * 导入Excel
     * @param multipartFile 上传的文件
     */
    @PostMapping("/importExcel")
    public void importExcel(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        ExcelUtil.read(multipartFile.getInputStream(), ItemUser.class, userService::saveBatch).sheet().doRead();
    }

    @PostMapping("/batchInsertCinfo")
    public void batchInsertCinfo() throws IOException {
        List<Cinfo> datas = Lists.newArrayList();
        for ( int i = 0; i< 50000; i++ ) {
            Cinfo data = new Cinfo();
            data.setC1(uuid());
            data.setC2(uuid());
            data.setC3(uuid());
            data.setC4(uuid());
            data.setC5(uuid());
            data.setC6(uuid());
            data.setC7(uuid());
            data.setC8(uuid());
            data.setC9(uuid());
            data.setC10(uuid());
            data.setC11(uuid());
            data.setC12(uuid());
            data.setC13(uuid());
            data.setC14(uuid());
            data.setC15(uuid());
            data.setC16(uuid());
            data.setC17(uuid());
            data.setC18(uuid());
            data.setC19(uuid());
            data.setC20(uuid());
            datas.add(data);
        }
        cinfoService.saveBatch(datas);
    }

    /**
     * 批量插入ItemUser测试数据
     */
    @PostMapping("/batchInsert")
    public void batchInsert() throws IOException {
        List<ItemUser> datas = Lists.newArrayList();
        for (int i = 0; i < 100000; i++) {
            ItemUser data = new ItemUser();
            data.setField1(uuid());
            data.setField2(uuid());
            data.setField3(uuid());
            data.setField4(uuid());
            data.setField5(uuid());
            data.setField6(uuid());
            data.setField7(uuid());
            data.setField8(uuid());
            data.setField9(uuid());
            data.setField10(uuid());
            data.setField11(uuid());
            data.setField12(uuid());
            data.setField13(uuid());
            data.setField14(uuid());
            data.setField15(uuid());
            data.setField16(uuid());
            data.setField17(uuid());
            data.setField18(uuid());
            data.setField19(uuid());
            data.setField20(uuid());
            data.setField21(uuid());
            data.setField22(uuid());
            data.setField23(uuid());
            data.setField24(uuid());
            data.setField25(uuid());
            data.setField26(uuid());
            data.setField27(uuid());
            data.setField28(uuid());
            data.setField29(uuid());
            data.setField30(uuid());
            data.setField31(uuid());
            data.setField32(uuid());
            data.setField33(uuid());
            data.setField34(uuid());
            data.setField35(uuid());
            data.setField36(uuid());
            data.setField37(uuid());
            data.setField38(uuid());
            data.setField39(uuid());
            data.setField40(uuid());
            data.setField41(uuid());
            data.setField42(uuid());
            data.setField43(uuid());
            data.setField44(uuid());
            data.setField45(uuid());
            data.setField46(uuid());
            data.setField47(uuid());
            data.setField48(uuid());
            data.setField49(uuid());
            data.setField50(uuid());
            datas.add(data);
        }
        userService.saveBatch(datas);
    }
}
