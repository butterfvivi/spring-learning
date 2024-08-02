package org.vivi.framework.iexcelsimple.service;

import com.alibaba.excel.exception.ExcelDataConvertException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelsimple.common.exception.BizException;
import org.vivi.framework.iexcelsimple.common.utils.EasyExcelUtil;
import org.vivi.framework.iexcelsimple.entity.dto.User;
import org.vivi.framework.iexcelsimple.entity.model.UserRequest;
import org.vivi.framework.iexcelsimple.listener.CustomReadListener;
import org.vivi.framework.iexcelsimple.mapper.UserMapper;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ExcelHandleService extends ServiceImpl<UserMapper,User> {


    private static final String MES_TEMPLATE = "第%s行，第%s列-，单元格值[%s]，解析异常:%s ; \n";

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
        this.saveBatch(collect);
        log.info("获取到一批的数据批量插入完成:{}", requests);
    }

}
