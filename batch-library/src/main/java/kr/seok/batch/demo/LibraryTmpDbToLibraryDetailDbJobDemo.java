package kr.seok.batch.demo;

import kr.seok.batch.demo.library.domain.*;
import kr.seok.batch.demo.library.repository.LibraryEntityRepository;
import kr.seok.batch.demo.library.repository.SidoEntityRepository;
import kr.seok.batch.demo.library.repository.SignguEntityRepository;
import kr.seok.common.listener.CustomItemProcessorListener;
import kr.seok.common.listener.CustomItemReaderListener;
import kr.seok.common.listener.CustomItemWriterListener;
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
import org.springframework.util.StringUtils;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 임시 테이블에 저장된 도서관 데이터 중 주로 사용되지 않는 속성을 Detail 테이블에 저장
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpDbToLibraryDetailDbJobDemo {

    /* Batch */
    private static final String JOB_NAME = "LIBRARY_TMP_TO_LIBRARY_DETAIL_JOB";
    private static final int CHUNK_SIZE = 1000;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /* Jdbc, Jpa */
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    /* 도서관, 시도, 시군구 Repository */
    private final LibraryEntityRepository libraryEntityRepository;
    private final SidoEntityRepository sidoEntityRepository;
    private final SignguEntityRepository signguEntityRepository;

    /* 임시 테이블의 데이터를 도서관 테이블에 저장하기위한 Job */
    @Bean(name = JOB_NAME)
    public Job libraryTmpToLibraryJob() {
        return jobBuilderFactory.get(JOB_NAME)
                /* runid 생성 */
                .incrementer(new RunIdIncrementer())
                /* 도서관 임시 테이블의 데이터를 도서관 테이블에 저장하기 위한 Step 호출 */
                .start(libraryTmpToLibraryStep())
                .build();
    }

    /* 도서관 임시 테이블의 데이터를 도서관 테이블에 저장하기 위한 Step */
    @Bean(name = JOB_NAME + "_STEP")
    public Step libraryTmpToLibraryStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<LibraryTmpEntity, LibraryDetailEntity>chunk(CHUNK_SIZE)

                .listener(new CustomItemReaderListener())
                /* JdbcPagingItemReader 방식으로 임시 테이블의 데이터를 조회 */
                .reader(libraryTmpToLibraryDetailReader())

                .listener(new CustomItemProcessorListener<>())
                /* 시도, 시군구, 도서관 데이터를 조회하여 상세 Entity 에 저장 */
                .processor(tmpProcessor())

                .listener(new CustomItemWriterListener<>())
                /* Jpa Entity를 DB에 Flush 처리 */
                .writer(libraryEntityJpaItemWriter())

                .build();
    }

    /* 임시 테이블에 저장되어 있던 도서관 상세 테이블을 조회 */
    @Bean
    @StepScope
    public JdbcPagingItemReader<? extends LibraryTmpEntity> libraryTmpToLibraryDetailReader() {
        return new JdbcPagingItemReader<LibraryTmpEntity>() {{
            setName(JOB_NAME + "_READER");
            setPageSize(1000);
            setFetchSize(1000);
            setDataSource(dataSource);
            setQueryProvider(dbToDbProvider());
            setRowMapper(new BeanPropertyRowMapper<>(LibraryTmpEntity.class));
        }};
    }

    private MySqlPagingQueryProvider dbToDbProvider() {

        StringBuffer selectClause = new StringBuffer();
        StringBuffer fromClause = new StringBuffer();

        selectClause.append("LBRRY_NM,");
        selectClause.append("CTPRVN_NM,");
        selectClause.append("SIGNGU_NM,");
        selectClause.append("PLOT_AR,");
        selectClause.append("BULD_AR,");
        selectClause.append("LATITUDE,");
        selectClause.append("LONGITUDE,");

        selectClause.append("REFERENCE_DATE,");
        selectClause.append("INSTT_CODE,");
        selectClause.append("INSTT_NM");

        fromClause.append("CSV_TABLE");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("LBRRY_NM", Order.DESCENDING);

        return new MySqlPagingQueryProvider() {{
            setSelectClause(selectClause.toString());
            setFromClause(fromClause.toString());
            setSortKeys(sortKeys);
        }};
    }

    /* Sido, Signgu, Library -> LibraryDetail */
    @Bean
    @StepScope
    public ItemProcessor<? super LibraryTmpEntity, ? extends LibraryDetailEntity> tmpProcessor() {
        return item -> {
            /* Sido의 key 값이 필요하기 때문에 Entity 자체를 조회할 필요가 없음 */
            Sido sido = sidoEntityRepository.findByCtprvnNm(item.getCtprvnNm());
            /* Signgu key 값이 필요하기 때문에 Entity 자체를 조회할 필요가 없음 */
            Signgu signgu = signguEntityRepository.findBySignguNmAndCtprvnCode(item.getSignguNm(), sido.getCtprvnCode());
            /* Library key 값이 필요하기 때문에 Entity 자체를 조회할 필요가 없음 */
            LibraryEntity libraryEntity = libraryEntityRepository.findByLbrryNmAndCtprvnCodeAndSignguCode(item.getLbrryNm(), sido.getCtprvnCode(), signgu.getSignguCode());

            return LibraryDetailEntity.builder()
                    .lbrryCode(libraryEntity.getLbrryCode())
                    .buldAr(!StringUtils.isEmpty(item.getBuldAr()) ? new BigDecimal(item.getBuldAr()) : null)
                    .plotAr(!StringUtils.isEmpty(item.getPlotAr()) ? new BigDecimal(item.getPlotAr()) : null)
                    .latitude(!StringUtils.isEmpty(item.getLatitude()) ? new BigDecimal(item.getLatitude()) : null)
                    .longitude(!StringUtils.isEmpty(item.getLongitude()) ? new BigDecimal(item.getLongitude()) : null)
                    .insttCode(item.getInsttCode())
                    .insttNm(item.getInsttNm())
                    .referenceDate(item.getReferenceDate())
                    .build();
        };
    }

    /* Jpa Entity Flush 처리 */
    @Bean(name = "LIBRARY_DETAIL_WRITER")
    @StepScope
    public JpaItemWriter<LibraryDetailEntity> libraryEntityJpaItemWriter() {
        return new JpaItemWriter<LibraryDetailEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }

}
