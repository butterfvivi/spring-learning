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


    @IExcelRewrite(targetParam = "dynamic")
    public void dynamic(List<Map<String, Object>> data, List<String> headers) throws Exception {
        log.info("dynamic data list1:{}", JSONUtil.parse(data));
    }
}
