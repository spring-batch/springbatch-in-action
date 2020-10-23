package kr.seok.library.demo;

import kr.seok.library.repository.CityRepository;
import kr.seok.library.repository.CountryRepository;
import kr.seok.library.repository.LibraryRepository;
import kr.seok.library.repository.TmpRepository;
import kr.seok.library.step.FileToTmpStep;
import kr.seok.library.step.TmpToCityStep;
import kr.seok.library.step.TmpToCountryStep;
import kr.seok.library.step.TmpToLibraryStep;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;

@Transactional
@Configuration
@RequiredArgsConstructor
public class TotalProcessDemo {
    /* Batch */
    private static final String JOB_NAME = "TOTAL_PROCESS";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /* DB: Jpa */
    private final EntityManagerFactory entityManagerFactory;
    private final TmpRepository tmpRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LibraryRepository libraryRepository;

    @Bean(name = JOB_NAME + "_JOB")
    public Job totalProcess() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
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
                .start(new FileToTmpStep(stepBuilderFactory, entityManagerFactory).fileToTmpStep())
                .next(new TmpToCityStep(stepBuilderFactory, cityRepository, entityManagerFactory).tmpToCityStep())
                .next(new TmpToCountryStep(stepBuilderFactory, entityManagerFactory, cityRepository).tmpToCountryStep())
                .next(new TmpToLibraryStep(stepBuilderFactory, entityManagerFactory, cityRepository, countryRepository).tmpToLibraryStep())
                .build();

    }
}