package com.batch.demo.library;

import com.batch.domain.region.Sido;
import com.batch.domain.region.Signgu;
import com.batch.writer.ConsoleItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibrarySidoDbToDbJobDemo {
    private static final String JOB_NAME = "SidoDbToDbJob";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * JDBC 용 DataSource
     */
    @Resource(name = "oracleDataSource")
    private DataSource oracleDataSource;

    @Bean
    public Job sidoDbToDbJob() throws Exception {
        return this.jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(sidoDbToDbStep())
                .next(signguDbToDbStep())
                .build();
    }

    @Bean
    public Step sidoDbToDbStep() throws Exception {
        return this.stepBuilderFactory.get(JOB_NAME + "_sido_step")
                .<Sido, Sido>chunk(1000)
                .reader(extractLibraryToSido())
                /* JdbcBatchItemWriter 로 구현한 버전 */
                .writer(sidoDbWriter())
                .build();
    }

    @Bean
    public Step signguDbToDbStep() {
        return stepBuilderFactory.get(JOB_NAME + "_signgu_step")
                .<Signgu, Signgu>chunk(1000)
                .reader(extractLibraryToSigngu())
                .writer(new ConsoleItemWriter<>())
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
                        "    A.CTPRVN_CODE AS SIDO_CD\n";
        String fromClause =
                "FROM (\n" +
                        "    SELECT\n" +
                        "        C.SIGNGU_NM,\n" +
                        "        SIDO.CTPRVN_CODE\n" +
                        "    FROM CSV_TABLE C\n" +
                        "    JOIN TB_SIDO SIDO\n" +
                        "    ON C.CTPRVN_NM = SIDO.CTPRVN_NM\n" +
                        "    GROUP BY C.SIGNGU_NM, SIDO.CTPRVN_CODE\n" +
                        ") A";

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("SIDO_CD", Order.ASCENDING);

        return new OraclePagingQueryProvider() {{
            setSelectClause(selectClause);
            setFromClause(fromClause);
            setSortKeys(sortKeys);
        }};
    }

    @Bean
    public JdbcPagingItemReader<? extends Sido> extractLibraryToSido() throws Exception {
        return new JdbcPagingItemReader<Sido>() {{
            setFetchSize(1000);
            setDataSource(oracleDataSource);
            setQueryProvider(libraryToSidoProvider());
            setRowMapper(new BeanPropertyRowMapper<>(Sido.class));
            setName(JOB_NAME + "_sido_reader");
        }};
    }

    private OraclePagingQueryProvider libraryToSidoProvider() {
        String selectClause =
                "DECODE(CTPRVN_NM, '서울특별시', 11,\n" +
                "                        '세종특별자치시', 25,\n" +
                "                        '부산광역시', 26,\n" +
                "                        '대구광역시', 27,\n" +
                "                        '인천광역시', 28,\n" +
                "                        '광주광역시', 29,\n" +
                "                        '대전광역시', 30,\n" +
                "                        '울산광역시', 31,\n" +
                "                        '경기도', 41,\n" +
                "                        '강원도', 42,\n" +
                "                        '충청북도', 43,\n" +
                "                        '충청남도', 44,\n" +
                "                        '전라북도', 45,\n" +
                "                        '전라남도', 46,\n" +
                "                        '경상북도', 47,\n" +
                "                        '경상남도', 48,\n" +
                "                        '제주도', 49,\n" +
                "                        '제주특별자치도', 50) AS CTPRVN_CD,\n" +
                "    CTPRVN_NM";
        String fromClause = "FROM CSV_TABLE";
        String groupByClause = "GROUP BY CTPRVN_NM";

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("CTPRVN_CD", Order.ASCENDING);

        return new OraclePagingQueryProvider() {{
            setSelectClause(selectClause);
            setFromClause(fromClause);
            setGroupClause(groupByClause);
            setSortKeys(sortKeys);
        }};
    }

    @Bean
    public JdbcBatchItemWriter<Sido> sidoDbWriter() {
        String insertQuery =
                "INSERT INTO TB_SIDO ( \n" +
                "    CTPRVN_CODE,\n" +
                "    CTPRVN_NM\n" +
                ") VALUES (" +
                "   :ctprvnCd," +
                "   :ctprvnNm" +
                ")";

        return new JdbcBatchItemWriter<Sido>() {{
            setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
            setDataSource(oracleDataSource);
            setSql(insertQuery);
        }};
    }
}
