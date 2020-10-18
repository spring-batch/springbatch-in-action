package kr.seok.batch.demo;

import kr.seok.batch.demo.library.domain.LibraryTmpEntity;
import kr.seok.batch.demo.library.domain.Sido;
import kr.seok.batch.demo.library.domain.Signgu;
import kr.seok.batch.demo.library.listener.CustomSignguJobListener;
import kr.seok.batch.demo.library.repository.SidoEntityRepository;
import kr.seok.batch.demo.library.repository.SignguEntityRepository;
import kr.seok.common.listener.CustomItemProcessorListener;
import kr.seok.common.listener.CustomItemReaderListener;
import kr.seok.common.listener.CustomItemWriterListener;
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

/**
 * 임시 테이블에서 Signgu 테이블로 저장
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpDbToSignguDbJobDemo {

    /* Batch */
    private static final String JOB_NAME = "LIBRARY_TMP_TO_SIGNGU_JOB";
    private static final int CHUNK_SIZE = 1000;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /* Jdbc, Jpa */
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;

    /* Sido, Signgu Repository */
    private final SidoEntityRepository sidoEntityRepository;
    private final SignguEntityRepository signguEntityRepository;

    /* 임시 테이블 데이터를 시군구 테이블에 저장 Job */
    @Bean(name = JOB_NAME)
    public Job libraryTmpDbToSignguDbJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                /* 임시 테이블 데이터를 시군구 테이블에 저장 Step 호출 */
                .start(libraryTmpDbToSignguDbStep())
                /* 시군구 테이블에 저장 시 테이블 비우기 */
                .listener(new CustomSignguJobListener(signguEntityRepository))
                .build();
    }

    /* 임시 테이블 데이터를 시군구 테이블에 저장 Step */
    @Bean(name = JOB_NAME + "_STEP")
    public Step libraryTmpDbToSignguDbStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<LibraryTmpEntity, Signgu>chunk(CHUNK_SIZE)

                .listener(new CustomItemReaderListener<>())
                /* 임시 테이블 읽는 ItemReader */
                .reader(tmpDbToSignguReader())

                .listener(new CustomItemProcessorListener<>())
                /* 임시 테이블 Entity 를 Signgu Entity로 저장 */
                .processor(tmpDbToSignguDbProcessor())

                .listener(new CustomItemWriterListener<>())
                /* Entity 에 저장된 데이터를 DB에 Flush */
                .writer(signguDbWriter())

                .listener(new CustomStepListener())
                .build();
    }

    /* JdbcPagingItemReader 를 통해 데이터 조회 */
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

    /* 임시테이블 조회 및 Sido Entity 조회해서 Signgu Entity에 저장 */
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

    /* Entity 에 저장된 데이터를 DB에 Flush */
    @StepScope
    @Bean(name = "SIGNGU_DB_WRITER")
    public ItemWriter<? super Signgu> signguDbWriter() {
        return new JpaItemWriter<Signgu>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }

}
