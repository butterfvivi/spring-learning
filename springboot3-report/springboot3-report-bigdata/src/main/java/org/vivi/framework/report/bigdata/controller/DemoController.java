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
import org.vivi.framework.report.bigdata.entity.Demo;
import org.vivi.framework.report.bigdata.paging.ExcelUtil;
import org.vivi.framework.report.bigdata.service.CinfoService;
import org.vivi.framework.report.bigdata.service.DemoService;

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

    /**
     * 导出Excel(先查后写)，<br>
     * 注意：实际应用中，pageSize参数不应对外开放，此处为方便测试，或pageSize需要设置上限，防止恶意用户传一个很大的pageSize值，而撑满内存
     * @param response 响应
     * @param pageSize 页大小
     */
    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response,
                            @RequestParam(value = "pageSize", defaultValue = "10000") int pageSize) throws IOException {
        LambdaQueryWrapper<Demo> queryWrapper = Wrappers.lambdaQuery(Demo.class);
        demoService.writeExcel(excelOutput(response, "测试"), pageSize, queryWrapper);
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
        LambdaQueryWrapper<Demo> queryWrapper = Wrappers.lambdaQuery(Demo.class);
        demoService.writeExcelForParallel(excelOutput(response, "测试"), parallelNum, pageSize, queryWrapper);
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
        LambdaQueryWrapper<Demo> queryWrapper = Wrappers.lambdaQuery(Demo.class);
        demoService.writeExcelForXParallel(excelOutput(response, "测试"), parallelNum, pageSize, queryWrapper);
    }

    /**
     * 导出Excel（流式查询）
     * @param response 响应
     */
    @GetMapping("/writeExcelForFetch")
    public void writeExcelForFetch(HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<Demo> queryWrapper = Wrappers.lambdaQuery(Demo.class);
        demoService.writeExcelForFetch(excelOutput(response, "测试"), queryWrapper);
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
        ExcelUtil.read(multipartFile.getInputStream(), Demo.class, demoService::saveBatch).sheet().doRead();
    }

    @PostMapping("/batchInsert")
    public void batchInsert() throws IOException {
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
}
