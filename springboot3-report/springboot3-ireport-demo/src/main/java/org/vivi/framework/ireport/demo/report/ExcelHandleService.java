package org.vivi.framework.ireport.demo.report;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.common.annotation.IExcelRewrite;

import java.util.List;
import java.util.Map;

@Slf4j
@IExcelRewrite(targetParam = "excel")
@Service(value = "excelHandleService")
public class ExcelHandleService {

    /**
     *
     **/
    @IExcelRewrite(targetParam = "template")
    public void templateExport(List<Map<String, Object>> data, Map<String, Object> otherVal) throws Exception {
    }

    /**
     * {
     *     "dataList":[
     *     {"id": null,"name": "张三14","sex": 2,"age": 22,"birthday": "2023-09-05","createTime": "2023-09-13T20:52:36","salary": 0.0300},
     *     {"id": null,"name": "张三15","sex": 2,"age": 23,"birthday": "2023-09-05","createTime": "   `   `2023-09-13T20:52:36","salary": 0.0300},
     *     {"id": null,"name": "张三16","sex": 0,"age": 24,"birthday": "2023-09-05","createTime": "2023-09-13T20:52:36","salary": 0.0300},
     *     {"id": null,"name": "张三17","sex": 0,"age": 25,"birthday": "2023-09-05","createTime": "2023-09-13T20:52:36","salary": 0.0300}
     * ],
     *     "headList":["id","姓名","性别","年龄","生日","创建时间","薪水"],
     *     "config":{
     *         "watermark":"vivi",
     *         "targetParam":"excel@dynamic"
     *     }
     * }
     */
    @IExcelRewrite(targetParam = "dynamic")
    public void dynamic(List<Map<String, Object>> data, List<String> headers) throws Exception {
        log.info("dynamic data list1:{}", JSONUtil.parse(data));
    }
}
