package org.vivi.framework.report.service.web.dto.reporttpldataset;

import lombok.Data;
import org.vivi.framework.report.service.model.reporttpldataset.ReportTplDataset;

import java.util.List;
import java.util.Map;

@Data
public class ReportDatasetDto extends ReportTplDataset {

	/**
	* @Feilds:columns 数据列
	*/
	private List<Map<String, Object>> columns;
}
