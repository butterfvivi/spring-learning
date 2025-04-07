package org.vivi.framework.ureport.store.config;

import org.vivi.framework.ureport.store.core.definition.datasource.BuildinDatasource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DataSourceConfig implements BuildinDatasource {

    @Autowired
    private DataSource dataSource;

    /**
     * @return 返回数据源名称
     */
    @Override
    public String name() {
        return "数据源";
    }

    /**
     * @return 返回当前采用数据源的一个连接
     */
    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
