package org.vivi.framework.lucky.simple.common.datasource;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 * 开启注解事务管理，等同于xml配置文件中的 <tx:annotation-driven />
 */
@EnableTransactionManagement
@Configuration
public class ProfiledemoApplication implements TransactionManagementConfigurer {

    /**
     * postgre数据源
     */
    @Resource(name = "mysqlDataSource")
    private DataSource dataSource;


    @Bean(name = "mysqlTxManager")
    public PlatformTransactionManager mysqlTxManager() {
        return new DataSourceTransactionManager(dataSource);
    }


    @Resource(name="mysqlTxManager")
    private PlatformTransactionManager txManager;

    /**
     * 实现接口 TransactionManagementConfigurer 方法，其返回值代表在拥有多个事务管理器的情况下默认使用的事务管理器
     * @return
     */
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return txManager;
    }
}
