package com.tinyquest.exam.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;


@Slf4j
@Configuration
@MapperScan(
        basePackages = {
                "com.tinyquest.exam.api.*.mapper"
        },
        sqlSessionFactoryRef = "mybatisSqlSessionFactory"
)
public class MybatisDataSourceConfig {

//    @Bean(name = "mybatisTransactionManager")
//    public DataSourceTransactionManager mybatisTransactionManager(
//            @Qualifier("lazyRoutingDataSource") DataSource dataSource
//    ) {
//        return new DataSourceTransactionManager(dataSource);
//    }

//    @Bean(name = "mybatisTransactionManager")
//    public PlatformTransactionManager mybatisTransactionManager(
//            @Qualifier("sharedTransactionManager") PlatformTransactionManager transactionManager) {
//        return transactionManager;
//    }

    @Bean(name = "mybatisSqlSessionFactory")
    public SqlSessionFactory mybatisSqlSessionFactory(
            @Qualifier("lazyRoutingDataSource") DataSource dataSource
    ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*/*.xml"));
        sqlSessionFactoryBean.setConfigurationProperties(getMybatisProperties());

        return sqlSessionFactoryBean.getObject();
    }

    private Properties getMybatisProperties() {
        Properties properties = new Properties();
        // DB 컬럼명 (snake_case) ↔ Java 필드명 (camelCase) 자동 변환
        properties.setProperty("mapUnderscoreToCamelCase", "true");
        // NULL 값을 포함하여 setter 메서드 호출 허용
        properties.setProperty("callSettersOnNulls", "true");
        // NULL 값에 대한 JDBC 타입 설정 (예: `JdbcType.NULL`을 명시적으로 설정)
        properties.setProperty("jdbcTypeForNull", JdbcType.NULL.toString());
        return properties;
    }

}
