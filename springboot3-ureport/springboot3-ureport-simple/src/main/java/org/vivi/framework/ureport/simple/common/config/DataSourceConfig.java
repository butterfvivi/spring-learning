package org.vivi.framework.ureport.simple.common.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.vivi.framework.ureport.simple.datasource.bean.DataSourceInfo;


@Component
@ConfigurationProperties(prefix="ureport.support")
public class DataSourceConfig implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	private static final Map<String, DataSourceInfo>  datasources = new HashMap<String, DataSourceInfo>();
	
	private List<DataSourceInfo>  datasource;

	public List<DataSourceInfo> getDatasource() {
		return datasource;
	}

	public void setDatasource(List<DataSourceInfo> datasource) {
		this.datasource = datasource;
	}

	@PostConstruct
	private void init() {
		if(datasource != null && datasource.size() > 0) {
			for (DataSourceInfo ds : datasource) {
				datasources.put(ds.getName(), ds);
			}
		}
	}
	
	public static DataSourceInfo getDataSourceInfo(String name) {
		return datasources.get(name);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		DataSourceConfig.applicationContext = applicationContext;
	}
	
	public static Object getBean(String name) {
		if(applicationContext != null) {
			return applicationContext.getBean(name);
		}
        return null;
    }
}