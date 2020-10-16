package kr.seok.batch.demo;

import kr.seok.batch.demo.library.repository.LibraryEntityRepository;
import kr.seok.batch.demo.library.repository.SidoEntityRepository;
import kr.seok.batch.demo.library.repository.SignguEntityRepository;
import kr.seok.common.listener.CustomItemProcessorListener;
import kr.seok.common.listener.CustomItemReaderListener;
import kr.seok.common.listener.CustomItemWriterListener;
import kr.seok.batch.demo.library.domain.*;
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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpDbToLibraryDetailDbJobDemo {

    private static final String JOB_NAME = "LIBRARY_TMP_TO_LIBRARY_DETAIL_JOB";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    private final LibraryEntityRepository libraryEntityRepository;
    private final SidoEntityRepository sidoEntityRepository;
    private final SignguEntityRepository signguEntityRepository;

    private static final int CHUNK_SIZE = 1000;

    @Bean(name = JOB_NAME)
    public Job libraryTmpToLibraryJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(libraryTmpToLibraryStep())
                .build();
    }

    @Bean(name = JOB_NAME + "_STEP")
    public Step libraryTmpToLibraryStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<LibraryTmpEntity, LibraryDetailEntity>chunk(CHUNK_SIZE)

                .listener(new CustomItemReaderListener())
                .reader(libraryTmpToLibraryDetailReader())

                .listener(new CustomItemProcessorListener<>())
                .processor(tmpProcessor())

                .listener(new CustomItemWriterListener<>())
                .writer(libraryEntityJpaItemWriter())

                .build();
    }

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

    @Bean
    @StepScope
    public ItemProcessor<? super LibraryTmpEntity,? extends LibraryDetailEntity> tmpProcessor() {
        return item -> {
            Sido sido = sidoEntityRepository.findByCtprvnNm(item.getCtprvnNm());
            Signgu signgu = signguEntityRepository.findBySignguNmAndCtprvnCode(item.getSignguNm(), sido.getCtprvnCode());
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

    @Bean(name = "LIBRARY_DETAIL_WRITER")
    @StepScope
    public JpaItemWriter<LibraryDetailEntity> libraryEntityJpaItemWriter() {
        return new JpaItemWriter<LibraryDetailEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }

}
