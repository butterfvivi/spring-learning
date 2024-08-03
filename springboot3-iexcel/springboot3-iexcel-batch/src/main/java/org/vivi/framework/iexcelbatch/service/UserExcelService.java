package org.vivi.framework.iexcelbatch.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.iexcelbatch.common.enums.Constant;
import org.vivi.framework.iexcelbatch.common.utils.EasyExcelUtil;
import org.vivi.framework.iexcelbatch.entity.model.User;
import org.vivi.framework.iexcelbatch.entity.query.UserQuery;
import org.vivi.framework.iexcelbatch.mapper.UserMapper;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserExcelService {

    @Autowired
    private UserMapper userMapper;

    private static final String EXPORT_SHEET_NAME_TEMPLATE = "用户信息";
    private static final String MES_TEMPLATE = "第%s行，第%s列-，单元格值[%s]，解析异常:%s ; \n";

    /**
     * 触发分批导出阈值
     */
    private static final Integer PART_EXPORT_NUM = 5000;

    public Page<User> page(UserQuery query) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        Page<User> page = userMapper.selectPage(new Page<>(query.getCurrent(), query.getSize()), userLambdaQueryWrapper);
//        Page<User> userVOPage = new Page<>();
//        userVOPage.setRecords(BeanUtil.copyToList(page.getRecords(), User.class));
//        userVOPage.setTotal(page.getTotal());
//        userVOPage.setSize(page.getSize());
//        userVOPage.setCurrent(page.getCurrent());
//        userVOPage.setPages(page.getPages());
        return page;
    }

    @SneakyThrows
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, UserQuery query) {
        query.setCurrent(1);
        query.setSize(PART_EXPORT_NUM);
        //文件名
        String fileName = String.format(Constant.EXPORT_FILE_NAME_TEMPLATE, DateUtil.now());
        //获取流
        ServletOutputStream outputStream = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcelUtil.excelWriter(outputStream, User.class);
        //单个Sheet名  如果要多 sheet 把这行代码放在循环里面
        WriteSheet writeSheet = EasyExcelFactory.writerSheet(EXPORT_SHEET_NAME_TEMPLATE).build();
        long pages;
        do {
            //每批的数据
            Page<User> pageResult = this.page(query);
            List<User> records = pageResult.getRecords();
            pages = pageResult.getPages();
            //分批写入文件
            excelWriter.write(records, writeSheet);
            records.clear();
            Objects.requireNonNull(query).setCurrent(query.getCurrent() + 1);
        } while (query.getCurrent() <= pages);

        EasyExcelUtil.setResponseHeaders(response, fileName);
        excelWriter.finish();
        outputStream.flush();
        outputStream.close();
    }

}
