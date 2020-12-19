package kr.seok.area.job;

import kr.seok.area.domain.FileLineDto;
import kr.seok.area.domain.entity.AreaEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
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
public class AreaJobConfig {

    private final String JOB_NAME = "JPA_AREA";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Value("${file.area}")
    private String filePath;

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
                .<FileLineDto, AreaEntity>chunk(1000)
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
