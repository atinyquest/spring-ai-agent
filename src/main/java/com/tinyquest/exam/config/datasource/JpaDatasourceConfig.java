package com.tinyquest.exam.config.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.tinyquest.exam.api.*.repository",
        entityManagerFactoryRef = "jpaEntityManagerFactory",
        transactionManagerRef = "sharedTransactionManager"
)
public class JpaDatasourceConfig {

//    @Bean(name = "jpaTransactionManager")
//    public JpaTransactionManager jpaTransactionManager(
//            @Qualifier("jpaEntityManagerFactory") EntityManagerFactory entityManagerFactory
//    ) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }

    @Bean(name = "jpaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean jpaEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("lazyRoutingDataSource") DataSource dataSource
    ) {
        return builder
                .dataSource(dataSource)
                .packages("com.tinyquest.exam.api.*.model.entity") // JPA 엔티티 패키지
                .build();
    }
}
