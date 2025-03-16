package org.vivi.framework.ureport.simple.datasource.bean;

/**
 * 数据源信息
 */
public class DataSourceInfo {

	/**
	 * 数据源类型  jdbc  buildin
	 */
	private String type;

	/**
	 * 数据源显示名称
	 */
	private String name;
	
	
	/**
	 * 数据库驱动
	 */
	private String driverClassName;

	/**
	 * 数据库链接
	 */
	private String url;

	/**
	 * 数据库用户名
	 */
	private String userName;

	/**
	 * 数据库密码
	 */
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
