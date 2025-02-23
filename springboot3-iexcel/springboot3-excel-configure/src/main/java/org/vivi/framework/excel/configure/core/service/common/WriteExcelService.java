package org.vivi.framework.excel.configure.core.service.common;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vivi.framework.excel.configure.base.annotations.ExportField;
import org.vivi.framework.excel.configure.base.annotations.ExportTitle;
import org.vivi.framework.excel.configure.common.exceptions.DuplicatedIndexException;
import org.vivi.framework.excel.configure.core.beans.ExportFieldBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 公共业务报表导出据组件对外接口定义
 */
@Service
public class WriteExcelService {

    private Logger logger = LoggerFactory.getLogger(WriteExcelService.class);

    /**
     * 获取报表标题
     *
     * @param clazz
     * @return
     */
    private String getExportTitle(Class<?> clazz) {
        if (clazz.isAnnotationPresent(ExportTitle.class)) {
            ExportTitle exportTitle = clazz.getAnnotation(ExportTitle.class);
            return exportTitle.title();
        }
        return "通用报表";
    }

    /**
     * 将数据写入excel中，使用eazyexcel组件
     *
     * @param dataMapList
     * @param filePath
     * @param dataReportBean
     * @return
     * @throws java.io.IOException
     */
    public boolean writeListMap2Excel(List<Map<Integer, String>> dataMapList, String filePath, Object dataReportBean) throws IOException {

        long startTime = System.currentTimeMillis();
        List<List<String>> resultList = new ArrayList<>();

        //5.保存文件到filePath中
        File file = new File(filePath);
        file.createNewFile();
        FileOutputStream stream = FileUtils.openOutputStream(file);
        try {
            List<List<String>> head = this.getTableHeader(dataReportBean.getClass());

            dataMapList.stream().forEach(map -> {
                List<String> rowList = new ArrayList<>();
                for (int i = 0; i < map.size(); i++) {
                    rowList.add(map.get(i));
                }
                resultList.add(rowList);
            });
            String sheetName = getExportTitle(dataReportBean.getClass());
            logger.info("sheetName = {}" , sheetName);
            logger.info("dataMapList.size ={} ,head.size() = {},convertResultList = {} , row.size() = {}",dataMapList.size(),head.size(),resultList.size());

            EasyExcel.write(filePath)
                    // 这里放入动态头
                    .head(head).sheet(sheetName)
                    // 当然这里数据也可以用 List<List<String>> 去传入
                    .doWrite(resultList);
        } catch (Exception e) {
            logger.error("生成报表文件失败", e);
            return false;
        } finally {
            stream.close();
        }
        long endTime = System.currentTimeMillis();
        logger.info("generate excel use time:(endTime - startTime) = {}" , (endTime - startTime));
        return true;
    }

    /**
     * 将数据写入excel中，使用eazyexcel组件
     *
     * @param dataMapList
     * @param filePath
     * @return
     * @throws java.io.IOException
     */
    public boolean writeListMap2ExcelAndHead(List<Map<Integer, String>> dataMapList, String filePath, List<List<String>> heads,String sheetName) throws IOException {

        long startTime = System.currentTimeMillis();
        List<List<String>> resultList = new ArrayList<>();

        //5.保存文件到filePath中
        File file = new File(filePath);
        file.createNewFile();
        FileOutputStream stream = FileUtils.openOutputStream(file);
        try {

            dataMapList.stream().forEach(map -> {
                List<String> rowList = new ArrayList<>();
                for (int i = 0; i < map.size(); i++) {
                    rowList.add(map.get(i));
                }
                resultList.add(rowList);
            });
            logger.info("sheetName = {}" , sheetName);
            logger.info("dataMapList.size ={} ,head.size() = {},convertResultList = {} , row.size() = {}",dataMapList.size(),heads.size(),resultList.size());

            EasyExcel.write(stream)
                    // 这里放入动态头
                    .head(heads).sheet(sheetName)
                    // 当然这里数据也可以用 List<List<String>> 去传入
                    .doWrite(resultList);
        } catch (Exception e) {
            logger.error("生成报表文件失败", e);
            return false;
        } finally {
            stream.close();
        }
        long endTime = System.currentTimeMillis();
        logger.info("generate excel use time:(endTime - startTime) = {}" , (endTime - startTime));
        return true;
    }

    /**
     * 将数据写入excel中，使用eazyexcel组件
     *
     * @param dataObjectList
     * @param filePath
     * @param dataReportBean
     * @return
     * @throws java.io.IOException
     */
    public boolean writeListObject2Excel(List<Object> dataObjectList, String filePath, Object dataReportBean,String sheetName) throws IOException {

        long startTime = System.currentTimeMillis();

        //5.保存文件到filePath中
        File file = new File(filePath);
        file.createNewFile();
        FileOutputStream stream = FileUtils.openOutputStream(file);
        try {
            // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
            EasyExcel.write(filePath, dataReportBean.getClass()).sheet(sheetName).doWrite(dataObjectList);
        } catch (Exception e) {
            logger.error("生成报表文件失败", e);
            return false;
        } finally {
            stream.close();
        }
        long endTime = System.currentTimeMillis();
        logger.info("generate excel use time = {}" , (endTime - startTime));
        return true;
    }

    /**
     * 获取表头信息
     *
     * @param clazz
     * @return
     */
    private List<List<String>> getTableHeader(Class<?> clazz) throws DuplicatedIndexException {
        Map<Integer, ExportFieldBean> exportFieldBeanMap = this.getMetaFieldMap(clazz);
        List<List<String>> headList = new ArrayList<>();
        //判重
        for (int i = 0; i < exportFieldBeanMap.size(); i++) {
            List<String> rowList = new ArrayList<>();

            ExportFieldBean exportFieldBean = exportFieldBeanMap.get(i);
            if (exportFieldBean != null) {
                rowList.add(exportFieldBean.getTitle());
            } else {
                logger.error("it has a unknown column on = {}" ,exportFieldBean.toString());
                rowList.add("unknown column");
            }
            headList.add(rowList);
        }
        return headList;
    }

    /**
     * 反射获取导出模型中的列字段名称
     *
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    public Map<Integer, ExportFieldBean> getMetaFieldMap(Class<?> clazz) throws DuplicatedIndexException {

        Field[] fields = clazz.getDeclaredFields();
        Map<Integer, ExportFieldBean> metaMap = new HashMap<>(fields.length);

        for (int i = 0; i < fields.length; ++i) {
            Field f = fields[i];
            if (f.isAnnotationPresent(ExportField.class)) {
                ExportField exportField = f.getAnnotation(ExportField.class);
                ExportFieldBean exportFieldBean = new ExportFieldBean();
                exportFieldBean.setFormate(exportField.formate());
                exportFieldBean.setIndex(exportField.index());
                exportFieldBean.setSourceKey(exportField.sourceKey());
                exportFieldBean.setTitle(exportField.title());
                exportFieldBean.setReferIndex(exportField.referIndex());
                if(metaMap.containsKey(exportField.index())){
                    throw new DuplicatedIndexException("duplicated index in the query model,please check in :"+clazz.getName());
                }
                metaMap.put(exportField.index(), exportFieldBean);

            }
        }

        return metaMap;
    }

    /**
     * 反射获取导出模型中的列字段名称
     *
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    public Map<String, ExportFieldBean> getMetaFieldColumnMap(Class<?> clazz) {

        Field[] fields = clazz.getDeclaredFields();
        Map<String, ExportFieldBean> metaMap = new HashMap<>(fields.length);

        for (int i = 0; i < fields.length; ++i) {
            Field f = fields[i];
            if (f.isAnnotationPresent(ExportField.class)) {
                ExportField exportField = f.getAnnotation(ExportField.class);
                ExportFieldBean exportFieldBean = new ExportFieldBean();
                exportFieldBean.setFormate(exportField.formate());
                exportFieldBean.setIndex(exportField.index());
                exportFieldBean.setSourceKey(exportField.sourceKey());
                exportFieldBean.setTitle(exportField.title());
                exportFieldBean.setReferIndex(exportField.referIndex());
                metaMap.put(f.getName(), exportFieldBean);
            }
        }

        return metaMap;
    }

    /**
     * 反射获取导出模型中的列字段名称
     *
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws IllegalAccessException
     */
    public Set<String> getNeedConvertIndexSet(Class<?> clazz) {

        Field[] fields = clazz.getDeclaredFields();
        Set<String> indexConvertSet = new HashSet<>();

        for (int i = 0; i < fields.length; ++i) {
            Field f = fields[i];
            if (f.isAnnotationPresent(ExportField.class)) {
                ExportField exportField = f.getAnnotation(ExportField.class);
                if(StringUtils.isNotEmpty(exportField.sourceKey()) || StringUtils.isNotEmpty(exportField.formate())){
                    indexConvertSet.add(exportField.index()+"");
                }
            }
        }
        return indexConvertSet;
    }
}
