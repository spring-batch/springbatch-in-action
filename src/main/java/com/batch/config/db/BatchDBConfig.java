package com.batch.config.db;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class BatchDBConfig {

    /* Spring Batch가 실행되기 위한 DataSource 우선순위 설정 */
    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.batch")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    /* MyBatis 사용을 위한 */
    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        /* Mapper 경로 설정 */
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mappers/batch/*.xml")
                //applicationContext.getResources("classpath:/resources/main/mapper/batch/*.xml")
        );
        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
//
//    @Primary
//    @Bean(name = "entityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("dataSource") DataSource dataSource) {
//        return builder
//                .dataSource(dataSource)
//                .packages("com.batch.domain.batch")
//                .persistenceUnit("batch")
//                .build();
//    }
//
//    @Primary
//    @Bean(name = "transactionManager")
//    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
}
