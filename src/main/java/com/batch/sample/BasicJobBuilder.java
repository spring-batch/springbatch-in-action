package com.batch.sample;

import com.batch.domain.Record;
import com.batch.mapper.LibraryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import sun.nio.cs.ext.EUC_KR;

import java.nio.file.Paths;

/**
 * 간단하게 사용하기 위한 Batch Job <br />
 *      1. Job
 *      2. Step
 *          2.1 tasklet
 *              - 데이테 조회 및 출력
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BasicJobBuilder {

    private static final String JOB_NAME = "basicJob";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job basicJob() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(basicStep())
                .build();
    }

    /**
     * FlatFileReader example
     * @return
     */
    @Bean
    public Step basicStep() {
        return this.stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<Record, Record>chunk(100)
                .reader(new FlatFileItemReader<Record>() {{
                    setEncoding(new EUC_KR().historicalName());
                    setResource(new FileSystemResource(Paths.get("src","main", "resources", "files", "전국도서관표준데이터.csv")));
                    setLineMapper(new DefaultLineMapper<Record>() {{
                        setLineTokenizer(new DelimitedLineTokenizer(",") {{
                            setNames(
                                    "도서관명",
                                    "시도명",
                                    "시군구명",
                                    "도서관유형",
                                    "휴관일",
                                    "평일운영시작시각",
                                    "평일운영종료시각",
                                    "토요일운영시작시각",
                                    "토요일운영종료시각",
                                    "공휴일운영시작시각",
                                    "공휴일운영종료시각",
                                    "열람좌석수",
                                    "자료수(도서)",
                                    "자료수(연속간행물)",
                                    "자료수(비도서)",
                                    "대출가능권수",
                                    "대출가능일수",
                                    "소재지도로명주소",
                                    "운영기관명",
                                    "도서관전화번호",
                                    "부지면적",
                                    "건물면적",
                                    "홈페이지주소",
                                    "위도",
                                    "경도",
                                    "데이터기준일자",
                                    "제공기관코드",
                                    "제공기관명"
                            );
                            setFieldSetMapper(new LibraryMapper());
                        }});
                    }});
                }})
                .writer(writer())
                .build();
    }

    @Bean
    public ItemWriter<? super Record> writer(){
        return (ItemWriter<Record>) items -> {
            for(Record item : items) {
                log.info("item : {}", item);
            }
        };
    }
}
