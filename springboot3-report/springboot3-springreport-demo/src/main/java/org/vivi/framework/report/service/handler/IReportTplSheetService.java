package org.vivi.framework.report.service.handler;

import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.report.service.common.entity.BaseEntity;
import org.vivi.framework.report.service.common.entity.PageEntity;
import org.vivi.framework.report.service.model.reporttplsheet.ReportTplSheet;

import java.util.List;

public interface IReportTplSheetService extends IService<ReportTplSheet> {

    List<String> getAllSheetNames(long tplId);
}
