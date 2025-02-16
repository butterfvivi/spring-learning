package org.vivi.framework.excel.dynamic4.service;

import com.alibaba.excel.write.handler.WriteHandler;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface IEasyExcelService {
    /**
     * 导出excel方法
     *
     * @param exportData 需要导出的数据
     * @param response   response
     * @param tClass     导出excel的字段实体类
     * @param fileName   文件名字
     * @param sheetName  sheet名字
     */
    <T> void exportExcel(List<T> exportData, HttpServletResponse response, Class<T> tClass, String fileName, String sheetName);
    /**
     * 导出excel方法 (携带自定义策略)
     * @param exportData 需要导出的数据
     * @param response HttpServletResponse
     * @param tClass 导出excel的字段实体类
     * @param fileName 文件名字
     * @param sheetName sheet名字
     * @param writeHandler 自定义策略（可扩展多个）
     * @param <T> T
     */
    <T> void exportExcelWithHandler(List<T> exportData, HttpServletResponse response, Class<T> tClass, String fileName, String sheetName, WriteHandler writeHandler);
    /**
     * 导出excel方法(动态表头-动态数据) 携带自定义handler
     * @param response HttpServletResponse
     * @param head 表头
     * @param data 数据
     * @param fileName 文件名字
     * @param sheetName sheet名字
     * @param writeHandler 自定义策略（可扩展多个）
     * @param <T> T
     */
    <T> void exportExcelWithDynamicsHead(HttpServletResponse response, List<List<String>> head, List<List<Object>> data,
                                         String fileName, String sheetName, WriteHandler writeHandler);
}
