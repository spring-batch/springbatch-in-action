package com.batch.demo.library.step;

import com.batch.demo.library.domain.LibraryTmpEntity;
import com.batch.demo.library.domain.Sido;
import com.batch.demo.library.domain.Signgu;
import com.batch.demo.library.listener.SignguStep3Listener;
import com.batch.demo.library.repository.SidoEntityRepository;
import com.batch.demo.library.repository.SignguEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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

import static com.batch.demo.library.LibraryTotalToReportFileJobDemo.CHUNK_SIZE;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpToSignguStep3 {

    private static final String STEP_NAME = "LIBRARY_TMP_TO_SIGNGU_STEP3";
    private final StepBuilderFactory stepBuilderFactory;

    private final SidoEntityRepository sidoEntityRepository;
    private final SignguEntityRepository signguEntityRepository;

    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;

    @JobScope
    @Bean(name = STEP_NAME)
    public Step libraryTmpToSignguStep3() {
        return stepBuilderFactory.get(STEP_NAME)
                .<LibraryTmpEntity, Signgu>chunk(CHUNK_SIZE)

                .reader(libraryTmpStep3Reader())
                .processor(libraryTmpToSignguStep3Processor())
                .writer(signguStep3Writer())

                .listener(new SignguStep3Listener(signguEntityRepository))
                .build();
    }

    @StepScope
    @Bean(name = STEP_NAME + "_READER")
    public JdbcPagingItemReader<? extends LibraryTmpEntity> libraryTmpStep3Reader() {
        return new JdbcPagingItemReader<LibraryTmpEntity>() {{
            setName("LIBRARY_TMP_STEP3_READER");
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

    @Bean(name = STEP_NAME + "PROCESSOR")
    @StepScope
    public ItemProcessor<? super LibraryTmpEntity,? extends Signgu> libraryTmpToSignguStep3Processor() {
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

    @Bean(name = STEP_NAME + "_WRITER")
    @StepScope
    public ItemWriter<? super Signgu> signguStep3Writer() {
        return new JpaItemWriter<Signgu>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
