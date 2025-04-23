package org.vivi.framework.ireport.demo.service.reportsheet.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.vivi.framework.ireport.demo.mapper.ReportSheetSettingMapper;
import org.vivi.framework.ireport.demo.model.report.ReportSheetSet;
import org.vivi.framework.ireport.demo.service.reportsheet.ReportSheetSetService;

import java.util.List;

@Service
public class ReportSheetSetServiceImpl extends ServiceImpl<ReportSheetSettingMapper, ReportSheetSet> implements ReportSheetSetService {


    public List<String> getHeaders(Long id) {
        //get headList
        ReportSheetSet reportSheetSet = this.getById(id);
        String headers = reportSheetSet.getDynHeader();
        List<String> headerList = Lists.newArrayList(headers.split(","));
        return headerList;
    }

    @Override
    public List<ReportSheetSet> getAllSheetSetting(Long reportIds) {
        return this.lambdaQuery().eq(ReportSheetSet::getRtId, reportIds).list();
    }
}
