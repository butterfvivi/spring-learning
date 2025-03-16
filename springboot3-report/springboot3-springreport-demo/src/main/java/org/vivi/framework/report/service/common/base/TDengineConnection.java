package org.vivi.framework.report.service.common.base;

import lombok.Data;

import java.sql.Connection;
import java.sql.DriverManager;

@Data
public class TDengineConnection {
	// 用户名
	private String username;
	// 密码
	private String password;
	// 连接地址
	private String jdbcUrl;
	/**  
	 * @Fields connection : 数据库连接
	 */
	private Connection connection;
	
	public TDengineConnection(String url, String username, String password) throws Exception {
		this.jdbcUrl = url;
		this.username = username;
		this.password = password;
		buildConnection();
	}
	
	public void buildConnection() throws Exception {
		if(connection == null || connection.isClosed())
		{
			 Class.forName("com.taosdata.jdbc.TSDBDriver");
			 Connection conn = DriverManager.getConnection(this.jdbcUrl,this.username,this.password);
			 this.connection = conn;
		}
	}
}
