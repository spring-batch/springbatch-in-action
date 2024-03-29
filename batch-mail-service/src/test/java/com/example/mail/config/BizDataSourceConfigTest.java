package com.example.mail.config;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(BizDataSourceConfig.class)
class BizDataSourceConfigTest {

    @Autowired
    private Environment environment;

    @DisplayName("배치 테이블 DataSource 설정 조회 테스트")
    @Test
    void testCase1(
            @Qualifier(value = "bizDataSource") DataSource dataSource,
            @Value(value = "biz") String target
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