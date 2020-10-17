package kr.seok.batch.demo;

import kr.seok.batch.demo.library.domain.LibraryEntity;
import kr.seok.batch.demo.library.domain.LibraryTmpEntity;
import kr.seok.batch.demo.library.domain.Sido;
import kr.seok.batch.demo.library.domain.Signgu;
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

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 임시 테이블에 저장된 Raw 데이터를 도서관 테이블(TB_LBRRY)에 저장
 *  - JdbcPagingItemWReader to JpaItemWriter
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpDbToLibraryDbJobDemo {

    private static final String JOB_NAME = "LIBRARY_TMP_TO_LIBRARY_JOB";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final SidoEntityRepository sidoEntityRepository;
    private final SignguEntityRepository signguEntityRepository;

    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    private static final int CHUNK_SIZE = 1000;

    /* 임시 테이블 -> 도서관 테이블에 저장 */
    @Bean(name = JOB_NAME)
    public Job libraryTmpToLibraryJob() {
        return jobBuilderFactory.get(JOB_NAME)
                /* Job 실행시 JobParameters 의 run id 생성 */
                .incrementer(new RunIdIncrementer())
                /* 임시 테이블에 저장된 데이터를 도서관 테이블에 저장하는 step */
                .start(libraryTmpToLibraryStep())
                .build();
    }

    /* 임시 테이블에 저장된 데이터를 도서관 테이블에 저장하는 step */
    @Bean(name = JOB_NAME + "_STEP")
    public Step libraryTmpToLibraryStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                /* Input: 도서관 임시 Entity, OutPut: 도서관 Entity */
                .<LibraryTmpEntity, LibraryEntity>chunk(CHUNK_SIZE)

                /* Console 로 출력하기 위한 ReaderListener */
                .listener(new CustomItemReaderListener())
                /* 임시 테이블의 데이터를 읽는 JdbcPagingItemReader */
                .reader(tmpToLibraryReader())

                /* Console 로 출력하기 위한 ProcessorListener */
                .listener(new CustomItemProcessorListener<>())
                /* 임시 테이블의 데이터를 읽어 저장할 Entity에 저장 */
                .processor(tmpProcessor())

                /* Console로 Entity에 저장되어 있던 데이터를 DB에 Flush 하는 내용 출력 */
                .listener(new CustomItemWriterListener<>())
                /* Entity 캐시에 저장된 데이터를 DB로 Flush 처리 */
                .writer(libraryEntityJpaItemWriter())

                .build();
    }

    /* 임시 테이블의 데이터를 읽는 JdbcPagingItemReader */
    @Bean
    @StepScope
    public JdbcPagingItemReader<? extends LibraryTmpEntity> tmpToLibraryReader() {
        return new JdbcPagingItemReader<LibraryTmpEntity>() {{
            setName("LIBRARY_TMP_TO_LIBRARY_READER");
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
        selectClause.append("NONEBOOK_Co,");
        selectClause.append("LON_CO,");
        selectClause.append("LONDAY_CNT,");
        selectClause.append("RDNM_ADR,");
        selectClause.append("OPERINSTITUTION_NM,");
        selectClause.append("LBRRY_PHONENUMBER,");
        selectClause.append("PLOT_AR,");
        selectClause.append("BULD_AR,");
        selectClause.append("HOMEPAGEURL,");
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

    @Bean(name = JOB_NAME + "_PROCESSOR")
    @StepScope
    public ItemProcessor<? super LibraryTmpEntity,? extends LibraryEntity> tmpProcessor() {
        return item -> {
            Sido sido = sidoEntityRepository.findByCtprvnNm(item.getCtprvnNm());
            Signgu signgu = signguEntityRepository.findBySignguNmAndCtprvnCode(item.getSignguNm(), sido.getCtprvnCode());
            return LibraryEntity.builder()
                    .lbrryNm(item.getLbrryNm())
                    .ctprvnCode(sido.getCtprvnCode())
                    .signguCode(signgu.getSignguCode())

                    .lbrrySe(item.getLbrrySe())
                    .lbrryPhonenumber(item.getLbrryPhonenumber())
                    .homepageUrl(item.getHomepageUrl())

                    .closeDay(item.getCloseDay())
                    .weekdayOperCloseHhmm(item.getWeekdayOperCloseHhmm())
                    .weekdayOperOpenHhmm(item.getWeekdayOperOpenHhmm())
                    .satOperCloseHhmm(item.getSatOperCloseHhmm())
                    .satOperOpenHhmm(item.getSatOperOpenHhmm())
                    .holidayOperCloseHhmm(item.getHolidayOperCloseHhmm())
                    .holidayOperOpenHhmm(item.getHolidayOperOpenHhmm())

                    .nonebookCo(item.getNonebookCo())
                    .seatCo(item.getSeatCo())
                    .bookCo(item.getBookCo())
                    .pblictnCo(item.getPblictnCo())

                    .rdnmAdr(item.getRdnmAdr())
                    .lonCo(item.getLonCo())
                    .londayCnt(item.getLondayCnt())
                    .operinstitutionNm(item.getOperinstitutionNm())

                    .build();
        };
    }

    @Bean(name = "LIBRARY_WRITER")
    @StepScope
    public JpaItemWriter<LibraryEntity> libraryEntityJpaItemWriter() {
        return new JpaItemWriter<LibraryEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }

}
