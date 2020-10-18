package kr.seok.batch.demo;

import kr.seok.batch.demo.library.domain.LibraryDTO;
import kr.seok.batch.demo.library.domain.LibraryTmpEntity;
import kr.seok.batch.demo.library.domain.enums.LibraryTmpEntityFields;
import kr.seok.batch.demo.library.listener.LibraryTmpJobListener;
import kr.seok.batch.demo.library.repository.LibraryTmpEntityRepository;
import kr.seok.common.listener.CustomItemProcessorListener;
import kr.seok.common.listener.CustomItemReaderListener;
import kr.seok.common.listener.CustomItemWriterListener;
import kr.seok.common.listener.CustomStepListener;
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

/**
 *  도서관 csv 파일을 읽어 CSV_TABLE(임시 저장 테이블) 테이블에 저장
 *      - CSV 파일에 저장되어 있는 Raw 데이터 저장
 */
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
                /* CSV 파일을 읽어 임시 테이블에 저장하는 Step을 호출 */
                .start(libraryFileToDbStep())
                /* 임시 테이블에 저장하기 전 테이블 데이터 비우기 */
                .listener(new LibraryTmpJobListener(libraryTmpEntityRepository))
                .build();
    }

    /* 파일 데이터를 읽어와 CSV 파일 컬럼과 Entity를 매핑하여 임시 테이블에 저장 */
    @Bean(name = JOB_NAME + "_STEP")
    public Step libraryFileToDbStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<LibraryDTO, LibraryTmpEntity>chunk(CHUNK_SIZE)

                /* Console 로그용 Reader Listener */
                .listener(new CustomItemReaderListener<>())
                /* 파일 데이터 읽기위한 FlatFileReader 호출 */
                .reader(flatFileReader())

                /* Console 로그용 Processor Listener */
                .listener(new CustomItemProcessorListener<>())
                /* CSV 파일 데이터를 Entity로 저장하기 위한 Processor */
                .processor(fileToDbProcessor())

                /* Console 로그용 Writer Listener */
                .listener(new CustomItemWriterListener<>())
                /* Jpa EntityManager를 통해 Entity에 저장된 데이터를 Flush 처리 */
                .writer(jpaItemWriter())

                /* Step Listener */
                .listener(new CustomStepListener())
                .build();
    }

    /* FlatFileItemReader를 통해 파일 데이터 읽기 */
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
                /* [LineMapper 필수 설정] FieldSet을 Entity와 매핑 설정*/
                setFieldSetMapper(new BeanWrapperFieldSetMapper<LibraryDTO>() {{
                    /* CSV 파일의 값 부분을 Vo로 매핑 하기 위한 설정 */
                    setTargetType(LibraryDTO.class);
                }});
            }});
        }};
    }

    /* CSV 파일 데이터를 Entity로 저장하기 위한 Processor */
    @Bean(name = "LIBRARY_DTO_TO_ENTITY_PROCESSOR")
    @StepScope
    public ItemProcessor<? super LibraryDTO, ? extends LibraryTmpEntity> fileToDbProcessor() {
        /* DTO to Entity */
        return LibraryDTO::toEntity;
    }

    /* Jpa EntityManager를 통해 Entity에 저장된 데이터를 Flush 처리 */
    @Bean(name = "LIBRARY_ENTITY_WRITER")
    @StepScope
    public JpaItemWriter<LibraryTmpEntity> jpaItemWriter() {
        return new JpaItemWriter<LibraryTmpEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
