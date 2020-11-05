package kr.seok.library.demo;

import kr.seok.library.repository.CityRepository;
import kr.seok.library.repository.CountryRepository;
import kr.seok.library.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

import static kr.seok.library.common.Constants.CHUNK_SIZE;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MultiDBToReportDemo {

    /* Batch */
    private static final String JOB_NAME = "MULTI_DB_TO_REPORT";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /* JPA */
    private final EntityManagerFactory entityManagerFactory;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LibraryRepository libraryRepository;

//    @Bean(name = JOB_NAME + "_JOB")
    public Job multiDbToReportJob() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .start(multiDbToReportStep())
                .build();
    }

    private Step multiDbToReportStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .chunk(CHUNK_SIZE)
                .reader(null)
                .build();
    }
}
