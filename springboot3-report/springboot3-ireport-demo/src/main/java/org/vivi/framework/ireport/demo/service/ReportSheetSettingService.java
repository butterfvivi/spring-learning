package org.vivi.framework.ireport.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.vivi.framework.ireport.demo.model.report.ReportSheetSetting;

import java.util.List;

public interface ReportSheetSettingService  extends IService<ReportSheetSetting> {

    List<String> getHeaders(Integer id);
}
