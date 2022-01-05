package com.example.mail.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "bizEntityManagerFactory",
        transactionManagerRef = "bizTransactionManager",
        basePackages = {"com.example.mail.domain.repository"}
)
public class BizDataSourceConfig {

    @Bean(value = "bizDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.biz")
    public DataSource bizDataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(value = "bizEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean bizEntityManagerFactory(
            EntityManagerFactoryBuilder managerFactoryBuilder,
            @Qualifier(value = "bizDataSource") DataSource dataSource
    ) {
        return managerFactoryBuilder.dataSource(dataSource)
                .packages("com.example.mail.domain")
                .persistenceUnit("biz")
                .build();
    }

    @Bean(value = "bizTransactionManager")
    public PlatformTransactionManager bizTransactionManager(
            @Qualifier(value = "bizEntityManagerFactory") EntityManagerFactory entityManagerFactory
    ) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
