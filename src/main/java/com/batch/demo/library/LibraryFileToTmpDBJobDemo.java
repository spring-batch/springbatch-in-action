package com.batch.demo.library;

import com.batch.domain.oracle.LibraryEntity;
import com.batch.listener.CustomStepListener;
import com.batch.mapper.LibraryFileToDBMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import sun.nio.cs.ext.EUC_KR;

import javax.annotation.Resource;
import javax.sql.DataSource;

import static org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_COMMA;

/**
 * <pre>
 * 간단하게 Batch 사용하기 <br />
 * 1. Job
 * 2. Step
 * 2.1 FlatFileItemReader
 *      - Excel 타입의 데이터를 읽기
 * 2.2 JdbcBatchItemWriter
 *      - 단순한 DB 쓰기
 * </pre>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryFileToTmpDBJobDemo {

    private static final String JOB_NAME = "fileToDbJob";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * JDBC 용 DataSource
     */
    @Resource(name = "oracleDataSource")
    private DataSource oracleDataSource;

    /**
     * FlatFile -> JdbcBatch Job
     */
    @Bean
    public Job fileToDbJob() throws Exception {
        return this.jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(fileToDbStep())
                .build();
    }

    /**
     * FlatFileReader example
     */
    @Bean
    public Step fileToDbStep() throws Exception {
        return this.stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<LibraryEntity, LibraryEntity>chunk(1000)

                /* Reader -> FlatFileItemReader */
                .reader(flatFileReader())
                /* JdbcBatchItemWriter 로 구현한 버전 */
                .writer(jdbcItemWriter())

                /* Custom Step Listener */
                .listener(new CustomStepListener())
                .build();
    }

    /**
     * FlatFileItemReader Custom
     *
     * @return FlatFileItemReader
     */
    @Bean
    public FlatFileItemReader<LibraryEntity> flatFileReader() throws Exception {
        return new FlatFileItemReader<LibraryEntity>() {{

            /* 파일 인코딩 문제로 EUC-KR로 설정 */
            setEncoding(new EUC_KR().historicalName());
            /* 파일 경로 읽기 Resource 설정 (추후 외부 경로를 파라미터로 받는 방법으로 변경하기) */
            setResource(new ClassPathResource("files/전국도서관표준데이터.csv"));

            /* LineMapper 설정하기 (FlatFileItemReader의 필수 설정 값) */
            setLineMapper(new DefaultLineMapper<LibraryEntity>() {{

                /* LineTokenizer로 데이터 Mapping */
                setLineTokenizer(new DelimitedLineTokenizer(DELIMITER_COMMA) {{
                    /* excel 헤더 첫 번째 row Skip :: TODO 애초에 Line 1 row를 Skip 해도 됨 */
                    setLinesToSkip(1);
                    /* line tokenizer 에 설정한 names 와 includeFields 가 읽어온 line 의 tokens 와 정확하게 일치해야 함 */
                    setStrict(true);
                    /*  LibraryEntitys의 key 값 매핑을 위한 Name 설정 */
                    setNames(LibraryEntity.CSVFields.getFieldNmArrays());
                    /* Contents 부분 Mapper */
                    setFieldSetMapper(new LibraryFileToDBMapper());
                    /* 필수값 체크 (delimiter) */
                    afterPropertiesSet();
                }});
                /* 필수값 체크 (tokenizer, fieldSetMapper) */
                afterPropertiesSet();
            }});
            /* 필수값 체크 (lineMapper) */
            afterPropertiesSet();
        }};
    }

    @Bean
    public JdbcBatchItemWriter<LibraryEntity> jdbcItemWriter() {
        return new JdbcBatchItemWriter<LibraryEntity>() {{
            /* Template ? */
            setJdbcTemplate(new NamedParameterJdbcTemplate(oracleDataSource));
            /* */
            setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
            /* Query */
            setSql(QUERT_INSERT_RECORD);
            afterPropertiesSet();
        }};
    }

    private static final String QUERT_INSERT_RECORD =
            "INSERT INTO CSV_TABLE " +
                    "(" +
                    "LBRRY_CODE, " +
                    "LBRRY_NM, " +
                    "CTPRVN_NM, " +
                    "SIGNGU_NM, " +
                    "LBRRY_SE, " +
                    "CLOSE_DAY, " +
                    "WEEKDAY_OPER_OPEN_HHMM, " +
                    "WEEKDAY_OPER_CLOSE_HHMM, " +
                    "SAT_OPER_OPEN_HHMM, " +
                    "SAT_OPER_CLOSE_HHMM, " +
                    "HOLIDAY_OPER_OPEN_HHMM, " +
                    "HOLIDAY_OPER_CLOSE_HHMM, " +
                    "SEAT_CO, " +
                    "BOOK_CO, " +
                    "PBLICTN_CO, " +
                    "NONEBOOK_CO, " +
                    "LON_CO, " +
                    "LONDAY_CNT, " +
                    "RDNM_ADR, " +
                    "OPERINSTITUTION_NM, " +
                    "LBRRY_PHONENUMBER, " +
                    "PLOT_AR, " +
                    "BULD_AR, " +
                    "HOMEPAGEURL, " +
                    "LATITUDE, " +
                    "LONGITUDE, " +
                    "REFERENCE_DATE, " +
                    "INSTT_CODE, " +
                    "INSTT_NM " +
                    ")" +
                    "VALUES " +
                    "(" +
                    "CSV_TABLE_SEQ.nextval, " +
                    ":lbrryNm, " +
                    ":ctprvnNm, " +
                    ":signguNm, " +
                    ":lbrrySe, " +
                    ":closeDay, " +
                    ":weekdayOperOpenHhmm, " +
                    ":weekdayOperCloseHhmm, " +
                    ":satOperOpenHhmm, " +
                    ":satOperCloseHhmm, " +
                    ":holidayOperOpenHhmm, " +
                    ":holidayOperCloseHhmm, " +
                    ":seatCo, " +
                    ":bookCo, " +
                    ":pblictnCo, " +
                    ":noneBookCo, " +
                    ":lonCo, " +
                    ":lonDaycnt, " +
                    ":rdnmadr, " +
                    ":operInstitutionNm, " +
                    ":lbrryPhoneNumber, " +
                    ":plotAr, " +
                    ":buldAr, " +
                    ":homepageUrl, " +
                    ":latitude, " +
                    ":longitude, " +
                    ":referenceDate, " +
                    ":insttCode, " +
                    ":insttNm " +
                    ")";
}
