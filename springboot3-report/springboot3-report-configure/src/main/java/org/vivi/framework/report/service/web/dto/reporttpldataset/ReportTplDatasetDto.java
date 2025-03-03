package org.vivi.framework.report.service.web.dto.reporttpldataset;

import lombok.Data;
import org.vivi.framework.report.service.common.enums.YesNoEnum;
import org.vivi.framework.report.service.model.reporttpldataset.ReportTplDataset;

@Data
public class ReportTplDatasetDto extends ReportTplDataset {

	/**  
	 * @Fields isMobile : 是否是手机端
	 */
	private Integer isMobile = YesNoEnum.NO.getCode();
	
	private boolean initSelectData = false;
	
	/**  
	 * @Fields reportType : 类型 1excel 2doc
	 */
	private Integer reportType = 1;
}
