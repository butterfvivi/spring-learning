package org.vivi.framework.ureport.simple.datasource.action;

import java.util.List;
import java.util.Map;


import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.vivi.framework.ureport.simple.common.utils.MultipleJdbcTemplate;
import org.vivi.framework.ureport.simple.datasource.bean.DataSourceInfo;
import org.vivi.framework.ureport.simple.datasource.bean.PreviewParams;
import org.vivi.framework.ureport.simple.datasource.service.DataSourceService;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.Field;

/**
 * 数据源配置
 * 
 * @author tomcat
 *
 */
@RestController
@RequestMapping("/datasource")
public class DataSourceInfoAction {
	
	@Resource
	private DataSourceService dataSourceService;
	
	@RequestMapping("/selectBuildinDruidList")
	public List<Map<String, String>> selectBuildinDruidList() {
		return dataSourceService.selectBuildinDruidList();
	}
	
	@RequestMapping("/testConnection")
	public String testConnection(@RequestBody DataSourceInfo info) {
		return MultipleJdbcTemplate.testConnection(info);
	}

	@RequestMapping("/tableList")
	public List<Map<String, String>> selectTableList(@RequestBody DataSourceInfo info) {
		return dataSourceService.selectTableList(info);
	}

	@RequestMapping("/tableFieldList")
	public List<Field> selectFieldList(@RequestBody PreviewParams previewParams) {
		return dataSourceService.selectFieldList(previewParams);
	}

	@RequestMapping("/preview")
	public Map<String, Object> previewData(@RequestBody PreviewParams previewParams) {
		return dataSourceService.previewData(previewParams);
	}
	
	@RequestMapping("/springbeanFieldList")
	public List<Field> springbeanFieldList(String clazz) {
		return dataSourceService.getSpringBeanFieldList(clazz);
	}
}
