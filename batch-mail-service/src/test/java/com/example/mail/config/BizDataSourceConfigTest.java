package com.example.mail.config;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        MetaDataSourceConfig.class,
        HikariDataSource.class
})
@ActiveProfiles(value = "application-db.yml")
class BizDataSourceConfigTest {

    @Autowired
    private Environment environment;

    @DisplayName("배치 테이블 DataSource 설정 조회 테스트")
    @Test
    void testCase1(
            @Qualifier(value = "metaDataSource") DataSource dataSource,
            @Value(value = "meta") String target
    ) {
        HikariDataSource source = (HikariDataSource) dataSource;
        checkDataSourceSettings(source, target);
    }

    private void checkDataSourceSettings(HikariDataSource source, String target) {
        String dataSourcePrefix = "spring.datasource.hikari.";
        String propDriverClassName = environment.getProperty(dataSourcePrefix + target + ".driver-class-name");
        String propJdbcUrl = environment.getProperty(dataSourcePrefix + target + ".jdbc-url");

        assertThat(source.getDriverClassName()).isEqualTo(propDriverClassName);
        assertThat(source.getJdbcUrl()).isEqualTo(propJdbcUrl);
    }
}