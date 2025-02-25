package org.vivi.framework.iexceltoolkit.toolkit.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexceltoolkit.common.enums.Constant;
import org.vivi.framework.iexceltoolkit.common.utils.ConvertDataUtils;
import org.vivi.framework.iexceltoolkit.toolkit.core.FileUtilsCore;
import org.vivi.framework.iexceltoolkit.toolkit.dto.IExportConfig;
import org.vivi.framework.iexceltoolkit.toolkit.invoke.HandleCell;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ExcelUtils {

    private static final String RESOURCE_TEMPLATE = Constant.ExcelPath;



    /**
     * 动态列导出
     * 动态列导出。 heads@列头，注意顺序需要与data数据顺序一致；data@数据,动态列导出；config@配置项 可为null
     * @param response
     * @param heads    列头，注意顺序需要与data数据顺序一致
     * @param data     数据,动态列导出
     * @param config   配置项 可为null
     * @return
     * @author mashuai
     */

    public static <T> void writerDynamicToWeb(HttpServletResponse response, List<String> heads, List<T> data, IExportConfig config) throws IOException {
        writerDynamicToWebUtil(response, heads, data, config);
    }

    public static <T> void writerDynamicToWeb(HttpServletResponse response, List<String> heads, List<T> data) throws IOException {
        writerDynamicToWebUtil(response, heads, data, null);
    }

    /**
     * 复杂的excel导出，在基础上，可以增加合计、统计等等信息
     * 复杂的excel导出，在基础上，可以增加合计、统计等等信息。 data@导出的数据; templatePath@导出数据的模板; otherValMap@填充合计等特殊行数据; config@配置项
     * @param response
     * @param data         导出的数据
     * @param templatePath 导出数据的模板
     * @param otherValMap  {填充合计等特殊行数据}
     * @param config       配置项
     * @return
     * @author mashuai
     */

    public static <T> void writerTemplateToWeb(HttpServletResponse response, List<T> data, String templatePath, Map<String, Object> otherValMap, IExportConfig config) throws IOException {
        writerTemplateToWebUtil(response, data, templatePath, otherValMap, config);
    }

    public static <T> void writerTemplateToWeb(HttpServletResponse response, List<T> data, String templatePath, Map<String, Object> otherValMap) throws IOException {
        writerTemplateToWebUtil(response, data, templatePath, otherValMap, null);
    }

    /**
     * 根据实体类解析excel，最终返回List<Bean>
     * 推荐写法，便于后期他人维护
     * 根据实体类解析excel，最终返回List<Bean>。 clazz@类对象 需要映射的字段加上 @ExcelProperty('excel的字段名'); file@上传文件MultipartFile流; headRowNumber@几行标题（没有标题就写0，第一行如果是标题就写1.第一和二行是标题就写2.....）
     * @param clazz         类对象 需要映射的字段加上 @ExcelProperty("excel的字段名")
     * @param file          上传文件MultipartFile流
     * @param headRowNumber 几行标题（没有标题就写0，第一行如果是标题就写1.第一和二行是标题就写2.....）
     * @return 返回了List<Bean>格式
     * @author mashuai
     */

    public static <T> List<T> readExcelBean(Class<T> clazz, MultipartFile file, Integer headRowNumber) throws Exception {
        return readExcelBeanUtils(clazz, null, file, headRowNumber);
    }


    public static <T> List<T> readExcelBean(Class<T> clazz, String fileNamePath, Integer headRowNumber) throws Exception {
        return readExcelBeanUtils(clazz, fileNamePath, null, headRowNumber);
    }

    /**
     * 如果列名称以 姓名@name构建而成，那么重构数据顺序...要么全部其别名映射，要么一个都不起
     * 如果列名称以 姓名@name构建而成，那么重构数据顺序...要么全部其别名映射，要么一个都不起。headList@['姓名@name' , '年龄@age','爱好@hobby']; dataList@[{'hobby:'爱好'','name':'zs','age':'12'}]"
     * @param headList ["姓名@name" , "年龄@age","爱好@hobby"]
     * @param dataList [{"hobby:"爱好","name":"zs","age":"12"}]
     * @return
     * @author mashuai
     */

    public static List restructureDynamicData(List<String> headList, List dataList) {
        if (headList.get(0).contains("@")) {
            List<Map<String, Object>> newDataList = new ArrayList<>();
            for (Object obj : dataList) {
                Map<String, Object> objMap = (Map<String, Object>) obj;
                LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                for (String header : headList) {
                    String[] split = header.split("@");
                    String prop = split[1];
                    map.put(prop, MapUtils.getObject(objMap, prop));
                }
                newDataList.add(map);
            }
            return newDataList;
        }
        return dataList;
    }

    /**
     * 处理逻辑
     **/
    private static <T> void writerDynamicToWebUtil(HttpServletResponse response, List<String> heads, List<T> data, IExportConfig config) throws IOException {
        setResponse(response);
        ExcelWriterBuilder write = null;
        try {
            List<List<String>> headsList = new ArrayList<>();
            heads.forEach(t -> headsList.add(Arrays.asList(t)));
            write = EasyExcel.write(response.getOutputStream());
            //水印
            //setWaterMark(config, write);
            addWriteHandle(config, write);
            write.head(headsList).sheet("sheet1").doWrite(handleDynamicData(data));
        } catch (IOException e) {
            resetResponse(response, e.getMessage());
        } finally {
            if (write != null) write.autoCloseStream(true);
        }
    }

    /**
     * 处理逻辑
     **/
    private static <T> void writerTemplateToWebUtil(HttpServletResponse response, List<T> data, String templatePath, Map<String, Object> otherValMap, IExportConfig config) throws IOException {
        if (otherValMap == null) otherValMap = new HashMap<>();
        setResponse(response);
        ExcelWriter excelWriter = null;
        try {
            ExcelWriterBuilder excelWriterBuilder = EasyExcel.write(response.getOutputStream());
            //水印
            //setWaterMark(config, excelWriterBuilder);
            //模板导出，在单元格合并时候，会出现问题
            addWriteHandle(config, excelWriterBuilder);
            excelWriter = excelWriterBuilder.withTemplate(getTemplateFile(templatePath)).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            excelWriter.fill(data, fillConfig, writeSheet);
            excelWriter.fill(otherValMap, writeSheet);
        } catch (Exception e) {
            e.printStackTrace();
            resetResponse(response, e.getMessage());
        } finally {
            if (excelWriter != null) excelWriter.finish();
        }
    }

    /***
     * 添加excel的表格处理器
     * **/
    private static void addWriteHandle(IExportConfig config, ExcelWriterBuilder write) {
        if (config != null) {
            List<WriteHandler> writeHandlers = config.getWriteHandlers();
            Set<Integer> mergeColIndex = config.getMergeColIndex();
            //1.优先使用自定义的处理器
            if (writeHandlers != null && writeHandlers.size() != 0) {
                for (WriteHandler writeHandler : writeHandlers) {
                    write.registerWriteHandler(writeHandler);
                }
            }
            //2.使用给定的处理器
            else if (mergeColIndex != null && mergeColIndex.size() != 0) {
                write.registerWriteHandler(new HandleCell(mergeColIndex,
                        config.getExcludeRowIndex(),
                        config.getExcludeTillRow(),
                        config.getHeaderWord(),
                        config.getMergerRowIndexLimit(),
                        config.getMergerColIndexLimit(),
                        config.getIsNeedLeftConditionMerge()
                ));
            }
        }
    }

    /**
     * 把导出的数据处理成动态头
     **/
    private static <T> List<List<Object>> handleDynamicData(List<T> data) {
        List<Map<String, Object>> mapList = data.stream().map(t -> (Map<String, Object>) JSON.parseObject(ConvertDataUtils.objToJsonStr(t), LinkedHashMap.class)).collect(Collectors.toList());
        List<List<Object>> dataList = new ArrayList<>(mapList.size());
        for (Map<String, Object> map : mapList) {
            List<Object> valList = new ArrayList<>();
            for (String key : map.keySet()) {
                valList.add(map.get(key));
            }
            dataList.add(valList);
        }
        return dataList;
    }


    /**
     * 处理数据
     **/
    private static <T> List<T> readExcelBeanUtils(Class<T> clazz, String fileNamePath, MultipartFile file, Integer headRowNumber) throws Exception {
        List<T> result = new CopyOnWriteArrayList<>();
        PageReadListener<T> tPageReadListener = new PageReadListener<>(dataList -> {
            result.addAll(dataList);
        });
        //读取上传的文件
        ExcelReaderBuilder read = null;
        if (file != null) {
            read = EasyExcel.read(file.getInputStream(), clazz, tPageReadListener);
        }
        //读取本地文件
        else {
            read = EasyExcel.read(fileNamePath, clazz, tPageReadListener);
        }
        read.sheet().headRowNumber(headRowNumber).doRead();
        return result;
    }


    /**
     * 设置response的响应头
     **/
    private static void setResponse(HttpServletResponse response) throws UnsupportedEncodingException {
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
    }

    /**
     * 重置response的响应头，返回失败的原因
     **/
    private static void resetResponse(HttpServletResponse response, String errorMsg) throws IOException {
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Map<String, String> map = new HashMap<>(1);
        map.put("errorMsg", errorMsg);
        response.getWriter().println(map.toString());
    }

    /**
     * 读取resources目录下文件流
     **/
    private static InputStream getTemplateFile(String templatePath) throws IOException {
        return FileUtilsCore.getResourcesFile(RESOURCE_TEMPLATE+templatePath);
    }

//    /**
//     * 添加水印
//     **/
//    private static void setWaterMark(IExportConfig config, ExcelWriterBuilder writer) {
//        if (null != config && StringUtils.isNotEmpty(config.getWatermark())) {
//            writer.inMemory(true).registerWriteHandler(new MsWatermarkUtils(config.getWatermark()));
//        }
//    }
}
