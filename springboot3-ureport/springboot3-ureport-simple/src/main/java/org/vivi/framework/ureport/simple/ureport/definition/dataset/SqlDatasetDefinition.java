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
package org.vivi.framework.ureport.simple.ureport.definition.dataset;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import org.vivi.framework.ureport.simple.ureport.build.Dataset;
import org.vivi.framework.ureport.simple.ureport.definition.datasource.DataType;
import org.vivi.framework.ureport.simple.ureport.utils.ProcedureUtils;
import org.vivi.framework.ureport.simple.ureport.utils.ToolUtils;

/**
 * @author Jacky.gao
 * @since 2016年12月27日
 */
public class SqlDatasetDefinition implements DatasetDefinition {

	private static final long serialVersionUID = -1134526105416805870L;

	private String name;

	private String sql;

	private List<Parameter> parameters;
	
	private Object[] values;

	private List<Field> fields;

	public Dataset buildDataset(Map<String, Object> params, Connection conn) {
		long start = System.currentTimeMillis();
		Map<String, Object> pmap = buildParameters(params);
		String sqlForUse = parseSql(pmap);
		if (ProcedureUtils.isProcedure(sqlForUse)) {
			List<Map<String, Object>> result = ProcedureUtils.procedureQuery(sqlForUse, pmap, conn);
			return new Dataset(name, result);
		}
		ToolUtils.logToConsole("SQL参数:" + pmap);
		SingleConnectionDataSource datasource = new SingleConnectionDataSource(conn, false);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
		List<Map<String, Object>> list = jdbcTemplate.query(sqlForUse, values,new ResultSetExtractor<List<Map<String, Object>>>() {
			@Override
			public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
				rs.setFetchSize(512);
				while (rs.next()) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					int i = 1;
					for (Field field : fields) {
						Object value = JdbcUtils.getResultSetValue(rs, i);
						map.put(field.getName(), value);
						i++;
					}
					list.add(map);
				}
				return list;
			}
		});
		long end = System.currentTimeMillis();
		ToolUtils.logToConsole("SQL查询耗时:" + (end - start) + "ms");
		return new Dataset(name, list);
	}
	
	public Object[] getParams() {
		return values;
	}

	public String parseSql(Map<String, Object> params) {
		String sqlForUse = sql.trim();
		String xml = MessageFormat.format("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\"><mapper namespace=\"null\"><select id=\"selectDataSet\">{0}</select></mapper>",sqlForUse);
    	InputStream inputStream = IOUtils.toInputStream(xml, "UTF-8");
    	Configuration configuration = new Configuration();
    	XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, null, null);
    	mapperParser.parse();
    	IOUtils.closeQuietly(inputStream);
    	MappedStatement s = configuration.getMappedStatement("selectDataSet");
    	BoundSql boundSql = s.getBoundSql(params);
    	String sql = boundSql.getSql();
    	List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
    	if(parameterMappings != null && parameterMappings.size() > 0) {
    		values = new Object[parameterMappings.size()];
    		int index = 0;
    		for (ParameterMapping parameterMapping : parameterMappings) {
        		String property = parameterMapping.getProperty();
        		Object value = boundSql.getAdditionalParameter(property);
        		if(value == null) {
        			value = params.get(property);
        		}
        		values[index] = value;
        		index++;
    		}
    	}
    	ToolUtils.logToConsole("SQL:" + sql);
		return sql;
	}


	public Map<String, Object> buildParameters(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		for (Parameter param : parameters) {
			String name = param.getName();
			DataType datatype = param.getType();
			Object value = param.getDefaultValue();
			if (params != null && params.containsKey(name)) {
				value = params.get(name);
			}
			map.put(name, datatype.parse(value));
		}
		return map;
	}

	@Override
	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getSql() {
		return sql;
	}
	
	public boolean hasParameter(String key) {
		if(parameters != null &&  parameters.size() > 0) {
			for (Parameter p : parameters) {
				if(p.getName().equals(key)) {
					return true;
				}
			}
		}
		return false;
	}
}
