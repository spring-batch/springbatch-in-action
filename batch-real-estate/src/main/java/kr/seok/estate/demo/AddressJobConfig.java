package kr.seok.estate.demo;

import kr.seok.estate.domain.dto.FileLineDto;
import kr.seok.estate.domain.entity.AreaEntity;
import kr.seok.estate.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import sun.nio.cs.ext.EUC_KR;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AddressJobConfig {

    private static final String JOB_NAME = "AREA";
    private static final Integer CHUNK_SIZE = 10000;

    @Value("${file.area}")
    private String filePath;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private final AreaRepository areaRepository;


    @Bean
    public Job areaJob() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .incrementer(new RunIdIncrementer())
                .start(areaStep())
                .build();
    }

    /* File Reader -> File :: Entity -> Entity 프로세스 Step */
    private Step areaStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        areaRepository.deleteAllInBatch();
                        log.info("[LOG] [INIT] [TB_AREA] [{}]", areaRepository.count());
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        log.info("[LOG] [COMMIT_CNT] [TB_AREA] [{}]", stepExecution.getCommitCount());
                        log.info("[LOG] [WRITE_CNT] [TB_AREA] [{}]", stepExecution.getWriteCount());
                        return ExitStatus.COMPLETED;
                    }
                })
                .<FileLineDto, AreaEntity>chunk(CHUNK_SIZE)
                .reader(txtFlatFileReader())
                .processor((ItemProcessor<FileLineDto, AreaEntity>) FileLineDto::toAreaEntity)
                .writer(areaWriter())
                .build();
    }

    /* Text - FlatFileItemReader */
    private ItemReader<? extends FileLineDto> txtFlatFileReader() {
        return new FlatFileItemReader<FileLineDto>() {{
            setResource(new ClassPathResource(filePath));
            setEncoding(new EUC_KR().historicalName());
            setLinesToSkip(1);
            setLineMapper((line, lineNumber) -> new FileLineDto(line));
        }};
    }

    /* File 필드 값을 AreaEntity에 저장하여 DB에 적재 */
    private ItemWriter<? super AreaEntity> areaWriter() {
        return new JpaItemWriterBuilder<>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
