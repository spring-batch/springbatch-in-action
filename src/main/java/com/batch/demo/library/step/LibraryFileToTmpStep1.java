package com.batch.demo.library.step;

import com.batch.demo.library.domain.LibraryDTO;
import com.batch.demo.library.domain.LibraryTmpEntity;
import com.batch.demo.library.domain.enums.LibraryTmpEntityFields;
import com.batch.demo.library.listener.LibraryTmpStepListener;
import com.batch.demo.library.repository.LibraryTmpEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
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

import static com.batch.demo.library.LibraryTotalToReportFileJobDemo.CHUNK_SIZE;
import static org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_COMMA;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryFileToTmpStep1 {

    private static final String STEP_NAME = "LIBRARY_FILE_TO_TMP_STEP1";

    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final LibraryTmpEntityRepository libraryTmpEntityRepository;

    @JobScope
    @Bean(name = STEP_NAME)
    public Step fileToTmpStep1() {
        return stepBuilderFactory.get(STEP_NAME)
                .<LibraryDTO, LibraryTmpEntity>chunk(CHUNK_SIZE)

                .reader(libraryFileStep1Reader())
                .processor(libraryDtoToEntityStep1Processor())
                .writer(libraryEntityStep1Writer())

                .listener(new LibraryTmpStepListener(libraryTmpEntityRepository))

                .build();
    }

    @StepScope
    @Bean(name = STEP_NAME + "_READER")
    public FlatFileItemReader<LibraryDTO> libraryFileStep1Reader() {
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

    @StepScope
    @Bean(name = STEP_NAME + "_PROCESSOR")
    public ItemProcessor<? super LibraryDTO, ? extends LibraryTmpEntity> libraryDtoToEntityStep1Processor() {
        return LibraryDTO::toEntity;
    }

    @StepScope
    @Bean(name = STEP_NAME + "_WRITER")
    public JpaItemWriter<LibraryTmpEntity> libraryEntityStep1Writer() {
        return new JpaItemWriter<LibraryTmpEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
