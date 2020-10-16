package kr.seok.batch.demo.library.step;


import kr.seok.batch.demo.library.repository.LibraryEntityRepository;
import kr.seok.batch.demo.library.repository.SidoEntityRepository;
import kr.seok.batch.demo.library.repository.SignguEntityRepository;
import kr.seok.common.utils.StringUtils;
import kr.seok.batch.demo.library.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static kr.seok.batch.demo.LibraryTotalToReportFileJobDemo.CHUNK_SIZE;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpToLibraryDetailStep5 {

    private static final String STEP_NAME = "LIBRARY_TMP_TO_LIBRARY_DETAIL_STEP5";

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    private final LibraryEntityRepository libraryEntityRepository;
    private final SidoEntityRepository sidoEntityRepository;
    private final SignguEntityRepository signguEntityRepository;

    @JobScope
    @Bean(name = STEP_NAME)
    public Step libraryTmpToLibraryDetailStep5() {
        return stepBuilderFactory.get(STEP_NAME)
                .<LibraryTmpEntity, LibraryDetailEntity>chunk(CHUNK_SIZE)

                .reader(libraryTmpToLibraryDetailReader())
                .processor(tmpProcessor())
                .writer(libraryEntityJpaItemWriter())

                .build();
    }

    @StepScope
    @Bean(name = STEP_NAME + "_READER")
    public JdbcPagingItemReader<? extends LibraryTmpEntity> libraryTmpToLibraryDetailReader() {
        return new JdbcPagingItemReader<LibraryTmpEntity>() {{
            setName(STEP_NAME + "_READER");
            setPageSize(1000);
            setFetchSize(1000);
            setDataSource(dataSource);
            setQueryProvider(dbToDbProvider());
            setRowMapper(new BeanPropertyRowMapper<>(LibraryTmpEntity.class));
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
        }};
    }



    @StepScope
    @Bean(name = STEP_NAME + "_PROCESSOR")
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

    @Bean(name = STEP_NAME + "_WRITER")
    public JpaItemWriter<LibraryDetailEntity> libraryEntityJpaItemWriter() {
        return new JpaItemWriter<LibraryDetailEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }

}
