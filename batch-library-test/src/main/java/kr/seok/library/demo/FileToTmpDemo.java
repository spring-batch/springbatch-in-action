package kr.seok.library.demo;

import kr.seok.library.domain.FileDto;
import kr.seok.library.domain.TmpEntity;
import kr.seok.library.repository.TmpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
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

import static kr.seok.library.common.Constants.CHUNK_SIZE;
import static org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_COMMA;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileToTmpDemo {

    /* Batch */
    private static final String JOB_NAME = "batch_FILE_TO_TMP";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /* Jpa */
    private final EntityManagerFactory entityManagerFactory;
    private final TmpRepository tmpRepository;

    @Bean(name = JOB_NAME)
    public Job fileToTmpJob() {
        /* JobBuilderFactory을 통한 Job 생성 */
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                /* StepBuilderFactory를 통해 만들어진 Step 시작 */
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        /* 테이블 비우기 */
                        tmpRepository.deleteAllInBatch();
                        long entityCnt = tmpRepository.count();
                        if(entityCnt < 1) {
                            log.debug("테이블 비우기: {}", entityCnt);
                        } else {
                            log.debug("테이블 비우기 실패: {}", entityCnt);
                        }
                    }
                    @Override
                    public void afterJob(JobExecution jobExecution) {
                    }
                })
                .start(fileToTmpStep())
                .build();
    }

    public Step fileToTmpStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<FileDto, TmpEntity>chunk(CHUNK_SIZE)
                /* 읽기 */
                .reader(fileReader())
                /* 데이터 변환 */
                .processor(fileToTmpProcessor())
                /* 쓰기 */
                .writer(tmpWriter())

                .build();
    }

    private ItemWriter<? super TmpEntity> tmpWriter() {
        return new JpaItemWriter<TmpEntity>() {{
           setEntityManagerFactory(entityManagerFactory);
        }};
    }

    private ItemProcessor<? super FileDto,? extends TmpEntity> fileToTmpProcessor() {
        return FileDto::toEntity;
    }

    private ItemReader<FileDto> fileReader() {
        return new FlatFileItemReader<FileDto>() {{
            /* 파일 경로 읽기 Resource 설정 (추후 외부 경로를 파라미터로 받는 방법으로 변경하기) */
            setResource(new ClassPathResource("file/전국도서관표준데이터.csv"));
            /* 파일 인코딩 문제로 EUC-KR로 설정 */
            setEncoding(new EUC_KR().historicalName());
            /* excel 헤더 첫 번째 row Skip*/
            setLinesToSkip(1);

            /* [FlatFileReader 필수 설정] LineMapper 설정하기 */
            setLineMapper(new DefaultLineMapper<FileDto>() {{
                /* [LineMapper 필수 설정] LineTokenizer로 데이터 Mapping */
                setLineTokenizer(
                        /* [DelimitedLineTokenizer 클래스의 필수 값] delimiter 설정 */
                        new DelimitedLineTokenizer(DELIMITER_COMMA) {{
                            /*  LibraryEntitys의 key 값 매핑을 위한 Name 설정 */
                            setNames(FileDto.FileFields.getFieldNms());
                            /* 원본 CSV 파일에서 모든 필드가 필요없어서 필요한 컬럼 값만 조회 */
                            setIncludedFields(0, 1, 2, 3);
                        }});
                /* [LineMapper 필수 설정] FieldSet을 Entity와 매핑 설정*/
                setFieldSetMapper(new BeanWrapperFieldSetMapper<FileDto>() {{
                    /* CSV 파일의 값 부분을 Vo로 매핑 하기 위한 설정 */
                    setTargetType(FileDto.class);
                }});
            }});
        }};
    }
}
