package org.vivi.framework.easyexcel.simple.common.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.vivi.framework.easyexcel.simple.common.response.R;
import org.vivi.framework.easyexcel.simple.listener.DefaultAnalysisListener;
import org.vivi.framework.easyexcel.simple.common.enums.ResponseCodeEnum;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EasyExcelUtils {

    /**
     * 读取Excel(一个sheet)
     *
     * @param inputStream 输入流
     * @param rowModel    实体类
     * @param listener    excel读取监听器,已实现入库等逻辑
     */
    public static void readExcel(InputStream inputStream, Class<?> rowModel, ReadListener<?> listener) {
        EasyExcel.read(inputStream, rowModel, listener).sheet().doRead();
    }

    /**
     * 读取Excel(一个sheet),需自行实现入库等逻辑
     *
     * @param inputStream 输入流
     * @param rowModel    实体类
     * @param sheetName   sheet名称
     * @return List<T>
     */
    public static <T> List<T> readExcel(InputStream inputStream, Class<T> rowModel, String sheetName) {
        DefaultAnalysisListener<T> listener = new DefaultAnalysisListener<>();

        EasyExcel.read(inputStream, rowModel, listener)
                .sheet(sheetName)
                .doRead();

        return listener.getList();
    }

    /**
     * 读取Excel(单个 sheet)
     * 将多sheet合并成一个list数据集，通过自定义ExcelReader继承AnalysisEventListener
     * 重写invoke doAfterAllAnalysed方法
     */
    public static <T> List<T> readExcel(InputStream inputStream, Class<T> rowModel) {
        ExcelReader reader = EasyExcel.read(inputStream).build();
        if (reader == null) {
            return new ArrayList<>();
        }
        return readExcel(reader, rowModel, 0);
    }

    /**
     * 读取Excel(指定sheet)
     *
     * @param inputStream 文件输入流
     * @param rowModel    实体类映射
     * @param sheetNo     sheet 的序号 从1开始
     * @return List 数据 list
     */
    public static <T> List readExcel(InputStream inputStream, Class<T> rowModel, int sheetNo) {
        return readExcel(EasyExcel.read(inputStream).build(), rowModel, sheetNo);
    }

    /**
     * 读取Excel(多个 sheet)
     *
     * @param inputStream 输入流
     * @param rowModel    实体类映射
     */
    public static List[] readExcel(InputStream inputStream, Class<?>[] rowModel) {
        ExcelReader reader = EasyExcel.read(inputStream).build();
        if (reader == null) {
            return new ArrayList[rowModel.length];
        }
        List[] result = new ArrayList[rowModel.length];
        for (int i = 0; i < rowModel.length; i++) {
            result[i] = new ArrayList<>(readExcel(reader, rowModel[i], i));
        }
        return result;
    }

    /**
     * 读取Excel(指定sheet)
     *
     * @param reader   excel读取器
     * @param rowModel 实体类映射
     * @param sheetNo  sheet 的序号 从1开始
     * @return List<T>
     */
    public static <T> List<T> readExcel(ExcelReader reader, Class<T> rowModel, int sheetNo) {
        if (reader == null) {
            return new ArrayList<>();
        }
        List<ReadSheet> readSheetList = new ArrayList<>();
        DefaultAnalysisListener<T> listener = new DefaultAnalysisListener<>();
        ReadSheet readSheet = EasyExcel.readSheet(sheetNo)
                .head(rowModel)
                .registerReadListener(listener)
                .build();
        readSheetList.add(readSheet);
        reader.read(readSheetList);
        return listener.getList();
    }

    /**
     * 导出Excel(一个sheet)
     *
     * @param response  HttpServletResponse 响应
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的sheet名
     * @param list      数据list
     */
    public static <T> void writeExcel(HttpServletResponse response, String fileName, String sheetName, List<T> list) {
        try {
            OutputStream outputStream = getOutputStream(response, fileName);
            sheetName = StringUtils.isNotBlank(sheetName) ? sheetName : "Sheet1";

            // 这里需要设置不关闭流
            EasyExcel.write(outputStream)
                    .autoCloseStream(Boolean.FALSE)
                    .sheet(sheetName)
                    .head(list.get(0).getClass())
                    .doWrite(list);
        } catch (IOException e) {
            responseFailure(response);
        }
    }

    /**
     * 导出Excel(多个sheet)
     *
     * @param response  HttpServletResponse 响应
     * @param sheetName 导入文件的sheet名
     * @param lists     数据列表lists
     */
    public static void writeExcel(HttpServletResponse response, String fileName, String sheetName, List[] lists) {
        ExcelWriter excelWriter = null;
        sheetName = StringUtils.isNotBlank(sheetName) ? sheetName : "Sheet";
        try {
            excelWriter = EasyExcel.write(getOutputStream(response, fileName)).build();
            for (int i = 0; i < lists.length; i++) {
                if (CollectionUtils.isEmpty(lists[i])) {
                    continue;
                }
                WriteSheet writeSheet = EasyExcel.writerSheet(i, sheetName + (i + 1))
                        .head(lists[i].get(0).getClass())
                        .build();
                excelWriter.write(lists[i], writeSheet);
            }
        } catch (IOException e) {
            responseFailure(response);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 获取OutputStream
     *
     * @param response HttpServletResponse 响应
     * @param fileName fileName 导出的文件名
     * @return OutputStream 输出流
     */
    private static OutputStream getOutputStream(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fn = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition",
                "attachment;filename=" + fn + ExcelTypeEnum.XLSX.getValue());
        return response.getOutputStream();
    }

    /**
     * 响应错误信息
     *
     * @param response HttpServletResponse 响应
     */
    private static void responseFailure(HttpServletResponse response) {
        // 重置response
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        R<?> failure = R.failed(ResponseCodeEnum.FAILED_DOWNLOAD_FILE);
        try {
            response.getWriter().println(JSON.toJSONString(failure));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
