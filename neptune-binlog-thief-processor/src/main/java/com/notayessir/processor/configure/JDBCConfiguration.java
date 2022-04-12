package com.notayessir.processor.configure;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;


/**
 * hikari、spring-jdbc 配置
 */
@Configuration
@EnableJdbcRepositories(basePackages = "com.notayessir.processor.database.repository")
class JDBCConfiguration extends AbstractJdbcConfiguration {



//    @Bean
//    @ConfigurationProperties(prefix="app.datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create()
//                .type(HikariDataSource.class).build();
//    }


//    @Bean
//    public NamedParameterJdbcOperations namedParameterJdbcOperations(DataSource dataSource) {
//        return new NamedParameterJdbcTemplate(dataSource);
//    }




}
