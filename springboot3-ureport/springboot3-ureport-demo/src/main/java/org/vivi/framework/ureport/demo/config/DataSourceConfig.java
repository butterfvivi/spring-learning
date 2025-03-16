package org.vivi.framework.ureport.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.vivi.framework.ureport.demo.core.definition.datasource.BuildinDatasource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * ureport2内置数据源配置
 *
 * @author wuxc
 * @date 2021/4/2 14:56
 */
@Configuration
public class DataSourceConfig implements BuildinDatasource {

    @Autowired
    private DataSource dataSource;

    @Override
    public String name() {
        return "wms-db";
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}