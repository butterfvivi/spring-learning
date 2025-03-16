package org.vivi.framework.report.service.web.dto.reporttpldataset;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* <p>Title: DatasetsParamDto</p>
* <p>Description: 数据集参数用实体类</p>
*/
@Data
public class DatasetsParamDto implements Serializable{

	/**
	* @Feilds:datasetId 数据集id
	*/
	private Long datasetId;
	
	/**
	* @Feilds:datasetName 数据集名称
	*/
	private String datasetName;
	
	/**
	* @Feilds:datasourceId 数据库id
	*/
	private Long datasourceId;
	
	/**
	* @Feilds:params 参数
	*/
	private List<ReportParamDto> params;
}
