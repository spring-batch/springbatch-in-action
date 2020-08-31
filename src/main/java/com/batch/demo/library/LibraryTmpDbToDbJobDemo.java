package com.batch.demo.library;

import com.batch.domain.oracle.Library;
import com.batch.domain.oracle.LibraryTmp;
import com.batch.listener.CustomJobListener;
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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpDbToDbJobDemo {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final String JOB_NAME = "tmpLibraryDbToLibraryDbJob";

    /**
     * JDBC 용 DataSource
     */
    @Resource(name = "oracleDataSource")
    private DataSource oracleDataSource;

    @Bean
    public Job tmpLibraryDbToLibraryDbJob() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(tmpLibDbToLibDbStep())
                .listener(new CustomJobListener())
                .build();
    }

    @Bean
    public Step tmpLibDbToLibDbStep() {
        return stepBuilderFactory.get(JOB_NAME + "_step")
                .<LibraryTmp, Library>chunk(1000)
                .reader(tmpToLibReader())
                .writer(new ConsoleItemWriter<>())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<? extends LibraryTmp> tmpToLibReader() {
        return new JdbcPagingItemReader<LibraryTmp>() {{
            setFetchSize(1000);
            setDataSource(oracleDataSource);
            setQueryProvider(tmpQueryProvider());
            setRowMapper(new BeanPropertyRowMapper<>(LibraryTmp.class));
            setName(JOB_NAME + "_reader");

        }};
    }

    private OraclePagingQueryProvider tmpQueryProvider() {
        StringBuffer selectClause = new StringBuffer();

        selectClause.append(" B.CTPRVN_CODE, ");
        selectClause.append(" C.signgu_code ");
        for(String selectField : LibraryTmp.LibraryDBFields.getDBFieldArrays()) {
            if(selectClause.length() != 0) selectClause.append(", ");
            selectClause.append("A." + selectField);
        }

        StringBuffer fromClause = new StringBuffer();
        fromClause.append("FROM CSV_TABLE A ");
        fromClause.append("JOIN TB_SIDO B ");
        fromClause.append("ON a.ctprvn_nm = b.ctprvn_nm ");
        fromClause.append("JOIN TB_SIGNGU C ");
        fromClause.append("ON b.ctprvn_code = c.ctprvn_code ");

        System.out.println(selectClause.toString());
        System.out.println(fromClause.toString());

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("LBRRY_CODE", Order.DESCENDING);

        return new OraclePagingQueryProvider() {{
            setSelectClause(selectClause.toString());
            setFromClause(fromClause.toString());
            setSortKeys(sortKeys);
        }};
    }

    public JdbcBatchItemWriter<LibraryTmp> tmpToLbrryWriter() {
        return new JdbcBatchItemWriter<LibraryTmp>() {{
            setJdbcTemplate(new NamedParameterJdbcTemplate(oracleDataSource));
            setItemSqlParameterSourceProvider(BeanPropertySqlParameterSource::new);
            setSql("");
            afterPropertiesSet();
        }};
    }

}
