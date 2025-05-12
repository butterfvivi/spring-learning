package org.vivi.framework.report.bigdata.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.vivi.framework.report.bigdata.entity.ItemUser;
import org.vivi.framework.report.bigdata.service.UserService;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.vivi.framework.report.bigdata.utils.HttpResponseUtil.excelOutput;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

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
}
