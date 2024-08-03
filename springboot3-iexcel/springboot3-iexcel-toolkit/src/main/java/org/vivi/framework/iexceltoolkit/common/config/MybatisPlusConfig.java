package org.vivi.framework.iexceltoolkit.common.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;

@MapperScan("org.vivi.framework.iexceltoolkit.mapper")
public class MybatisPlusConfig {

    @Bean
    public PaginationInnerInterceptor paginationInterceptor() {
        return new PaginationInnerInterceptor();
    }
}
