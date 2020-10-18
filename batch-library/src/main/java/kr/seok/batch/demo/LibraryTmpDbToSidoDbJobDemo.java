package kr.seok.batch.demo;


import kr.seok.batch.demo.library.domain.LibraryTmpEntity;
import kr.seok.batch.demo.library.domain.Sido;
import kr.seok.batch.demo.library.listener.CustomSidoJobListener;
import kr.seok.batch.demo.library.repository.SidoEntityRepository;
import kr.seok.common.listener.CustomItemProcessorListener;
import kr.seok.common.listener.CustomItemReaderListener;
import kr.seok.common.listener.CustomStepListener;
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

/**
 * 임시 테이블에 저장된 데이터를 Sido 테이블에 저장
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpDbToSidoDbJobDemo {

    /* Spring Batch */
    private static final String JOB_NAME = "LIBRARY_TMP_TO_SIDO_JOB";
    private static final int CHUNK_SIZE = 1000;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /* Jdbc, Jpa */
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    /* Entity */
    private final SidoEntityRepository sidoEntityRepository;

    /* 임시 도서관 테이블의 데이터를 Sido 테이블에 저장 */
    @Bean(name = JOB_NAME)
    public Job libraryTmpDbToSidoDbJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())

                /* 임시 테이블의 데이터를 Sido 테이블에 저장시 Sido 테이블 비우기 */
                .listener(new CustomSidoJobListener(sidoEntityRepository))
                /* 임시 도서관 테이블의 데이터를 Sido 테이블에 저장 Step 호출 */
                .start(libraryTmpDbToSidoDbStep())
                .build();
    }

    /* 임시 도서관 테이블의 데이터를 Sido 테이블에 저장 Step*/
    @Bean(name = JOB_NAME + "_STEP")
    public Step libraryTmpDbToSidoDbStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<LibraryTmpEntity, Sido>chunk(CHUNK_SIZE)

                /* Console 출력 Listener */
                .listener(new CustomItemReaderListener<>())
                /* 임시 테이블의 데이터 조회 JdbcPagingItemReader */
                .reader(libraryTmpDbToSidoDbReader())

                /* Console 출력 Listener */
                .listener(new CustomItemProcessorListener<>())
                /* 임시 테이블 Entity를 시도 Entity로 저장 */
                .processor(libraryTmpDbToSidoDbProcessor())

                /* Sido Entity에 저장되어 있던 Data를 DB에 Flush */
                .writer(libraryTmpDbToSidoDbWriter())

                /* 이건 왜 만들었었지 ? 상태 값을 확인하려고 만들었던 것 같은데 의미 없음 */
                .listener(new CustomStepListener())
                .build();
    }

    /* 임시 테이블의 데이터 조회 JdbcPagingItemReader */
    @StepScope
    @Bean(name = "tmpDb_reader")
    public JdbcPagingItemReader<? extends LibraryTmpEntity> libraryTmpDbToSidoDbReader() {
        return new JdbcPagingItemReader<LibraryTmpEntity>() {{
            setName("tmpDbReader");
            setPageSize(CHUNK_SIZE);
            setFetchSize(CHUNK_SIZE);
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

    /* 임시 테이블 Entity를 시도 Entity로 저장 */
    @Bean
    @StepScope
    public ItemProcessor<? super LibraryTmpEntity, Sido> libraryTmpDbToSidoDbProcessor() {
        /* 없으면 code 매핑해서 저장 */
        return LibraryTmpEntity::toSido;
    }

    /* Sido Entity에 저장되어 있던 Data를 DB에 Flush */
    @StepScope
    @Bean(name = "sidoDb_writer")
    public JpaItemWriter<Sido> libraryTmpDbToSidoDbWriter() {
        return new JpaItemWriter<Sido>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
