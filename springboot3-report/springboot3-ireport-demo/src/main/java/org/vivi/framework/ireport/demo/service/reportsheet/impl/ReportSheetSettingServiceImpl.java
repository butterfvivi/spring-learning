package org.vivi.framework.ireport.demo.service.reportsheet.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.mapper.ReportSheetSettingMapper;
import org.vivi.framework.ireport.demo.model.report.ReportSheetSetting;
import org.vivi.framework.ireport.demo.service.reportsheet.ReportSheetSettingService;

import java.util.List;

@Service
public class ReportSheetSettingServiceImpl extends ServiceImpl<ReportSheetSettingMapper, ReportSheetSetting> implements ReportSheetSettingService {


    public List<String> getHeaders(Integer id) {
        //get headList
        ReportSheetSetting reportSheetSetting = this.getById(id);
        String headers = reportSheetSetting.getDynHeader();
        List<String> headerList = Lists.newArrayList(headers.split(","));
        return headerList;
    }
}
