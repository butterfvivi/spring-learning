package org.vivi.framework.report.service.handler.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.common.enums.DelFlagEnum;
import org.vivi.framework.report.service.common.utils.ListUtil;
import org.vivi.framework.report.service.handler.IReportTplSheetService;
import org.vivi.framework.report.service.mapper.reporttplsheet.ReportTplSheetMapper;
import org.vivi.framework.report.service.model.reporttplsheet.ReportTplSheet;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportTplSheetServiceImpl  extends ServiceImpl<ReportTplSheetMapper, ReportTplSheet> implements IReportTplSheetService {

    @Override
    public List<String> getAllSheetNames(long tplId) {
        List<String> result = new ArrayList<>();
        QueryWrapper<ReportTplSheet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tpl_id", tplId);
        queryWrapper.eq("del_flag", DelFlagEnum.UNDEL.getCode());
        List<ReportTplSheet> reportTplSheets = this.list(queryWrapper);
        if(!ListUtil.isEmpty(reportTplSheets))
        {
            for (int i = 0; i < reportTplSheets.size(); i++) {
                result.add(reportTplSheets.get(i).getSheetName());
            }
        }
        return result;
    }
}
