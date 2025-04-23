package org.vivi.framework.ireport.demo.service.reportsheet;

import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.ireport.demo.model.report.ReportSheetSet;

import java.util.List;

public interface ReportSheetSetService extends IService<ReportSheetSet> {

    List<String> getHeaders(Long id);

    List<ReportSheetSet> getAllSheetSetting(Long reportIds);
}
