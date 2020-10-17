package kr.seok.batch.demo;


import kr.seok.batch.demo.library.repository.LibraryDetailEntityRepository;
import kr.seok.batch.demo.library.repository.SidoEntityRepository;
import kr.seok.batch.demo.library.repository.SignguEntityRepository;
import kr.seok.batch.demo.library.writer.CustomExcelItemWriter;
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
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryToReportFileJobDemo {
    private static final String JOB_NAME = "LIBRARY_TOTAL_TO_REPORT_FILE_JOB";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;
    private final SidoEntityRepository sidoEntityRepository;
    private final SignguEntityRepository signguEntityRepository;
    private final LibraryDetailEntityRepository libraryDetailEntityRepository;

    private static final Integer CHUNK_SIZE = 1000;

    @Bean(name = JOB_NAME)
    public Job libraryTotalToReportFileJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(libraryTotalToReportFileStep())
                .build();

    }

    @Bean(name = JOB_NAME + "_STEP")
    public Step libraryTotalToReportFileStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<LibraryEntity, LibraryTotalEntity>chunk(CHUNK_SIZE)

                .reader(libraryTotalReader())
                .processor(libraryToLibraryTotalProcessor())
                .writer(new CustomExcelItemWriter())

                .build();
    }

    @StepScope
    @Bean(name = JOB_NAME + "_READER")
    public JdbcPagingItemReader<? extends LibraryEntity> libraryTotalReader() {
        return new JdbcPagingItemReader<LibraryEntity>() {{
            setName(JOB_NAME + "_READER");
            setPageSize(1000);
            setFetchSize(1000);
            setDataSource(dataSource);
            setQueryProvider(libraryTmpProvider());
            setRowMapper(new BeanPropertyRowMapper<>(LibraryEntity.class));
        }
            public MySqlPagingQueryProvider libraryTmpProvider() {
                StringBuffer selectClause = new StringBuffer();
                StringBuffer fromClause = new StringBuffer();
                selectClause.append("LBRRY_CODE,");
                selectClause.append("LBRRY_NM,");
                selectClause.append("CTPRVN_CODE,");
                selectClause.append("SIGNGU_CODE,");
                selectClause.append("LBRRY_SE,");
                selectClause.append("CLOSE_DAY,");
                selectClause.append("WEEKDAY_OPER_OPEN_HHMM,");
                selectClause.append("WEEKDAY_OPER_CLOSE_HHMM,");
                selectClause.append("SAT_OPER_OPEN_HHMM,");
                selectClause.append("SAT_OPER_CLOSE_HHMM,");
                selectClause.append("HOLIDAY_OPER_OPEN_HHMM,");
                selectClause.append("HOLIDAY_OPER_CLOSE_HHMM,");
                selectClause.append("SEAT_CO,");
                selectClause.append("BOOK_CO,");
                selectClause.append("PBLICTN_CO,");
                selectClause.append("NONEBOOK_CO,");
                selectClause.append("LON_CO,");
                selectClause.append("LONDAY_CNT,");
                selectClause.append("RDNM_ADR,");
                selectClause.append("OPERINSTITUTION_NM,");
                selectClause.append("LBRRY_PHONENUMBER,");
                selectClause.append("HOMEPAGEURL");

                fromClause.append("FROM TB_LBRRY ");

                Map<String, Order> sortKeys = new HashMap<>(1);
                sortKeys.put("LBRRY_CODE", Order.DESCENDING);

                return new MySqlPagingQueryProvider() {{
                    setSelectClause(selectClause.toString());
                    setFromClause(fromClause.toString());
                    setSortKeys(sortKeys);
                }};
            }};
    }

    @StepScope
    @Bean(name = JOB_NAME + "_PROCESSOR")
    public ItemProcessor<? super LibraryEntity,? extends LibraryTotalEntity> libraryToLibraryTotalProcessor() {
        return item -> {
            Sido sido = sidoEntityRepository.findByCtprvnCode(item.getCtprvnCode())
                    .orElseThrow(RuntimeException::new);
            Signgu signgu = signguEntityRepository.findBySignguCode(item.getSignguCode())
                    .orElseThrow(RuntimeException::new);
            LibraryDetailEntity libraryDetailEntity = libraryDetailEntityRepository.findByLbrryCode(item.getLbrryCode())
                    .orElseThrow(RuntimeException::new);
            return item.toEntity(sido, signgu, libraryDetailEntity);
        };
    }
}
