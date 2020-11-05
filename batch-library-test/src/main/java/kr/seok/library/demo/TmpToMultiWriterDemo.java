package kr.seok.library.demo;

import kr.seok.library.domain.entity.CityEntity;
import kr.seok.library.domain.entity.CountryEntity;
import kr.seok.library.domain.entity.LibraryEntity;
import kr.seok.library.domain.entity.TmpEntity;
import kr.seok.library.repository.CityRepository;
import kr.seok.library.repository.CountryRepository;
import kr.seok.library.repository.LibraryRepository;
import kr.seok.library.repository.TmpRepository;
import kr.seok.library.step.FileToTmpStep;
import kr.seok.library.step.TmpToMultiDbStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kr.seok.library.common.Constants.CHUNK_SIZE;

/**
 * FileToTmp
 * TmpToMultiDB
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class TmpToMultiWriterDemo {
    /* Batch */
    private static final String JOB_NAME = "TMP_TO_MULTI_DB_WRITER_JOB";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /* Jpa Entity Repository */
    private final FileToTmpStep fileToTmpStep;
    private final TmpToMultiDbStep tmpToMultiDbStep;
    private final TmpRepository tmpRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LibraryRepository libraryRepository;

    @Bean(name  = JOB_NAME)
    public Job tmpToMultiJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        tmpRepository.deleteAllInBatch();
                        cityRepository.deleteAllInBatch();
                        countryRepository.deleteAllInBatch();
                        libraryRepository.deleteAllInBatch();
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {

                    }
                })
                .start(fileToTmpStep.fileToTmpStep())
                .next(tmpToMultiDbStep.tmpToMultiDbStep())
                .build();
    }
}
