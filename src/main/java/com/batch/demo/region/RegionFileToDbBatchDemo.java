package com.batch.demo.region;

import com.batch.domain.region.Sido;
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
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import sun.nio.cs.ext.EUC_KR;

import javax.annotation.Resource;
import javax.sql.DataSource;

import static org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_COMMA;

/**
 * runner parameter
 * --job.name=regionFileToDbJob version=1 filePath={filePath}
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RegionFileToDbBatchDemo {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final static String JOB_NAME = "regionFileToDbJob";
    private final static int CHUNK_SIZE = 1000;

    @Resource(name = "oracleDataSource")
    private DataSource oracleDataSource;

    @Bean
    public Job regionFileToDbJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(regionFileToDbStep())
                .build();
    }

    public Step regionFileToDbStep() {
        return stepBuilderFactory.get(JOB_NAME + "_step")
                .<Sido, Sido>chunk(CHUNK_SIZE)
                .reader(regionFileReader())
                .writer(regionDbWriter())

                .build();
    }

    /**
     * File Reader Method
     * - Header Skip
     * - Value -> Vo
     *
     * @return FlatFileItemReader
     */
    public FlatFileItemReader<Sido> regionFileReader() {
        return new FlatFileItemReader<Sido>() {{

            /* 파일 경로 체크 확인 필수 */
            setResource(new ClassPathResource("files/지역코드(광역시도)_20191210.csv"));
            setEncoding(new EUC_KR().historicalName());

            /* 파일에 헤더가 존재하는 경우 1 row skip하고 value 부분만 읽기 */
            setLinesToSkip(1);

            /* File String Line -> Vo */
            setLineMapper(new DefaultLineMapper<Sido>() {{
                /* 파일 읽을 때 Line을 Tokenizer로 읽기 */
                setLineTokenizer(new DelimitedLineTokenizer(DELIMITER_COMMA) {{
                    /* CSV 파일의 Column을 어떤 값으로 매핑할 것인지 설정 */
                    setNames(Sido.SidoFields.getDbFieldNmArrays());
                }});

                /* set CSV File Value -> Vo */
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Sido>() {{
                    /* CSV 파일의 값 부분을 Vo로 매핑 하기 위한 설정 */
                    setTargetType(Sido.class);
                }});
            }});
        }};
    }

    /**
     * DB Writer Method
     * - Vo -> DB
     *
     * @return JdbcBatchItemWriter
     */
    public JdbcBatchItemWriter<Sido> regionDbWriter() {
        return new JdbcBatchItemWriter<Sido>() {{
            setDataSource(oracleDataSource);
            setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
            setSql("INSERT INTO TB_SIDO ( CTPRVN_CODE, CTPRVN_NM ) VALUES ( :ctprvnCd, :ctprvnNm )");
            afterPropertiesSet();
        }};
    }
}
