package com.batch.sample;

import com.batch.domain.oracle.LibraryEntity;
import com.batch.listener.CustomStepListener;
import com.batch.mapper.LibraryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import sun.nio.cs.ext.EUC_KR;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.nio.file.Paths;

/**
 * 간단하게 사용하기 위한 Batch Job <br />
 * 1. Job
 * 2. Step
 * 2.1 tasklet
 * - 데이테 조회 및 출력
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileToDBJobSample {

    private static final String JOB_NAME = "basicJob";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * JDBC 용 DataSource
     */
    @Resource(name = "oracleDataSource")
    private DataSource oracleDataSource;

    /**
     * JPA 용 EntityManagerFactory
     */
    @Resource(name = "oracleEntityManagerFactory")
    private EntityManagerFactory oracleEntityManagerFactory;

    @Bean
    public Job basicJob() throws Exception {
        return this.jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(basicStep())
                .build();
    }

    /**
     * FlatFileReader example
     */
    @Bean
    public Step basicStep() throws Exception {
        return this.stepBuilderFactory.get(JOB_NAME + "_STEP")
                /* Jpa 적용 시 TransactionalManager 필요 */
//                .transactionManager(new JpaTransactionManager() {{
//                    setEntityManagerFactory(oracleEntityManagerFactory);
//                }})
                .<LibraryEntity, LibraryEntity>chunk(1000)
                /* Reader -> FlatFileItemReader */
                .reader(flatReader())
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
    public FlatFileItemReader<LibraryEntity> flatReader() throws Exception {
        return new FlatFileItemReader<LibraryEntity>() {{

            /* 파일 인코딩 문제로 EUC-KR로 설정 */
            setEncoding(new EUC_KR().historicalName());
            /* 파일 경로 읽기 Resource 설정 (추후 외부 경로를 파라미터로 받는 방법으로 변경하기) */
            setResource(new FileSystemResource(Paths.get("src", "main", "resources", "files", "전국도서관표준데이터.csv")));

            /* LineMapper 설정하기 (FlatFileItemReader의 필수 설정 값) */
            setLineMapper(new DefaultLineMapper<LibraryEntity>() {{

                /* LineTokenizer로 데이터 Mapping */
                setLineTokenizer(new DelimitedLineTokenizer(",") {{

                    /* line tokenizer 에 설정한 names 와 includeFields 가 읽어온 line 의 tokens 와 정확하게 일치해야 함 */
                    setStrict(true);
                    setLinesToSkip(1);
                    /*  LibraryEntitys의 key 값 매핑을 위한 Name 설정 */
                    setNames(LibraryEntity.CSVFields.getFieldNmArrays());

                    /* Contents 부분 Mapper */
                    setFieldSetMapper(new LibraryMapper());
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
    @Bean
    public JdbcBatchItemWriter<LibraryEntity> jdbcItemWriter() {
        return new JdbcBatchItemWriter<LibraryEntity>() {{
            setJdbcTemplate(new NamedParameterJdbcTemplate(oracleDataSource));
            setItemSqlParameterSourceProvider(BeanPropertySqlParameterSource::new);
            setSql(QUERT_INSERT_RECORD);
            afterPropertiesSet();
        }};
    }

    @Bean
    public JpaItemWriter<LibraryEntity> jpaItemWriter() {
        return new JpaItemWriter<LibraryEntity>() {{
            setEntityManagerFactory(oracleEntityManagerFactory);
        }};
    }
}
