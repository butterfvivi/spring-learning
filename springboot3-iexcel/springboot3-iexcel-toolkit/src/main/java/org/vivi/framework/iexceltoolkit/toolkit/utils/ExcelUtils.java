package org.vivi.framework.iexceltoolkit.toolkit.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.multipart.MultipartFile;
import org.vivi.framework.iexceltoolkit.common.enums.Constant;
import org.vivi.framework.iexceltoolkit.common.utils.ConvertDataUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ExcelUtils {

    private static final String RESOURCE_TEMPLATE = Constant.ExcelPath;


    /**
     * 根据实体类解析excel，最终返回List<Bean>
     * 推荐写法，便于后期他人维护
     *
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
     *
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

}
