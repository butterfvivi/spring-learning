package org.vivi.framework.ureport.simple.datasource.service.impl;

import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.PagerUtils;

import org.vivi.framework.ureport.simple.common.config.DataSourceConfig;
import org.vivi.framework.ureport.simple.common.exception.ReportDesignException;
import org.vivi.framework.ureport.simple.common.utils.MultipleJdbcTemplate;
import org.vivi.framework.ureport.simple.datasource.bean.DataSourceInfo;
import org.vivi.framework.ureport.simple.datasource.bean.PreviewParams;
import org.vivi.framework.ureport.simple.datasource.service.DataSourceService;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.Field;
import org.vivi.framework.ureport.simple.ureport.definition.dataset.SqlDatasetDefinition;
import org.vivi.framework.ureport.simple.ureport.utils.ProcedureUtils;

@Service
public class DataSourceServiceImpl implements DataSourceService {

	@Resource
	private DataSourceConfig dataSourceConfig;
	
	@Override
	public List<Map<String, String>> selectBuildinDruidList() {
		List<Map<String,String>> druids = new ArrayList<Map<String,String>>();
		List<DataSourceInfo> list = dataSourceConfig.getDatasource();
		if(list != null && list.size() > 0) {
			for (DataSourceInfo info : list) {
				Map<String,String> map = new HashMap<String, String>();
				map.put("name", info.getName());
				druids.add(map);
			}
		}
		return druids;
	}
	
	@Override
	public List<Map<String, String>> selectTableList(DataSourceInfo info) {
		List<Map<String,String>> tables = new ArrayList<Map<String,String>>();
		Connection conn = null;
		ResultSet rs = null;
		try {
			String username = info.getUserName();
			String password = info.getPassword();
			String driver = info.getDriverClassName();
			String url = info.getUrl();
			// 内置jdbc查询
			if(StringUtils.isBlank(driver) || StringUtils.isBlank(url) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
				info = DataSourceConfig.getDataSourceInfo(info.getName());
			}
			conn = MultipleJdbcTemplate.buildConnection(info);
			DatabaseMetaData metaData = conn.getMetaData();
			String dbName = conn.getCatalog();
			String schema = conn.getSchema();
			rs = metaData.getTables(dbName, schema, "%", new String[] { "TABLE", "VIEW" });
			while (rs.next()) {
				Map<String,String> table = new HashMap<String, String>();
				table.put("name",rs.getString("TABLE_NAME"));
				table.put("type",rs.getString("TABLE_TYPE"));
				tables.add(table);
			}
			
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeConnection(conn);
		}
		return tables;
	}

	@Override
	public List<Field> selectFieldList(PreviewParams previewParams) {
		DataSourceInfo info = previewParams.getInfo();
		String username = info.getUserName();
		String password = info.getPassword();
		String driver = info.getDriverClassName();
		String url = info.getUrl();
		// 内置jdbc查询
		if(StringUtils.isBlank(driver) || StringUtils.isBlank(url) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			info = DataSourceConfig.getDataSourceInfo(info.getName());
		}
		Connection conn = null;
		try {
			conn = MultipleJdbcTemplate.buildConnection(info);
			
			SqlDatasetDefinition sqlDatasetDefinition = new SqlDatasetDefinition();
			sqlDatasetDefinition.setSql(previewParams.getSql());
			sqlDatasetDefinition.setParameters(previewParams.getParameters());
			
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, Object> pmap = sqlDatasetDefinition.buildParameters(params);
			String sqlForUse = sqlDatasetDefinition.parseSql(pmap);
			if (ProcedureUtils.isProcedure(sqlForUse)) {
				return ProcedureUtils.procedureColumnsQuery(sqlForUse, pmap, conn);
			}
			DataSource dataSource = new SingleConnectionDataSource(conn, false);
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			final List<Field> fields = new ArrayList<Field>();
			Object[] args = sqlDatasetDefinition.getParams();
			String dbType = com.alibaba.druid.util.JdbcUtils.getDbType(info.getUrl(), info.getDriverClassName());
			if(PagerUtils.getLimit(sqlForUse, DbType.valueOf(dbType)) == -1) {
				sqlForUse = PagerUtils.limit(sqlForUse, DbType.valueOf(dbType), 0, 1);
			}
			jdbcTemplate.query(sqlForUse, args, new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					ResultSetMetaData metadata = (ResultSetMetaData) rs.getMetaData();
					List<String> metadataList = new ArrayList<String>();
					for (int i = 1; i <= metadata.getColumnCount(); i++) {
						String columnName = metadata.getColumnLabel(i);
						metadataList.add(columnName);
						fields.add(new Field(columnName));
					}
					return null;
				}
			});
			return fields;
			
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			JdbcUtils.closeConnection(conn);
		}
	}

	@Override
	public Map<String, Object> previewData(PreviewParams previewParams) {
		Map<String, Object> data = new HashMap<String, Object>();
		DataSourceInfo info = previewParams.getInfo();
		String username = info.getUserName();
		String password = info.getPassword();
		String driver = info.getDriverClassName();
		String url = info.getUrl();
		// 内置jdbc查询
		if(StringUtils.isBlank(driver) || StringUtils.isBlank(url) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			info = DataSourceConfig.getDataSourceInfo(info.getName());
		}
		Connection conn = null;
		try {
			conn = MultipleJdbcTemplate.buildConnection(info);
			
			SqlDatasetDefinition sqlDatasetDefinition = new SqlDatasetDefinition();
			sqlDatasetDefinition.setSql(previewParams.getSql());
			sqlDatasetDefinition.setParameters(previewParams.getParameters());
			
			Map<String, Object> params = new HashMap<String, Object>();
			Map<String, Object> pmap = sqlDatasetDefinition.buildParameters(params);
			String sqlForUse = sqlDatasetDefinition.parseSql(pmap);
			
			data.put("sql", sqlForUse);
			int row = 100;
			if (ProcedureUtils.isProcedure(sqlForUse)) {
				return ProcedureUtils.procedureQuery(sqlForUse, pmap, conn, row);
			} else {
				Object[] args = sqlDatasetDefinition.getParams();
				List<String> fieldList = new LinkedList<>();
				data.put("fields", fieldList);
				DataSource dataSource = new SingleConnectionDataSource(conn, false);
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				String dbType = com.alibaba.druid.util.JdbcUtils.getDbType(info.getUrl(), info.getDriverClassName());
				if(PagerUtils.getLimit(sqlForUse, DbType.valueOf(dbType)) == -1) {
					sqlForUse = PagerUtils.limit(sqlForUse, DbType.valueOf(dbType), 0, 100);
				}
				List<Map<String, Object>> list = jdbcTemplate.query(sqlForUse, args,new ResultSetExtractor<List<Map<String, Object>>>() {
					@Override
					public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
						ResultSetMetaData metadata = rs.getMetaData();
						int count = metadata.getColumnCount();
						for (int i = 1; i <= count; i++) {
							String columnName = metadata.getColumnLabel(i);
							fieldList.add(columnName);
						}
						int index = 0;
						while (rs.next() && index < row) {
							int initialCapacity = (int) ((float) fieldList.size() / 0.75F + 1.0F);
							HashMap<String, Object> map = new HashMap<String, Object>(initialCapacity);
							int i = 1;
							for (String field : fieldList) {
								Object value = JdbcUtils.getResultSetValue(rs, i);
								map.put(field, value);
								i++;
							}
							index++;
							list.add(map);
						}
						return list;
					}
				});
				data.put("data", list);
			}
			
		} catch (Exception ex) {
			String message = ex.getMessage();
			if(message.contains(":")) {
				data.put("message",message.substring(message.indexOf(":")));
			} else {
				data.put("message",message);
			}
		} finally {
			JdbcUtils.closeConnection(conn);
		}
		return data;
	}

	@Override
	public List<Field> getSpringBeanFieldList(String clazz) {
		List<Field> result = new ArrayList<Field>();
		try {
			Class<?> targetClass = Class.forName(clazz);
			PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(targetClass);
			for(PropertyDescriptor pd:propertyDescriptors){
				String name = pd.getName();
				if("class".equals(name)){
					continue;
				}
				result.add(new Field(name));
			}
			return result;
			
		}catch(Exception ex){
			throw new ReportDesignException(ex);
		}
	}
}
