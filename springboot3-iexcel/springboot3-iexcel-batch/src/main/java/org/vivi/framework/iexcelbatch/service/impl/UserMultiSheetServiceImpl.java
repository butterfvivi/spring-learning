package org.vivi.framework.iexcelbatch.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.vivi.framework.iexcelbatch.common.enums.Constant;
import org.vivi.framework.iexcelbatch.common.utils.EasyExcelUtil;
import org.vivi.framework.iexcelbatch.entity.model.User;
import org.vivi.framework.iexcelbatch.entity.query.UserQuery;
import org.vivi.framework.iexcelbatch.service.UserMultiSheetService;

import java.util.List;
import java.util.Objects;


@Service
public class UserMultiSheetServiceImpl implements UserMultiSheetService {

    @Resource
    private UserService userService;

    /**
     * 触发分批导出阈值
     */
    private static final Integer PART_EXPORT_NUM = 50;
    /**
     * 每个sheet页的最大数量触发了切换下一个sheet页 这个值必须大于分批的阈值且%阈值==0
     */
    private static final Integer PART_EXPORT_SHEET_NUM = 50;

    @Override
    @SneakyThrows
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, UserQuery query) {
        // 设置文件名
        String fileName = String.format(Constant.EXPORT_FILE_NAME_TEMPLATE, DateUtil.now());
        // 设置响应头
        EasyExcelUtil.setResponseHeaders(response, fileName);
        // 创建输出流
        ServletOutputStream outputStream = response.getOutputStream();
        // 创建excel对象
        ExcelWriter excelWriter = EasyExcelUtil.excelWriter(outputStream, User.class);
        //初始化分页
        query.setCurrent(1);
        query.setSize(PART_EXPORT_NUM);

        long pages;
        int sheetIndex = 0;   //用于计算sheet页
        int accumulatedRows = 0; // 用于累积数据行数的计数器
        do {
            //每批的数据
            Page<User> pageResult = userService.page(query);
            List<User> records = pageResult.getRecords();
            pages = pageResult.getPages();
            int actualPageSize = Math.min(records.size(), PART_EXPORT_SHEET_NUM - accumulatedRows);
            // 创建Sheet对象
            WriteSheet writeSheet = EasyExcel.writerSheet(Constant.EXPORT_FILE_NAME_TEMPLATE + sheetIndex).build();
            // 分批写入文件，限制每个Sheet页的数据行数不超过 maxSheetPageSize
            excelWriter.write(records.subList(0, actualPageSize), writeSheet);
            // 更新计数器
            accumulatedRows += actualPageSize;
            // 如果累积的数据行数达到100,000条，切换到下一个Sheet页
            if (accumulatedRows >= PART_EXPORT_SHEET_NUM) {
                sheetIndex++;
                accumulatedRows = 0;
            }
            // 分批写入文件，限制每个Sheet页的数据行数不超过 maxSheetPageSize
            records.clear();
            Objects.requireNonNull(query).setCurrent(query.getCurrent() + 1);
        } while (query.getCurrent() <= pages);
        // 关闭excelWriter对象
        excelWriter.finish();
        // 关闭输出流
        outputStream.flush();
        outputStream.close();
    }
}
