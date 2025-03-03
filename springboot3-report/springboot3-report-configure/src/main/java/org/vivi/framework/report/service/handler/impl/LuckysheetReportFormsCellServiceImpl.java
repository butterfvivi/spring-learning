package org.vivi.framework.report.service.handler.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.vivi.framework.report.service.handler.ILuckysheetReportFormsCellService;
import org.vivi.framework.report.service.mapper.luckysheetreportformscell.LuckysheetReportFormsCellMapper;
import org.vivi.framework.report.service.model.luckysheetreportformscell.LuckysheetReportFormsCell;

@Service
public class LuckysheetReportFormsCellServiceImpl  extends ServiceImpl<LuckysheetReportFormsCellMapper, LuckysheetReportFormsCell> implements ILuckysheetReportFormsCellService {
}
