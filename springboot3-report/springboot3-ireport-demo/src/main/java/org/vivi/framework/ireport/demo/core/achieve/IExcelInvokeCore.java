package org.vivi.framework.ireport.demo.core.achieve;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.core.dto.IDynamicExportDto;
import org.vivi.framework.ireport.demo.core.dto.IExportConfig;
import org.vivi.framework.ireport.demo.core.dto.ITemplateExportDto;
import org.vivi.framework.ireport.demo.core.util.IExcelUtils;
import org.vivi.framework.ireport.demo.common.base.IocUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class IExcelInvokeCore {

    public void dynamicExport(HttpServletResponse response, IDynamicExportDto dto) throws Exception {
        List dataList = dto.getDataList();
        if (dataList == null) dataList = new ArrayList();

        List<String> headList = dto.getHeadList();
        if (headList == null) headList = new ArrayList<>();

        // restructure head list by
        List newDataList = (headList.size() != 0 && headList.size() != 0) ?
                IExcelUtils.restructureDynamicData(headList, dataList) : new ArrayList();

        // get export configuration
        IExportConfig config = dto.getConfig();
        if (config != null) {
            String targetParam = config.getTargetParam();
            //判断是否进行重写数据
            if (StrUtil.isNotBlank(targetParam)) {
                // invoke dynamic
                invokeDynamic(targetParam, newDataList, headList, dto.getParams());
            }
        }

        IExcelUtils.writerDynamicToWeb(response, headList, newDataList, config);
    }

    public void templateExport(HttpServletResponse response, ITemplateExportDto dto) {

    }

    /**
     * call template export, add parameter export
     **/
    private static void invokeDynamic(String targetParam, List dataList, List<String> headList, Map<String, Object> params) throws Exception {

    }

}
