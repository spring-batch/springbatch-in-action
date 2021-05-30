package kr.seok.library.demo;

import kr.seok.library.repository.CityRepository;
import kr.seok.library.repository.CountryRepository;
import kr.seok.library.repository.LibraryRepository;
import kr.seok.library.repository.TmpRepository;
import kr.seok.library.step.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * 아래 순서로 전체 프로세스를 수행하는 Job
 * 1. 파일을 읽고 임시테이블에 적재하는 작업
 * 2. 임시테이블에 적재된 데이터를 가공하여 City, Country, Library 테이블에 적재하는 작업
 * 3. City, Country, Library 테이블에서 필요한 데이터만을 가공하여 Report 작성
 */
@Configuration
@RequiredArgsConstructor
public class TotalProcessDemo {
    /* Batch */
    private static final String JOB_NAME = "TOTAL_PROCESS_JOB";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /* DB: Jpa */
    private final EntityManagerFactory entityManagerFactory;
    private final TmpRepository tmpRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LibraryRepository libraryRepository;
    private final DataSource datasource;

    /* 전체 프로세스*/
    @Bean(name = JOB_NAME)
    public Job totalProcess() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(new JobExecutionListener() {
                    /* 배치 데이터 가공하여 적재하기 전 테이블 비우기 */
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
                .start(new FileToTmpStep(stepBuilderFactory, entityManagerFactory).fileToTmpStep())
                .next(new TmpToCityStep(stepBuilderFactory, cityRepository, entityManagerFactory).tmpToCityStep())
                .next(new TmpToCountryStep(stepBuilderFactory, entityManagerFactory, cityRepository).tmpToCountryStep())
                .next(new TmpToLibraryStep(stepBuilderFactory, entityManagerFactory, cityRepository, countryRepository).tmpToLibraryStep())
                .next(new MultiDBToReportStep(stepBuilderFactory, datasource).multiDbToReportStep())
                .build();

    }
}
