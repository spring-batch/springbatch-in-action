package kr.seok.library.step;

import kr.seok.library.domain.entity.LibraryTmpEntity;
import kr.seok.library.domain.vo.LibraryFileDto;
import kr.seok.library.listener.TmpEntityStepListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import sun.nio.cs.ext.EUC_KR;

import javax.persistence.EntityManagerFactory;

import static org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_COMMA;

/**
 * 파일 읽어서 임시테이블에 넣는 Step
 */
@Configuration
@RequiredArgsConstructor
public class FileToTmpStep {
    /* Batch */
    private static final String STEP_NAME = "JPA_VERSION_STEP_ONE";
    private final StepBuilderFactory stepBuilderFactory;
    private final TmpEntityStepListener tmpEntityStepListener;
    /* DB */
    private final EntityManagerFactory entityManagerFactory;

    /* TODO 파일 경로를 외부 파라미터로 받을 수 있도록 해야함, 파라미터로 들어오지 않은 경우 기본 경로도 필요 */
    @Value("${file.library}")
    private String filePath;

    private static final int CHUNK_SIZE = 1000;

    /* 파일 읽어서 임시 테이블에 저장하는 Step */
    @Bean(name = STEP_NAME)
    public Step fileToTmpStep() {

        return stepBuilderFactory.get(STEP_NAME)
                .listener(tmpEntityStepListener)
                .<LibraryFileDto, LibraryTmpEntity>chunk(CHUNK_SIZE)
                .reader(fileReader())
                .processor(fileToTmpProcessor())
                .writer(tmpWriter())
                .build();
    }

    /* 파일 읽는 Reader */
    private ItemReader<? extends LibraryFileDto> fileReader() {
        return new FlatFileItemReader<LibraryFileDto>() {{
            setResource(new ClassPathResource(filePath));
            setEncoding(new EUC_KR().historicalName());
            setLinesToSkip(1);
            setLineMapper(new DefaultLineMapper<LibraryFileDto>() {{
                setLineTokenizer(
                        new DelimitedLineTokenizer(DELIMITER_COMMA) {{
                            /* TODO 파일 필드 값을 필드의 어노테이션 등록 형식으로 변경해보기 */
                            setNames(LibraryFileDto.LibraryFileFields.getFieldNms());
                            setIncludedFields(0, 1, 2, 3);
                        }});
                setFieldSetMapper(new BeanWrapperFieldSetMapper<LibraryFileDto>() {{
                    setTargetType(LibraryFileDto.class);
                }});
            }});
        }};
    }

    /* 파일 데이터를 읽어서 TmpEntity로 매핑하여 Persistence Context에 저장 */
    private ItemProcessor<? super LibraryFileDto,? extends LibraryTmpEntity> fileToTmpProcessor() {
        return LibraryFileDto::toEntity;
    }

    /* TmpEntity Persistence Context cache에 저장되어 있는 데이터를 DB에 Flush > transaction > commit */
    private ItemWriter<? super LibraryTmpEntity> tmpWriter() {
        return new JpaItemWriter<LibraryTmpEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
