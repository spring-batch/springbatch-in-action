package com.batch.demo.library;

import com.batch.demo.library.domain.LibraryTmpEntity;
import com.batch.demo.library.domain.Sido;
import com.batch.demo.library.domain.Signgu;
import com.batch.demo.library.listener.CustomSignguJobListener;
import com.batch.demo.library.repository.SidoEntityRepository;
import com.batch.common.listener.CustomItemProcessorListener;
import com.batch.common.listener.CustomItemReaderListener;
import com.batch.common.listener.CustomItemWriterListener;
import com.batch.common.listener.CustomStepListener;
import com.batch.demo.library.repository.SignguEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpDbToSignguDbJobDemo {

    private static final String JOB_NAME = "LIBRARY_TMP_TO_SIGNGU_JOB";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final SidoEntityRepository sidoEntityRepository;
    private final SignguEntityRepository signguEntityRepository;

    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;

    private static final int CHUNK_SIZE = 1000;

    @Bean(name = JOB_NAME)
    public Job libraryTmpDbToSignguDbJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(libraryTmpDbToSignguDbStep())
                .listener(new CustomSignguJobListener(signguEntityRepository))
                .build();
    }

    @Bean(name = JOB_NAME + "_STEP")
    public Step libraryTmpDbToSignguDbStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<LibraryTmpEntity, Signgu>chunk(CHUNK_SIZE)

                .listener(new CustomItemReaderListener())
                .reader(tmpDbToSignguReader())

                .listener(new CustomItemProcessorListener<>())
                .processor(tmpDbToSignguDbProcessor())

                .listener(new CustomItemWriterListener<>())
                .writer(signguDbWriter())

                .listener(new CustomStepListener())
                .build();
    }

    @StepScope
    @Bean(name = "LIBRARY_TMP_TO_SIGNGU_PROCESSOR")
    public ItemProcessor<? super LibraryTmpEntity,? extends Signgu> tmpDbToSignguDbProcessor() {
        return item -> {
            Sido sido = sidoEntityRepository.findByCtprvnNm(item.getCtprvnNm());
            return Signgu.builder()
                    .signguNm(item.getSignguNm())
                    .ctprvnCode(sido.getCtprvnCode())
                    .eupMyeonDongNm(null)
                    .eupMyeonDongCode(null)
                    .build();
        };
    }

    @StepScope
    @Bean(name = "LIBRARY_TMP_TO_SIGNGU_READER")
    public JdbcPagingItemReader<? extends LibraryTmpEntity> tmpDbToSignguReader() {
        return new JdbcPagingItemReader<LibraryTmpEntity>() {{
            setName("LIBRARY_TMP_TO_SIGNGU_READER");
            setPageSize(1000);
            setFetchSize(1000);
            setDataSource(dataSource);
            setQueryProvider(dbToDbProvider());
            setRowMapper(new BeanPropertyRowMapper<>(LibraryTmpEntity.class));
        }
        private MySqlPagingQueryProvider dbToDbProvider() {

            StringBuffer selectClause = new StringBuffer();
            StringBuffer fromClause = new StringBuffer();

            selectClause.append("CTPRVN_NM,");
            selectClause.append("SIGNGU_NM ");

            fromClause.append("CSV_TABLE");

            StringBuffer groupByClause = new StringBuffer();
            groupByClause.append("CTPRVN_NM, SIGNGU_NM");

            Map<String, Order> sortKeys = new HashMap<>(1);
            sortKeys.put("CTPRVN_NM", Order.DESCENDING);

            return new MySqlPagingQueryProvider() {{
                setSelectClause(selectClause.toString());
                setFromClause(fromClause.toString());
                setGroupClause(groupByClause.toString());
                setSortKeys(sortKeys);
            }};
        }};
    }

    @StepScope
    @Bean(name = "SIGNGU_DB_WRITER")
    public ItemWriter<? super Signgu> signguDbWriter() {
        return new JpaItemWriter<Signgu>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }

}
