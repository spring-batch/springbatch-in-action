package com.batch.demo;

import com.batch.domain.batch.LibraryDTO;
import com.batch.domain.batch.LibraryTmpEntity;
import com.batch.domain.batch.LibraryTmpEntityFields;
import com.batch.listener.ItemReaderListener;
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

    @Bean(name = "library_file_to_db_step")
    public Step libraryFileToDbStep() {
        return stepBuilderFactory.get("library_file_to_db_step")
                .<LibraryDTO, LibraryTmpEntity>chunk(CHUNK_SIZE)
                .reader(flatFileReader())
                .listener(new ItemReaderListener())
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
    @Bean(name = "library_file_reader")
    public FlatFileItemReader<LibraryDTO> flatFileReader() {
        return new FlatFileItemReader<LibraryDTO>() {{

            /* 파일 경로 읽기 Resource 설정 (추후 외부 경로를 파라미터로 받는 방법으로 변경하기) */
            setResource(new ClassPathResource("files/전국도서관표준데이터.csv"));
            /* 파일 인코딩 문제로 EUC-KR로 설정 */
            setEncoding(new EUC_KR().historicalName());
            /* excel 헤더 첫 번째 row Skip*/
            setLinesToSkip(1);

            /* [FlatFileReader 필수 설정] LineMapper 설정하기 */
            setLineMapper(new DefaultLineMapper<LibraryDTO>() {{

                /* [LineMapper 필수 설정] LineTokenizer로 데이터 Mapping */
                setLineTokenizer(
                        /* [DelimitedLineTokenizer 클래스의 필수 값] delimiter 설정 */
                        new DelimitedLineTokenizer(DELIMITER_COMMA) {{
                    /*  LibraryEntitys의 key 값 매핑을 위한 Name 설정 */
                    setNames(LibraryTmpEntityFields.getDBFieldArrays());
                }});
                /* [LineMapper 필수 설정]  FieldSet을 Entity와 매핑 설정*/
                setFieldSetMapper(new BeanWrapperFieldSetMapper<LibraryDTO>() {{
                    /* CSV 파일의 값 부분을 Vo로 매핑 하기 위한 설정 */
                    setTargetType(LibraryDTO.class);
                }});
            }});
        }};
    }

    @Bean(name = "library_dto_to_entity_processor")
    @StepScope
    public ItemProcessor<? super LibraryDTO, ? extends LibraryTmpEntity> fileToDbProcessor() {
        return LibraryDTO::toEntity;
    }

    @Bean(name = "library_entity_writer")
    @StepScope
    public JpaItemWriter<LibraryTmpEntity> jpaItemWriter() {
        JpaItemWriter<LibraryTmpEntity> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
