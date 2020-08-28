package com.batch.demo.library;

import com.batch.domain.oracle.LibraryEntity;
import com.batch.writer.ConsoleItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 간단하게 Batch 사용하기 <br />
 * 1. Job
 * 2. Step
 * 3. JdbcPagingItemReader
 *  - Oracle DB Reader
 * 4. JdbcBatchItemWriter
 *  - ?
 *  </pre>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryDbToMultiDbJobDemo {

    private static final String JOB_NAME = "dbToDbJob";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * JDBC 용 DataSource
     */
    @Resource(name = "oracleDataSource")
    private DataSource oracleDataSource;

    @Bean
    public Job dbToDbJob() throws Exception {
        return this.jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(dbToDbStep())
                .build();
    }

    /**
     *
     */
    @Bean
    public Step dbToDbStep() throws Exception {
        return stepBuilderFactory.get(JOB_NAME + "_Step")
                .<LibraryEntity, LibraryEntity>chunk(1000)
                .reader(dbToDbReader())
                .writer(new ConsoleItemWriter<>())
                .build();
    }

    /**
     *
     */
    @Bean
    public JdbcPagingItemReader<? extends LibraryEntity> dbToDbReader() throws Exception {
        return new JdbcPagingItemReader<LibraryEntity>() {{
            setFetchSize(1000);
            setDataSource(oracleDataSource);
            setQueryProvider(dbToDbProvider());
            setRowMapper(new BeanPropertyRowMapper<>(LibraryEntity.class));
            setName(JOB_NAME + "_READER");
        }};
    }

    private OraclePagingQueryProvider dbToDbProvider() {

        StringBuffer selectClause = new StringBuffer();
        StringBuffer fromClause = new StringBuffer();

        selectClause.append("	LBRRY_CODE,");
        selectClause.append("	LBRRY_NM,");
        selectClause.append("	CTPRVN_NM,");
        selectClause.append("	SIGNGU_NM,");
        selectClause.append("	LBRRY_SE,");
        selectClause.append("	CLOSE_DAY,");
        selectClause.append("	WEEKDAY_OPER_OPEN_HHMM,");
        selectClause.append("	WEEKDAY_OPER_CLOSE_HHMM,");
        selectClause.append("	SAT_OPER_OPEN_HHMM,");
        selectClause.append("	SAT_OPER_CLOSE_HHMM,");
        selectClause.append("	HOLIDAY_OPER_OPEN_HHMM,");
        selectClause.append("	HOLIDAY_OPER_CLOSE_HHMM,");
        selectClause.append("	SEAT_CO,");
        selectClause.append("	BOOK_CO,");
        selectClause.append("	PBLICTN_CO,");
        selectClause.append("	NONEBOOK_CO,");
        selectClause.append("	LON_CO,");
        selectClause.append("	LONDAY_CNT,");
        selectClause.append("	RDNM_ADR,");
        selectClause.append("	OPERINSTITUTION_NM,");
        selectClause.append("	LBRRY_PHONENUMBER,");
        selectClause.append("	PLOT_AR,");
        selectClause.append("	BULD_AR,");
        selectClause.append("	HOMEPAGEURL,");
        selectClause.append("	LATITUDE,");
        selectClause.append("	LONGITUDE,");
        selectClause.append("	REFERENCE_DATE,");
        selectClause.append("	INSTT_CODE,");
        selectClause.append("	INSTT_NM");

        fromClause.append("FROM CSV_TABLE ");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("lbrry_code", Order.DESCENDING);

        return new OraclePagingQueryProvider() {{
            setSelectClause(selectClause.toString());
            setFromClause(fromClause.toString());
            setSortKeys(sortKeys);
        }};
    }

    public JdbcBatchItemWriter<LibraryEntity> sidoWriter() {
        return new JdbcBatchItemWriter<LibraryEntity>() {{
            setJdbcTemplate(new NamedParameterJdbcTemplate(oracleDataSource));
            setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

            setSql("\n" +
                    "INSERT INTO TB_SIDO ( \n" +
                    "CTPRVN_CODE,\n" +
                    "CTPRVN_NM\n" +
                    ")\n" +
                    "SELECT    \n" +
                    "    DECODE(CTPRVN_NM, '서울특별시', 11,\n" +
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
                    "                        '제주특별자치도', 50) AS CTPRVN_CODE,\n" +
                    "    CTPRVN_NM\n" +
                    "FROM CSV_TABLE\n" +
                    "GROUP BY CTPRVN_NM");
            afterPropertiesSet();
        }};
    }

    public JdbcBatchItemWriter<LibraryEntity> signguWriter() {
        return new JdbcBatchItemWriter<LibraryEntity>() {{
            setJdbcTemplate(new NamedParameterJdbcTemplate(oracleDataSource));

            setSql("INSERT INTO TB_SIGNGU (\n" +
                    "    SIGNGU_CODE\n" +
                    "    , SIGNGU_NM\n" +
                    "    , CTPRVN_CODE\n" +
                    ") \n" +
                    "SELECT \n" +
                    "    TB_SIGNGU_SEQ.nextval,\n" +
                    "    A.SIGNGU_NM,\n" +
                    "    A.CTPRVN_CODE\n" +
                    "FROM (\n" +
                    "    SELECT\n" +
                    "        C.SIGNGU_NM,\n" +
                    "        SIDO.CTPRVN_CODE\n" +
                    "    FROM CSV_TABLE C\n" +
                    "    JOIN TB_SIDO SIDO\n" +
                    "    ON C.CTPRVN_NM = SIDO.CTPRVN_NM\n" +
                    "    GROUP BY C.SIGNGU_NM, SIDO.CTPRVN_CODE\n" +
                    ") A");
            afterPropertiesSet();
        }};
    }

    public JdbcBatchItemWriter<LibraryEntity> eupMyeonDongWriter() {
        return new JdbcBatchItemWriter<LibraryEntity>() {{
            setJdbcTemplate(new NamedParameterJdbcTemplate(oracleDataSource));
            setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

            setSql("");
            afterPropertiesSet();
        }};
    }

    /**
     * CompositeItemWriter 설정
     */
    public CompositeItemWriter<LibraryEntity> multiDbWriter() throws Exception {
        List<ItemWriter<? super LibraryEntity>> writers = new ArrayList<>();
        writers.add(sidoWriter());
        writers.add(signguWriter());
//        writers.add(eupMyeonDongWriter());

        return new CompositeItemWriter<LibraryEntity>() {{
            setDelegates(writers);
            afterPropertiesSet();
        }};
    }
}
