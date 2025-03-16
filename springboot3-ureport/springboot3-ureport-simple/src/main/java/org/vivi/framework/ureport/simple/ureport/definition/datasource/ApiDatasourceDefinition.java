/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vivi.framework.ureport.simple.ureport.definition.datasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import org.vivi.framework.ureport.simple.common.config.PropertiesConfig;
import org.vivi.framework.ureport.simple.ureport.build.Dataset;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.ApiDatasetDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.DatasetDefinition;

/**
 * @author Jacky.gao
 * @since 2017年2月9日
 */
public class ApiDatasourceDefinition implements DatasourceDefinition {

	private String name;

	private String url;

	private List<Map<String, String>> headers;
	
	private List<DatasetDefinition> datasets;
	
	private String token;

	public List<Dataset> buildDatasets(Map<String, Object> params) {
		if (datasets == null || datasets.size() == 0) {
			return Collections.emptyList();
		}
		//设置请求头参数
		HttpHeaders httpHeaders = new HttpHeaders();
		for (Map<String, String> map : headers) {
			httpHeaders.add(map.get("name"), map.get("value"));
		}
		// 以json方式提交
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		httpHeaders.setContentType(type);
		httpHeaders.add(PropertiesConfig.getTokenKey(), token);
		List<Dataset> list = new ArrayList<Dataset>();
		for (DatasetDefinition dsDef : datasets) {
			ApiDatasetDefinition beanDef = (ApiDatasetDefinition) dsDef;
			Dataset ds = beanDef.buildDataset(name, url, httpHeaders, params);
			list.add(ds);
		}
		return list;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<DatasetDefinition> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DatasetDefinition> datasets) {
		this.datasets = datasets;
	}

	public List<Map<String, String>> getHeaders() {
		return headers;
	}

	public void setHeaders(List<Map<String, String>> headers) {
		this.headers = headers;
	}

	public String getToken() {
		return token;
	}








	public void setToken(String token) {
		this.token = token;
	}








	@Override
	public DatasourceType getType() {
		return DatasourceType.api;
	}
}
