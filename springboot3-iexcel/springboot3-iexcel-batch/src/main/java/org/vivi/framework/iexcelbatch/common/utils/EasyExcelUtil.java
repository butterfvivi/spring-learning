package org.vivi.framework.iexcelbatch.common.utils;

import cn.hutool.core.lang.Assert;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.handler.WriteHandler;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexcelbatch.common.enums.FileNameEnum;
import org.vivi.framework.iexcelbatch.common.converter.AdaptiveWidthStyleStrategy;
import org.vivi.framework.iexcelbatch.entity.query.UserRequest;
import org.vivi.framework.iexcelbatch.listener.CustomReadListener;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

@Slf4j
public class EasyExcelUtil {

    /**
     * 将列表以 Excel 响应给前端
     *
     * @param response  响应
     * @param filename  文件名
     * @param sheetName Excel sheet 名
     * @param head      Excel head 头
     * @param data      数据列表哦
     * @param <T>       泛型，保证 head 和 data 类型的一致性
     * @throws IOException 写入失败的情况
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName,
                                 Class<T> head, List<T> data) throws IOException {
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), head)
                .autoCloseStream(false) // 不要自动关闭，交给 Servlet 自己处理
                .registerWriteHandler(new AdaptiveWidthStyleStrategy()) // 基于 column 长度，自动适配。最大 255 宽度
                .sheet(sheetName).doWrite(data);
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        setResponseHeaders(response, filename);
    }

    /**
     * 普通的读取
     *
     * @param file
     * @param head
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
        return EasyExcel.read(file.getInputStream(), head, null)
                .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
                .doReadAllSync();
    }


    /**
     * 使用自定义的监听器类型读取
     *
     * @param inputStream
     * @param customRedListener
     * @throws IOException
     */
    public static <T> void read(InputStream inputStream, CustomReadListener<T> customRedListener) throws IOException {
        EasyExcel.read(inputStream, UserRequest.class, customRedListener)
                .sheet(0)
                .headRowNumber(1)
                .doRead();
    }


    /**
     * 使用自定义模板，不使用easyexce的表头 获取ExcelWriter
     *
     * @param outputStream
     * @param head
     * @param fileNameEnum 导出模板枚举 {@link FileNameEnum}
     * @return
     * @throws IOException
     */
    public static ExcelWriter excelWriter(ServletOutputStream outputStream, Class<?> head, FileNameEnum fileNameEnum) throws IOException {
        ExcelWriter excelWriter = EasyExcelFactory.write(outputStream, head)
                .registerWriteHandler(new AdaptiveWidthStyleStrategy())
                .withTemplate(getTemplateInputStream(fileNameEnum))
                .needHead(Boolean.FALSE)
                .autoCloseStream(false)
                .build();
        return excelWriter;
    }



    /**
     * 使用自定义模板，不使用easyexce的表头
     *
     * @param outputStream
     * @param head
     * @param fileNameEnum 导出模板枚举 {@link FileNameEnum}
     * @return
     * @throws IOException
     */
    public static ExcelWriter excelWriter(ServletOutputStream outputStream, Class<?> head, FileNameEnum fileNameEnum, WriteHandler writeHandler) throws IOException {
        ExcelWriter excelWriter = EasyExcelFactory.write(outputStream, head)
                .registerWriteHandler(new AdaptiveWidthStyleStrategy())
                .withTemplate(getTemplateInputStream(fileNameEnum))
                .registerWriteHandler(writeHandler)
                .needHead(Boolean.FALSE)
                .autoCloseStream(false)
                .build();
        return excelWriter;
    }

    /**
     * 使用自定义模板，不使用easyexce的表头 获取ExcelWriter 支持下拉值
     *
     * @param outputStream
     * @param head
     * @param
     * @return
     * @throws IOException
     */
    public static ExcelWriter excelWriter(ServletOutputStream outputStream, Class<?> head, InputStream inputStream) throws IOException {
        ExcelWriter excelWriter = EasyExcelFactory.write(outputStream, head)  //特定的对象结构
                .registerWriteHandler(new AdaptiveWidthStyleStrategy()) //自使用长宽
                .withTemplate(inputStream)      //模板流
                .needHead(Boolean.FALSE)  //不使用easyexce的表头
                .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
                .build();
        return excelWriter;
    }


    /**
     * 使用自定义模板，不使用easyexce的表头 获取ExcelWriter 支持下拉值
     *
     * @param outputStream
     * @param head
     * @param
     * @return
     * @throws IOException
     */
    public static ExcelWriter excelWriter(ServletOutputStream outputStream, Class<?> head, InputStream inputStream, WriteHandler writeHandler) throws IOException {
        ExcelWriter excelWriter = EasyExcelFactory.write(outputStream, head)  //特定的对象结构
                .registerWriteHandler(new AdaptiveWidthStyleStrategy()) //自使用长宽
                .withTemplate(inputStream)      //模板流
                .registerWriteHandler(writeHandler)  //自定义下拉值的Handler
                .needHead(Boolean.FALSE)  //不使用easyexce的表头
                .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
                .build();
        return excelWriter;
    }




    /**
     * 使用easyexcel的表头
     *
     * @param outputStream
     * @param head
     * @return
     */
    public static ExcelWriter excelWriter(ServletOutputStream outputStream, Class<?> head) {
        ExcelWriter excelWriter = EasyExcelFactory.write(outputStream, head)
                .registerWriteHandler(new AdaptiveWidthStyleStrategy())
                .autoCloseStream(false)
                .build();
        return excelWriter;
    }


    /**
     * 设置响应编码
     *
     * @param response
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    public static void setResponseHeaders(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
    }


    /**
     * @param fileNameEnum
     * @return
     * @throws IOException
     */
    public static InputStream getTemplateInputStream(FileNameEnum fileNameEnum) throws IOException {
        Assert.notNull(fileNameEnum, "模板信息异常");
        // 获取要下载的文件的模板文件路径
        String realPath = "template" + File.separator + fileNameEnum.getDir() + File.separator + fileNameEnum.getModelName();
        return Objects.requireNonNull(EasyExcelUtil.class.getClassLoader().getResourceAsStream(realPath));
    }


    /**
     * 生成一个 workbook 用于写报错信息
     *
     * @param file
     * @return
     */
    public static XSSFWorkbook getWorkbook(MultipartFile file) {
        XSSFWorkbook workbook = null;
        try {
            workbook = (XSSFWorkbook) WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException("文件异常");
        }
        return workbook;
    }


    public static String getString(StringBuffer sb) {
        String messageStr = sb.toString();
        if (messageStr.length() > 30000) {
            messageStr = messageStr.substring(0, 30000);
        }
        return messageStr;
    }

    /**
     * 输出到浏览器
     *
     * @param response
     * @param wk
     */
    public static void sendToDownload(HttpServletResponse response, Workbook wk) {
        // 输出到浏览器
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            wk.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

