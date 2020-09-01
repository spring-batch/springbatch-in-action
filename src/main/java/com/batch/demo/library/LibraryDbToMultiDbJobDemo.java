package com.batch.demo.library;

import com.batch.domain.oracle.LibraryTmp;
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

    public Job dbToDbJob() throws Exception {
        return this.jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(dbToDbStep())
                .build();
    }

    /**
     *
     */
    public Step dbToDbStep() throws Exception {
        return stepBuilderFactory.get(JOB_NAME + "_Step")
                .<LibraryTmp, LibraryTmp>chunk(1000)
                .reader(dbToDbReader())
                .writer(new ConsoleItemWriter<>())
                .build();
    }

    /**
     *
     */
    public JdbcPagingItemReader<? extends LibraryTmp> dbToDbReader() throws Exception {
        return new JdbcPagingItemReader<LibraryTmp>() {{
            setFetchSize(1000);
            setDataSource(oracleDataSource);
            setQueryProvider(dbToDbProvider());
            setRowMapper(new BeanPropertyRowMapper<>(LibraryTmp.class));
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

    public JdbcBatchItemWriter<LibraryTmp> sidoMultiWriter() {
        return new JdbcBatchItemWriter<LibraryTmp>() {{
            setJdbcTemplate(new NamedParameterJdbcTemplate(oracleDataSource));
            setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

            setSql("");
            afterPropertiesSet();
        }};
    }

    public JdbcBatchItemWriter<LibraryTmp> signguMultiWriter() {
        return new JdbcBatchItemWriter<LibraryTmp>() {{
            setJdbcTemplate(new NamedParameterJdbcTemplate(oracleDataSource));

            setSql("");
            afterPropertiesSet();
        }};
    }

    public JdbcBatchItemWriter<LibraryTmp> eupMyeonDongMultiWriter() {
        return new JdbcBatchItemWriter<LibraryTmp>() {{
            setJdbcTemplate(new NamedParameterJdbcTemplate(oracleDataSource));
            setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());

            setSql("");
            afterPropertiesSet();
        }};
    }

    /**
     * CompositeItemWriter 설정
     */
    public CompositeItemWriter<LibraryTmp> multiDbWriter() throws Exception {
        List<ItemWriter<? super LibraryTmp>> writers = new ArrayList<>();
        writers.add(sidoMultiWriter());
        writers.add(signguMultiWriter());
//        writers.add(eupMyeonDongWriter());

        return new CompositeItemWriter<LibraryTmp>() {{
            setDelegates(writers);
            afterPropertiesSet();
        }};
    }
}
