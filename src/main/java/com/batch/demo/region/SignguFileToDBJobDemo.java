package com.batch.demo.region;

import com.batch.demo.library.domain.Signgu;
import com.batch.demo.region.domain.SignguCSV;
import com.batch.common.listener.CustomItemProcessorListener;
import com.batch.common.listener.CustomItemReaderListener;
import com.batch.common.listener.CustomItemWriterListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;

import static org.springframework.batch.item.file.transform.DelimitedLineTokenizer.DELIMITER_COMMA;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SignguFileToDBJobDemo {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static final String JOB_NAME = "SIGNGU_FILE_TO_DB_JOB";

    @Bean(name = JOB_NAME)
    public Job signguFileToDBJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(signguFileToDBStep())
                .build();
    }

    @Bean
    public Step signguFileToDBStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<SignguCSV, Signgu>chunk(1000)

                .listener(new CustomItemReaderListener())
                .reader(signguFileReader())

                .processor(fileToDbProcessor())
                .listener(new CustomItemProcessorListener<>())

                .writer(signguDbWriter())
                .listener(new CustomItemWriterListener<>())
                .build();
    }

    @Bean
    public ItemReader<? extends SignguCSV> signguFileReader() {
        return new FlatFileItemReader<SignguCSV>() {{
            setResource(new ClassPathResource("files/행정구역분류표.csv"));
            setLinesToSkip(1);
            setLineMapper(new DefaultLineMapper<SignguCSV>() {{
                setLineTokenizer(new DelimitedLineTokenizer(DELIMITER_COMMA) {{
                    setNames(SignguCSV.SignguFields.getFieldNmArrays());
                }});
                setFieldSetMapper(new BeanWrapperFieldSetMapper<SignguCSV>() {{
                    setTargetType(SignguCSV.class);
                }});
            }});
        }};
    }

    @Bean
    public ItemProcessor<? super SignguCSV,? extends Signgu> fileToDbProcessor() {
        return signguCSV -> {
            if(signguCSV.getMidClass() != null) return signguCSV.toEntity();
            return null;
        };
    }

    @Bean
    public JpaItemWriter<Signgu> signguDbWriter() {
        return new JpaItemWriter<Signgu>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
