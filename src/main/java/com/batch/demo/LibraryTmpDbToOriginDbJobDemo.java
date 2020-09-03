package com.batch.demo;

import com.batch.domain.batch.LibraryTmpEntity;
import com.batch.domain.batch.Sido;
import com.batch.domain.repository.LibraryTmpEntityRepository;
import com.batch.domain.repository.SidoRepository;
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
public class LibraryTmpDbToOriginDbJobDemo {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    private final LibraryTmpEntityRepository libraryTmpEntityRepository;

    private final SidoRepository sidoRepository;

    private static final int CHUNK_SIZE = 1000;

    @Bean(name = "library_tmpdb_to_origindb_job")
    public Job libraryTmpDbToOriginDbJob() throws Exception {
        return jobBuilderFactory.get("library_tmpdb_to_origindb_job")
                .incrementer(new RunIdIncrementer())
                .start(libraryTmpDbToOriginDbStep())
                .build();
    }

    @Bean(name = "library_tmpdb_to_origindb_step")
    public Step libraryTmpDbToOriginDbStep() throws Exception {
        return stepBuilderFactory.get("library_tmpdb_to_origindb_step")
                .<LibraryTmpEntity, Sido>chunk(CHUNK_SIZE)

                .listener(new CustomItemReaderListener())
                .reader(tmpDbReader())

                .listener(new CustomItemProcessorListener<>())
                .processor(tmpDbToSidoDbProcessor())

                .writer(sidoDbWriter())

                .listener(new CustomStepListener())
                .build();
    }

    @Bean(name = "sidoDb_writer")
    public JpaItemWriter<Sido> sidoDbWriter() {
        return new JpaItemWriter<Sido>() {{
           setEntityManagerFactory(entityManagerFactory);
        }};
    }

    @Bean
    @StepScope
    public ItemProcessor<? super LibraryTmpEntity, Sido> tmpDbToSidoDbProcessor() {
        return new ItemProcessor<LibraryTmpEntity, Sido>() {
            @Override
            public Sido process(LibraryTmpEntity item) throws Exception {
                log.info("[PROCESSOR] [ITEM] [{}]", item);
                /* 없으면 code 매핑해서 저장 */
                return item.toSido();
            }
        };
    }

    @Bean(name = "tmpDb_reader")
    @StepScope
    public JdbcPagingItemReader<? extends LibraryTmpEntity> tmpDbReader() throws Exception {
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
}
