package kr.seok.library.demo;

import kr.seok.library.repository.CityRepository;
import kr.seok.library.repository.CountryRepository;
import kr.seok.library.repository.LibraryRepository;
import kr.seok.library.repository.TmpRepository;
import kr.seok.library.step.FileToTmpStep;
import kr.seok.library.step.MultiDBToReportStep;
import kr.seok.library.step.TmpToMultiDbStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FileToTmp
 * TmpToMultiDB
 * MultiDBToReport
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibrarySummaryDemo {
    /* Batch */
    private static final String JOB_NAME = "LIBRARY_SUMMARY";
    private final JobBuilderFactory jobBuilderFactory;

    /* Step Class */
    private final FileToTmpStep fileToTmpStep;
    private final TmpToMultiDbStep tmpToMultiDbStep;
    private final MultiDBToReportStep multiDBToReportStep;

    private final TmpRepository tmpRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LibraryRepository libraryRepository;

    @Bean(name  = JOB_NAME + "_JOB")
    public Job tmpToMultiJob() {
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
                .start(fileToTmpStep.fileToTmpStep())
                .next(tmpToMultiDbStep.tmpToMultiDbStep())
                .next(multiDBToReportStep.multiDbToReportStep())
                .build();
    }
}
