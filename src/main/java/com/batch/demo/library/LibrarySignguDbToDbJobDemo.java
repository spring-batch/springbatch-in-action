package com.batch.demo.library;

import com.batch.domain.region.Signgu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibrarySignguDbToDbJobDemo {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final String JOB_NAME = "signguDbToDbJob";

    /**
     * JDBC 용 DataSource
     */
    @Resource(name = "oracleDataSource")
    private DataSource oracleDataSource;

    @Bean
    public Job sidoDbToDbJob() throws Exception {
        return this.jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(signguDbToDbStep())
                .build();
    }

    @Bean
    public Step signguDbToDbStep() {
        return stepBuilderFactory.get(JOB_NAME + "_signgu_step")
                .<Signgu, Signgu>chunk(1000)
                .reader(extractLibraryToSigngu())
                .writer(signguWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<? extends Signgu> extractLibraryToSigngu() {
        return new JdbcPagingItemReader<Signgu>() {{
            setFetchSize(1000);
            setDataSource(oracleDataSource);
            setQueryProvider(libraryToSignguProvider());
            setRowMapper(new BeanPropertyRowMapper<>(Signgu.class));
            setName(JOB_NAME + "_signgu_reader");
        }};
    }

    private OraclePagingQueryProvider libraryToSignguProvider() {
        String selectClause =
                "    A.SIGNGU_NM,\n" +
                        "    A.SIDO_CD\n";
        String fromClause =
                "FROM (\n" +
                        "    SELECT\n" +
                        "        C.SIGNGU_NM,\n" +
                        "        SIDO.CTPRVN_CODE AS SIDO_CD\n" +
                        "    FROM CSV_TABLE C\n" +
                        "    JOIN TB_SIDO SIDO\n" +
                        "    ON C.CTPRVN_NM = SIDO.CTPRVN_NM\n" +
                        "    GROUP BY C.SIGNGU_NM, SIDO.CTPRVN_CODE\n" +
                        "    ORDER BY SIDO_CD ASC" +
                        ") A";

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("SIDO_CD", Order.ASCENDING);

        return new OraclePagingQueryProvider() {{
            setSelectClause(selectClause);
            setFromClause(fromClause);
            setSortKeys(sortKeys);
        }};
    }

    public JdbcBatchItemWriter<Signgu> signguWriter() {
        return new JdbcBatchItemWriter<Signgu>() {{
            setJdbcTemplate(new NamedParameterJdbcTemplate(oracleDataSource));
            setItemSqlParameterSourceProvider(BeanPropertySqlParameterSource::new);
            setSql("INSERT INTO TB_SIGNGU " +
                    "( SIGNGU_CODE, SIGNGU_NM, CTPRVN_CODE ) " +
                    "VALUES " +
                    "(TB_SIGNGU_SEQ.nextval, :signguNm, :sidoCd )");
            afterPropertiesSet();
        }};
    }

}
