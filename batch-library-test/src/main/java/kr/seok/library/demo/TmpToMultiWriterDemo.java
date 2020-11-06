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
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
public class TmpToMultiWriterDemo {
    /* Batch */
    private static final String JOB_NAME = "TMP_TO_MULTI_DB_WRITER";
    private final JobBuilderFactory jobBuilderFactory;

    /* Step Class */
    private final FileToTmpStep fileToTmpStep;
    private final TmpToMultiDbStep tmpToMultiDbStep;
    private final MultiDBToReportStep multiDBToReportStep;

    @Bean(name  = JOB_NAME + "_JOB")
    public Job tmpToMultiJob() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .incrementer(new RunIdIncrementer())
                .start(fileToTmpStep.fileToTmpStep())
                .next(tmpToMultiDbStep.tmpToMultiDbStep())
                .next(multiDBToReportStep.multiDbToReportStep())
                .build();
    }
}
