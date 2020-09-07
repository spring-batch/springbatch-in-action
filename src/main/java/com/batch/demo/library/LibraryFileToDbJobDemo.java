package com.batch.demo.library;

import com.batch.common.listener.CustomItemProcessorListener;
import com.batch.common.listener.CustomItemReaderListener;
import com.batch.common.listener.CustomItemWriterListener;
import com.batch.common.listener.CustomStepListener;
import com.batch.demo.library.domain.LibraryDTO;
import com.batch.demo.library.domain.LibraryTmpEntity;
import com.batch.demo.library.domain.enums.LibraryTmpEntityFields;
import com.batch.demo.library.listener.LibraryTmpJobListener;
import com.batch.demo.library.repository.LibraryTmpEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
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

    private final LibraryTmpEntityRepository libraryTmpEntityRepository;

    private static final String JOB_NAME = "LIBRARY_FILE_TO_TMP_JOB";
    private static final int CHUNK_SIZE = 1000;

    @Bean(name = JOB_NAME)
    public Job libraryFileToDbJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(libraryFileToDbStep())
                .listener(new LibraryTmpJobListener(libraryTmpEntityRepository))
                .build();
    }

    @Bean(name = JOB_NAME + "_STEP")
    public Step libraryFileToDbStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<LibraryDTO, LibraryTmpEntity>chunk(CHUNK_SIZE)

                .listener(new CustomItemReaderListener())
                .reader(flatFileReader())

                .listener(new CustomItemProcessorListener<>())
                .processor(fileToDbProcessor())

                .listener(new CustomItemWriterListener<>())
                .writer(jpaItemWriter())

                /* Step Listener */
                .listener(new CustomStepListener())
                .build();
    }

    /**
     * FlatFileItemReader Custom
     *
     * @return FlatFileItemReader
     */
    @StepScope
    @Bean(name = "LIBRARY_FILE_READER")
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

    @Bean(name = "LIBRARY_DTO_TO_ENTITY_PROCESSOR")
    @StepScope
    public ItemProcessor<? super LibraryDTO, ? extends LibraryTmpEntity> fileToDbProcessor() {
        return LibraryDTO::toEntity;
    }

    @Bean(name = "LIBRARY_ENTITY_WRITER")
    @StepScope
    public JpaItemWriter<LibraryTmpEntity> jpaItemWriter() {
        return new JpaItemWriter<LibraryTmpEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
