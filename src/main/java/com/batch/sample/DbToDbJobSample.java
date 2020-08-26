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
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 간단하게 Batch 사용하기
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DbToDbJobSample {

    private static final String JOB_NAME = "dbToDbJob";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Resource(name = "oracleDataSource")
    private DataSource oracleDataSource;

    @Bean
    public Job fileToDbJob() throws Exception {
        return this.jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(dbToDbStep())
                .build();
    }

    private Step dbToDbStep() throws Exception {
        return stepBuilderFactory.get(JOB_NAME + "_Step")
                .<LibraryEntity, Map<String, Object>>chunk(1000)
                .reader(dbReader())
                .writer(dbWriter())
                .build();
    }

    private JdbcPagingItemReader<? extends LibraryEntity> dbReader() throws Exception {
        return new JdbcPagingItemReader<LibraryEntity>() {{
            setDataSource(oracleDataSource);
            setName(JOB_NAME + "JdbcPagingItemReader");
            setQueryProvider(new OraclePagingQueryProvider() {{

            }});
            setFetchSize(1000);
            afterPropertiesSet();
        }};
    }

    private ItemWriter<? super Map<String, Object>> dbWriter() {
        return new ItemWriter<Map<String, Object>>() {
            @Override
            public void write(List<? extends Map<String, Object>> items) throws Exception {
                for(Map<String, Object> item : items) {
                    System.out.println(item);
                }
            }
        };
    }


}
