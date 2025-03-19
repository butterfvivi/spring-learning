package org.vivi.framework.ireport.demo.core.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.MapUtils;
import org.vivi.framework.ireport.demo.common.constant.Constants;
import org.vivi.framework.ireport.demo.core.dto.IExportConfig;
import org.vivi.framework.ireport.demo.core.invoke.IHandleCell;
import org.vivi.framework.ireport.demo.utils.ConvertDataUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

public class IExcelUtils {

    private static final String RESOURCE_TEMPLATE = Constants.iExcelPath;

    /**
     * dynamic column export
     *
     * @param response
     * @param heads    column header list, reminder order must be consistent with data list,
     *                 column name can be constructed as "name@username", "age@age", "hobby@hobby"
     * @param data     data list,dynamic column data
     * @param config   configuration
     * @return
     * @author mashuai
     */
    public static <T> void writerDynamicToWeb(HttpServletResponse response, List<String> heads, List<T> data, IExportConfig config) throws IOException {
        writerDynamicToWebUtil(response, heads, data, config);
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
            addWriteHandle(config, write);
            write.head(headsList).sheet("sheet1").doWrite(handleDynamicData(data));
        } catch (IOException e) {
            resetResponse(response, e.getMessage());
        } finally {
            if (write != null) write.autoCloseStream(true);
        }
    }

    /**
     * Process the exported data into dynamic headers
     *
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

    /***
     * add excel table handler
     **/
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
            //use default handler
//            else if (mergeColIndex != null && mergeColIndex.size() != 0) {
//                write.registerWriteHandler(new IHandleCell(mergeColIndex,
//                        config.getExcludeRowIndex(),
//                        config.getExcludeTillRow(),
//                        config.getHeaderWord(),
//                        config.getMergerRowIndexLimit(),
//                        config.getMergerColIndexLimit(),
//                        config.getIsNeedLeftConditionMerge()
//                ));
//            }
        }
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
     * set response header
     **/
    private static void setResponse(HttpServletResponse response) throws UnsupportedEncodingException {
        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
    }

    /**
     * reset response header, print error message
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
