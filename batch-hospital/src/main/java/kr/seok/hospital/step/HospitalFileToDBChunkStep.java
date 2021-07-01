package kr.seok.hospital.step;

import kr.seok.hospital.domain.Hospital;
import kr.seok.hospital.domain.dto.HospitalFileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;

import static org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_COMMA;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HospitalFileToDBChunkStep {

    @Value("${chunkSize:1000}")
    public int chunkSize;

    /* 38s7ms */
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private static final String STEP_NAME = "STEP_H_FileToDB";

    /**
     * H file to db step step.
     *
     * @param filePath 파일 경로를 파라미터로 받음
     * @return step
     */
    public Step hFileToDbStep(String filePath) {
        return stepBuilderFactory.get(STEP_NAME)
                .<HospitalFileDto, Hospital>chunk(chunkSize)
                .reader(fileReader(filePath))
                .processor(fileToDbProcessor())
                .writer(dbWriter())
                .build();
    }

    private ItemReader<? extends HospitalFileDto> fileReader(String filePath) {
        return new FlatFileItemReader<HospitalFileDto>() {{
            setResource(new ClassPathResource(filePath));
            setLinesToSkip(1);
            setLineMapper(new DefaultLineMapper<HospitalFileDto>() {{
                setLineTokenizer(
                        new DelimitedLineTokenizer(DELIMITER_COMMA) {{
                            /* 필드명 리스트 반환 */
                            setNames(HospitalFileDto.getFields());
                        }}
                );
                setFieldSetMapper(new BeanWrapperFieldSetMapper<HospitalFileDto>() {{
                    setTargetType(HospitalFileDto.class);
                }});
            }});
        }};
    }

    private ItemProcessor<? super HospitalFileDto, ? extends Hospital> fileToDbProcessor() {
        return HospitalFileDto::toEntity;
    }

    private ItemWriter<? super Hospital> dbWriter() {
        return new JpaItemWriterBuilder<>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
