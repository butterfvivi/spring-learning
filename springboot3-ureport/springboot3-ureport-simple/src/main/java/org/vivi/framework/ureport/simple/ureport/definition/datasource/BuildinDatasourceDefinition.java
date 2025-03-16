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

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.JdbcUtils;

import org.vivi.framework.ureport.simple.common.config.DataSourceConfig;
import org.vivi.framework.ureport.simple.common.utils.MultipleJdbcTemplate;
import org.vivi.framework.ureport.simple.datasource.bean.DataSourceInfo;
import org.vivi.framework.ureport.simple.ureport.build.Dataset;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.DatasetDefinition;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.SqlDatasetDefinition;

/**
 * @author Jacky.gao
 * @since 2017年2月9日
 */
public class BuildinDatasourceDefinition implements DatasourceDefinition {

	private String name;

	private List<DatasetDefinition> datasets;

	public List<Dataset> buildDatasets(Map<String, Object> parameters) {
		if (datasets == null || datasets.size() == 0) {
			return Collections.emptyList();
		}
		DataSourceInfo info = DataSourceConfig.getDataSourceInfo(name);
		if (info == null) {
			return Collections.emptyList();
		}
		Connection conn = MultipleJdbcTemplate.buildConnection(info);
		if (conn == null) {
			return Collections.emptyList();
		}
		List<Dataset> list = new ArrayList<Dataset>();
		try {
			for (DatasetDefinition dsDef : datasets) {
				SqlDatasetDefinition sqlDataset = (SqlDatasetDefinition) dsDef;
				Dataset ds = sqlDataset.buildDataset(parameters, conn);
				list.add(ds);
			}
		} finally {
			JdbcUtils.closeConnection(conn);
		}
		return list;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public List<DatasetDefinition> getDatasets() {
		return datasets;
	}

	public void setDatasets(List<DatasetDefinition> datasets) {
		this.datasets = datasets;
	}

	@Override
	public DatasourceType getType() {
		return DatasourceType.buildin;
	}
}
