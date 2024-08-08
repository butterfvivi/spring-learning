package org.vivi.framework.iexcelbatch.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.common.CustomStyleStrategy;
import org.vivi.framework.iexcelbatch.common.converter.AdaptiveWidthStyleStrategy;
import org.vivi.framework.iexcelbatch.common.enums.Constant;
import org.vivi.framework.iexcelbatch.common.enums.FileNameEnum;
import org.vivi.framework.iexcelbatch.common.exception.BizException;
import org.vivi.framework.iexcelbatch.common.utils.EasyExcelUtil;
import org.vivi.framework.iexcelbatch.entity.model.User;
import org.vivi.framework.iexcelbatch.entity.query.UserQuery;
import org.vivi.framework.iexcelbatch.entity.query.UserRequest;
import org.vivi.framework.iexcelbatch.listener.CustomReadListener;
import org.vivi.framework.iexcelbatch.mapper.UserMapper;
import org.vivi.framework.iexcelbatch.service.ExcelHandleService;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExcelHandleServiceImpl implements ExcelHandleService {


    @Autowired
    private UserService userService;

    private static final String EXPORT_SHEET_NAME_TEMPLATE = "用户信息";
    private static final String MES_TEMPLATE = "第%s行，第%s列-，单元格值[%s]，解析异常:%s ; \n";


    /**
     * 触发分批导出阈值
     */
    private static final Integer PART_EXPORT_NUM = 5000;


    @Override
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
            Page<User> pageResult = userService.page(query);
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

    @Override
    @SneakyThrows
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response, UserQuery query) {
        query.setCurrent(1);
        query.setSize(PART_EXPORT_NUM);
        //文件名
        String fileName = String.format(Constant.EXPORT_FILE_NAME_TEMPLATE, DateUtil.now());
        //获取流
        ServletOutputStream outputStream = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcelUtil.excelWriter(outputStream, User.class, FileNameEnum.USER_INFO_IMPORT);
        //单个Sheet名
        WriteSheet writeSheet = EasyExcelFactory.writerSheet(FileNameEnum.USER_INFO_IMPORT.getDesc()).build();
        //模拟填充模板值
        Map<String, Object> map = new HashMap<>();
        map.put("date", DateUtil.today());
        excelWriter.fill(map ,writeSheet);
        long pages;
        do {
            //每批的数据
            Page<User> pageResult = userService.page(query);
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

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importTemplate(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
        if (!file.getOriginalFilename().endsWith(".xls") && !file.getOriginalFilename().endsWith(".xlsx")) {
            throw new BizException("文件格式不正确，请检查后重新上传");
        }
        //创建自定义的监听对象，并把要批量处理的方法交给监听器对象，监听器对象在满足读取的行数会去回调此方法
        CustomReadListener<UserRequest> customRedListener = new CustomReadListener<>(100, this::reqSaveBath);
        InputStream inputStream = file.getInputStream();
        EasyExcelUtil.read(inputStream, customRedListener);

        List<ExcelDataConvertException> excelDataConvertExceptionList = customRedListener.getExcelDataConvertExceptionList();

        StringBuffer sb = new StringBuffer("错误提示：\r\n");
        //并且此对象里面个异常对象，如果读取某个单元格报错
        excelDataConvertExceptionList.stream().forEach(excelDataConvertException -> {
            Integer rowIndex = excelDataConvertException.getRowIndex();
            Integer columnIndex = excelDataConvertException.getColumnIndex();
            String cellData = excelDataConvertException.getCellData().getStringValue();
            String message = excelDataConvertException.getMessage();
            log.warn("第{}行，第{}列-，单元格值[{}]，解析异常:{}",  rowIndex, columnIndex, cellData, message);
            sb.append(String.format(MES_TEMPLATE,  rowIndex, columnIndex, cellData, message));
        });

        XSSFWorkbook workbook = EasyExcelUtil.getWorkbook(file);
        // 生成错误文件excel
        XSSFSheet sheet = workbook.getSheetAt(0);
        //覆盖原有得massage
        String messageStr = EasyExcelUtil.getString(sb);
        sheet.getRow(0).getCell(6).setCellValue(messageStr);
        // 异步上传oss并记录
        //输出到浏览器
        EasyExcelUtil.sendToDownload(response, workbook);
        return CollectionUtils.isEmpty(excelDataConvertExceptionList);
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public Boolean importExcel(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
        if (!file.getOriginalFilename().endsWith(".xls") && !file.getOriginalFilename().endsWith(".xlsx")) {
            throw new BizException("文件格式不正确，请检查后重新上传");
        }
        //创建自定义的监听对象，并把要批量处理的方法交给监听器对象，监听器对象在满足读取的行数会去回调此方法
        CustomReadListener<UserRequest> customRedListener = new CustomReadListener<>(100, this::reqSaveBath);
        InputStream inputStream = file.getInputStream();
        EasyExcelUtil.read(inputStream, customRedListener);

        List<ExcelDataConvertException> excelDataConvertExceptionList = customRedListener.getExcelDataConvertExceptionList();

        StringBuffer sb = new StringBuffer("错误提示：\r\n");
        //并且此对象里面个异常对象，如果读取某个单元格报错
        excelDataConvertExceptionList.stream().forEach(excelDataConvertException -> {
            Integer rowIndex = excelDataConvertException.getRowIndex();
            Integer columnIndex = excelDataConvertException.getColumnIndex();
            String cellData = excelDataConvertException.getCellData().getStringValue();
            String message = excelDataConvertException.getMessage();
            log.warn("第{}行，第{}列-，单元格值[{}]，解析异常:{}",  rowIndex, columnIndex, cellData, message);
            sb.append(String.format(MES_TEMPLATE,  rowIndex, columnIndex, cellData, message));
        });

//        XSSFWorkbook workbook = EasyExcelUtil.getWorkbook(file);
//        // 生成错误文件excel
//        XSSFSheet sheet = workbook.getSheetAt(0);
//        //覆盖原有得massage
//        String messageStr = EasyExcelUtil.getString(sb);
//        sheet.getRow(0).getCell(6).setCellValue(messageStr);
//        // 异步上传oss并记录
//        //输出到浏览器
//        EasyExcelUtil.sendToDownload(response, workbook);
        return CollectionUtils.isEmpty(excelDataConvertExceptionList);
    }

    public void reqSaveBath(List<UserRequest> requests) {
        log.info("====获取到一批数据正在执行批量插入====");
        if (CollectionUtils.isEmpty(requests)) {
            return;
        }
        List<User> collect = requests.stream().map(request -> {
            User user = new User();
            user.setId(null); // id自增
            user.setName(request.getName());
            user.setSex(request.getSex());
            user.setAge(request.getAge());
            user.setBirthday(LocalDate.now());
            user.setCreateTime(LocalDateTime.now());
            user.setSalary(request.getSalary());
            return user;
        }).collect(Collectors.toList());
        userService.saveBatch(collect);
        log.info("获取到一批的数据批量插入完成:{}", requests);
    }

    @SneakyThrows
    @Override
    public void exportDynamic(HttpServletRequest request, HttpServletResponse response, UserQuery query) {
        query.setCurrent(1);
        query.setSize(Integer.MAX_VALUE);
        // 文件名
        String fileName = String.format(Constant.EXPORT_FILE_NAME_TEMPLATE, DateUtil.now().replace(":", "_"));
        // 获取流
        ServletOutputStream outputStream = response.getOutputStream();
        // 设置响应
        EasyExcelUtil.setResponseHeaders(response, fileName);
        // 创建 ExcelWriter
        ExcelWriter excelWriter = EasyExcel.write(outputStream).head(userService.head())
                .registerWriteHandler(new CustomStyleStrategy())
                .registerWriteHandler(new AdaptiveWidthStyleStrategy())
                .build();

        for (int i = 0; i < 2; i++) {
            // 创建 Sheet
            WriteSheet writeSheet = EasyExcelFactory.writerSheet("用户信息" + i).build();
            // 写入数据
            excelWriter.write(userService.dataUserList(query), writeSheet);
        }

        // 关闭资源
        excelWriter.finish();
        // 刷新并关闭输出流
        outputStream.flush();
        outputStream.close();
    }

}
