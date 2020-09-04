package com.batch.demo.library;

import com.batch.domain.batch.LibraryTmpEntity;
import com.batch.domain.batch.Sido;
import com.batch.writer.ConsoleItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpDbToLibraryDbJobDemo {

    private static final String JOB_NAME = "LIBRARY_TMP_TO_LIBRARY_JOB";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    private static final int CHUNK_SIZE = 1000;

    @Bean(name = JOB_NAME)
    public Job libraryTmpToLibraryJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(libraryTmpToLibraryStep())
                .build();
    }

    @Bean(name = JOB_NAME + "_STEP")
    public Step libraryTmpToLibraryStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<LibraryTmpEntity, Sido>chunk(CHUNK_SIZE)

                .reader(tmpToLibraryReader())

                .writer(new ConsoleItemWriter<>())

                .build();
    }

    private ItemReader<? extends LibraryTmpEntity> tmpToLibraryReader() {
        return new JpaPagingItemReader<LibraryTmpEntity>() {{

        }};
    }
}
