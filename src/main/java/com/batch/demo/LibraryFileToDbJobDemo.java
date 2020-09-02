package com.batch.demo;

import com.batch.domain.batch.LbrryTmpMappedEntity;
import com.batch.domain.batch.LibraryTmp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import sun.nio.cs.ext.EUC_KR;

import javax.persistence.EntityManagerFactory;

import static org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_COMMA;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryFileToDbJobDemo {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final int CHUNK_SIZE = 1000;

    @Bean(name = "libraryFileToDbJob")
    public Job libraryFileToDbJob() {
        return jobBuilderFactory.get("libraryFileToDbJob")
                .incrementer(new RunIdIncrementer())
                .start(libraryFileToDbStep())
                .build();
    }

    @Bean(name = "libraryFileToDbStep")
    public Step libraryFileToDbStep() {
        return stepBuilderFactory.get("libraryFileToDbStep")
                .<LibraryTmp, LibraryTmp>chunk(CHUNK_SIZE)
                .reader(flatFileReader())
                .processor(fileToDbProcessor())
                .writer(jpaItemWriter())
                .build();
    }

    /**
     * FlatFileItemReader Custom
     *
     * @return FlatFileItemReader
     */
    @StepScope
    @Bean(name = "libraryFileReader")
    public FlatFileItemReader<LibraryTmp> flatFileReader() {
        return new FlatFileItemReader<LibraryTmp>() {{

            /* 파일 경로 읽기 Resource 설정 (추후 외부 경로를 파라미터로 받는 방법으로 변경하기) */
            setResource(new ClassPathResource("files/전국도서관표준데이터.csv"));
            /* 파일 인코딩 문제로 EUC-KR로 설정 */
            setEncoding(new EUC_KR().historicalName());
            /* excel 헤더 첫 번째 row Skip*/
            setLinesToSkip(1);

            /* [FlatFileReader 필수 설정] LineMapper 설정하기 */
            setLineMapper(new DefaultLineMapper<LibraryTmp>() {{

                /* [LineMapper 필수 설정] LineTokenizer로 데이터 Mapping */
                setLineTokenizer(
                        /* [DelimitedLineTokenizer 클래스의 필수 값] delimiter 설정 */
                        new DelimitedLineTokenizer(DELIMITER_COMMA) {{

//                    /* line tokenizer 에 설정한 names 와 includeFields 가 읽어온 line 의 tokens 와 정확하게 일치해야 함 */
//                    setStrict(true);
                    /*  LibraryEntitys의 key 값 매핑을 위한 Name 설정 */
                    setNames(LbrryTmpMappedEntity.getDBFieldArrays());
                }});
                /* [LineMapper 필수 설정]  FieldSet을 Entity와 매핑 설정*/
                setFieldSetMapper(new BeanWrapperFieldSetMapper<LibraryTmp>() {{
                    /* CSV 파일의 값 부분을 Vo로 매핑 하기 위한 설정 */
                    setTargetType(LibraryTmp.class);
                }});
            }});
            log.info("[LOG] [ITEM] [{}]", getCurrentItemCount());
        }};
    }

    @Bean
    @StepScope
    public ItemProcessor<? super LibraryTmp,? extends LibraryTmp> fileToDbProcessor() {
        return item -> {
            /* 데이터 처리 영역 */
            log.info("[LOG] [DATA] [{}] [{}]", item.getClass(), item);
            return item;
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<LibraryTmp> jpaItemWriter() {
        JpaItemWriter<LibraryTmp> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
