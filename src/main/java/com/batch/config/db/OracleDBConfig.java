package com.batch.config.db;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.ApplicationContext;
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
/* Jpa 구성 시 Repository package 경로 */
@EnableJpaRepositories(
        basePackages = {""}
)
public class OracleDBConfig {

    @Bean(name = "oracleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.oracle")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    /* Mybatis Oracle SessionFactory */
    @Bean(name = "oracleSqlSessionFactory")
    public SqlSessionFactory sessionFactory(@Qualifier("oracleDataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        /* Mapper 경로 설정 */
        sqlSessionFactoryBean.setMapperLocations(
                applicationContext.getResources("classPath:mappers/oracle/*.xml")
        );
        return sqlSessionFactoryBean.getObject();
    }

    /* Mybatis Oracle SessionTemplate */
    @Bean(name = "oracleSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("oracleSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "oracleEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("oracleDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.batch.domain.oracle")
                .persistenceUnit("oracle")
                .build();
    }

    @Bean(name = "oracleTransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("oracleEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
