package com.batch.demo.library;

import com.batch.domain.batch.LibraryTmpEntity;
import com.batch.domain.batch.Sido;
import com.batch.listener.CustomItemProcessorListener;
import com.batch.listener.CustomItemReaderListener;
import com.batch.listener.CustomStepListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
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
public class LibraryTmpDbToSidoDbJobDemo {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;

    private static final String JOB_NAME = "LIBRARY_TMP_TO_SIDO_JOB";
    private static final int CHUNK_SIZE = 1000;

    @Bean(name = JOB_NAME)
    public Job libraryTmpDbToOriginDbJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(libraryTmpDbToSidoDbStep())
                .build();
    }

    @Bean(name = JOB_NAME + "_STEP")
    public Step libraryTmpDbToSidoDbStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<LibraryTmpEntity, Sido>chunk(CHUNK_SIZE)

                .listener(new CustomItemReaderListener())
                .reader(libraryTmpDbToSidoDbReader())

                .listener(new CustomItemProcessorListener<>())
                .processor(libraryTmpDbToSidoDbProcessor())

                .writer(libraryTmpDbToSidoDbWriter())

                .listener(new CustomStepListener())
                .build();
    }

    @Bean(name = "sidoDb_writer")
    public JpaItemWriter<Sido> libraryTmpDbToSidoDbWriter() {
        return new JpaItemWriter<Sido>() {{
           setEntityManagerFactory(entityManagerFactory);
        }};
    }

    @Bean(name = "tmpDb_reader")
    public JdbcPagingItemReader<? extends LibraryTmpEntity> libraryTmpDbToSidoDbReader() {
        return new JdbcPagingItemReader<LibraryTmpEntity>() {{
            setName("tmpDbReader");
            setFetchSize(1000);
            setDataSource(dataSource);
            setQueryProvider(dbToDbProvider());
            setRowMapper(new BeanPropertyRowMapper<>(LibraryTmpEntity.class));
        }};
    }

    private MySqlPagingQueryProvider dbToDbProvider() {

        StringBuffer selectClause = new StringBuffer();
        StringBuffer fromClause = new StringBuffer();

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
        sortKeys.put("LBRRY_NM", Order.DESCENDING);

        return new MySqlPagingQueryProvider() {{
            setSelectClause(selectClause.toString());
            setFromClause(fromClause.toString());
            setSortKeys(sortKeys);
        }};
    }

    @Bean
    @StepScope
    public ItemProcessor<? super LibraryTmpEntity, Sido> libraryTmpDbToSidoDbProcessor() {
        /* 없으면 code 매핑해서 저장 */
        return (ItemProcessor<LibraryTmpEntity, Sido>) LibraryTmpEntity::toSido;
    }
}
