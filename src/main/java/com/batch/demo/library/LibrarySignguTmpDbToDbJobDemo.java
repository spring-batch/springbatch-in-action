package com.batch.demo.library;

import com.batch.domain.region.Signgu;
import com.batch.writer.ConsoleItemWriter;
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

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibrarySignguTmpDbToDbJobDemo {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final String JOB_NAME = "signguDbToDbJob";

    /**
     * JDBC 용 DataSource
     */
    @Resource(name = "oracleDataSource")
    private DataSource oracleDataSource;

    @Bean
    public Job signguDbToDbJob() throws Exception {
        return this.jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(signguDbToDbStep())
                .build();
    }

    @Bean
    public Step signguDbToDbStep() throws Exception {
        return stepBuilderFactory.get(JOB_NAME + "_signgu_step")
                .<Signgu, Signgu>chunk(100)
                .reader(libraryReader())
                .writer(new ConsoleItemWriter<>())
//                .listener(new CustomItemReadListener())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<? extends Signgu> libraryReader() throws Exception {
        return new JdbcPagingItemReader<Signgu>() {{
            setFetchSize(100);
            setDataSource(oracleDataSource);
            setQueryProvider(libraryQueryProvider());
            setRowMapper(new BeanPropertyRowMapper<>(Signgu.class));
            setName(JOB_NAME + "_signgu_reader");
            afterPropertiesSet();
        }};
    }

    private OraclePagingQueryProvider libraryQueryProvider() {
        String selectClause =
                "    A.CTPRVN_CODE,\n" +
                "    A.SIGNGU_NM\n";
        String fromClause =
                "FROM (\n" +
                "    SELECT\n" +
                "        SIDO.CTPRVN_CODE,\n" +
                "        C.SIGNGU_NM\n" +
                "    FROM CSV_TABLE C\n" +
                "    JOIN TB_SIDO SIDO\n" +
                "    ON C.CTPRVN_NM = SIDO.CTPRVN_NM\n" +
                "    GROUP BY SIDO.CTPRVN_CODE, C.SIGNGU_NM \n" +
                "    ORDER BY CTPRVN_CODE ASC" +
                ") A";

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("CTPRVN_CODE", Order.ASCENDING);

        return new OraclePagingQueryProvider() {{
            setSelectClause(selectClause);
            setFromClause(fromClause);
            setSortKeys(sortKeys);
        }};
    }

    public JdbcBatchItemWriter<Signgu> signguWriter() {
        return new JdbcBatchItemWriter<Signgu>() {{
            setDataSource(oracleDataSource);
            setItemSqlParameterSourceProvider(BeanPropertySqlParameterSource::new);
            setSql("INSERT INTO TB_SIGNGU " +
                    "( SIGNGU_CODE, SIGNGU_NM, CTPRVN_CODE ) " +
                    "VALUES " +
                    "(TB_SIGNGU_SEQ.nextval, :signguNm, :ctprvnCode )");
            afterPropertiesSet();
        }};
    }


}
