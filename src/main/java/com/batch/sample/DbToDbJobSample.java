package com.batch.sample;

import com.batch.domain.oracle.LibraryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.annotation.Resource;
import javax.sql.DataSource;
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
public class DbToDbJobSample {

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
     * @return
     */
    @Bean
    public Step dbToDbStep() throws Exception {
        return stepBuilderFactory.get(JOB_NAME + "_Step")
                .<LibraryEntity, LibraryEntity>chunk(1000)
                .reader(dbToDbReader())
                .writer(dbWriter())
                .build();
    }

    /**
     *
     * @return
     * @throws Exception
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

    private OraclePagingQueryProvider dbToDbProvider() throws Exception {

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

    public ItemWriter<LibraryEntity> dbWriter() {
        return new ItemWriter<LibraryEntity>() {
            @Override
            public void write(List<? extends LibraryEntity> items) throws Exception {
                for (LibraryEntity item : items) {
                    System.out.println(item);
                }
            }
        };
    }


}
