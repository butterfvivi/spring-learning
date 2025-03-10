package org.vivi.framework.report.lucky.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vivi.framework.report.lucky.utils.SnowFlake;

import javax.sql.DataSource;


/**
 * 数据源配置
 * @author Administrator
 */
@Configuration
@Slf4j
public class DataSourceConfig {

//    @Bean(name = "mysqlDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.url")
//    public DataSource postgreDataSource(){
//        DataSource dataSource = DataSourceBuilder.create().type(DruidDataSource.class).build();
//        log.debug("数据源 mysql",dataSource);
//        return dataSource;
//    }

    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource getDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name="snowFlake")
    public SnowFlake getSnowFlake(){
        return new SnowFlake(1l,1l);
    }

}
