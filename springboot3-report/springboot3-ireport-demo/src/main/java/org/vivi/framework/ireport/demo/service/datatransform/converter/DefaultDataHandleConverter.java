package org.vivi.framework.ireport.demo.service.datatransform.converter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.common.utils.IExcelUtils;
import org.vivi.framework.ireport.demo.common.utils.IocUtil;
import org.vivi.framework.ireport.demo.report.achieve.ExcelInvokeCore;
import org.vivi.framework.ireport.demo.report.config.IExportConfig;
import org.vivi.framework.ireport.demo.service.dataset.DataSetService;
import org.vivi.framework.ireport.demo.service.datatransform.ReportDataTransformService;
import org.vivi.framework.ireport.demo.web.dto.DataSearchDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.vivi.framework.ireport.demo.common.constant.Constants.TYPE_DYNAMIC;

@Slf4j
@Service
public class DefaultDataHandleConverter implements ReportDataTransformService {

    private static ExcelInvokeCore ExcelInvokeCore = IocUtil.getBean(ExcelInvokeCore.class);

    @Autowired
    private DataSetService dataSetService;

    @Override
    public List<T> transform(DataSearchDto searchDto) {
        List dataList = dataSetService.getAllMapData(searchDto);
        if (dataList == null) dataList = new ArrayList();

        List<String> headList = searchDto.getHeadList();
        if (headList == null) headList = new ArrayList<>();

        //进行动态数据重构处理判断
        List newDataList = (headList.size() != 0 && headList.size() != 0) ? IExcelUtils.restructureDynamicData(headList, dataList) : new ArrayList();

        // get export configuration
        IExportConfig config = searchDto.getExportConfig();
        if (config != null) {
            String targetParam = config.getTargetParam();
            //判断是否进行重写数据
            if (StringUtils.isNotBlank(targetParam)) {
                // invoke dynamic
                ExcelInvokeCore.invokeCache(targetParam, TYPE_DYNAMIC);
                ExcelInvokeCore.invokeDynamic(targetParam, newDataList, headList, searchDto.getParams());
            }
        }

        //重构表头,去除@符号
        if (headList.size() != 0) {
            headList = headList.stream().map(t -> {
                if (t.contains("@")) {
                    return t.split("@")[0];
                }
                return t;
            }).collect(Collectors.toList());
        }

        searchDto.setExportConfig(config);

        return newDataList;
    }



}
