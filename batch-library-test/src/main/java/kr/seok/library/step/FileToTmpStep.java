package kr.seok.library.step;

import kr.seok.library.domain.FileCSVDto;
import kr.seok.library.domain.FileDto;
import kr.seok.library.domain.entity.TmpEntity;
import kr.seok.library.repository.TmpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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

import static kr.seok.common.excel.utils.SuperClassReflectionUtils.getAllFieldNames;
import static kr.seok.library.common.Constants.CHUNK_SIZE;
import static org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_COMMA;

/**
 * 파일 읽어서 임시테이블에 넣는 Step
 */
@Configuration
public class FileToTmpStep {
    /* Batch */
    private static final String STEP_NAME = "FILE_TO_TMP_STEP";
    private final StepBuilderFactory stepBuilderFactory;

    /* DB */
    private final EntityManagerFactory entityManagerFactory;

    public FileToTmpStep(StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
    }

    /* 파일 읽어서 임시 테이블에 저장하는 Step */
    @Bean(name = STEP_NAME)
    public Step fileToTmpStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<FileDto, TmpEntity>chunk(CHUNK_SIZE)
                .reader(fileReader())
                .processor(fileToTmpProcessor())
                .writer(tmpWriter())
                .build();
    }

    /* 파일 읽는 Reader */
    private ItemReader<? extends FileDto> fileReader() {
        return new FlatFileItemReader<FileDto>() {{
            /* 파일 경로 읽기 Resource 설정 */
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

    /* 파일 데이터를 읽어서 TmpEntity로 매핑하여 Persistence Context에 저장 */
    private ItemProcessor<? super FileDto,? extends TmpEntity> fileToTmpProcessor() {
        return FileDto::toEntity;
    }

    /* TmpEntity Persistence Context cache에 저장되어 있는 데이터를 DB에 Flush */
    private ItemWriter<? super TmpEntity> tmpWriter() {
        return new JpaItemWriter<TmpEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
