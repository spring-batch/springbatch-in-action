package kr.seok.library.demo;

import kr.seok.library.step.MultiDBToReportStep;
import kr.seok.library.step.TmpToMultiDbStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MultiDBToReportDemo {

    /* Batch */
    private static final String JOB_NAME = "MULTI_DB_TO_REPORT";
    private final JobBuilderFactory jobBuilderFactory;

    /* JPA */
    private final TmpToMultiDbStep tmpToMultiDbStep;
    private final MultiDBToReportStep multiDBToReportStep;

    @Bean(name = JOB_NAME + "_JOB")
    public Job multiDbToReportJob() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .incrementer(new RunIdIncrementer())
                .start(multiDBToReportStep.multiDbToReportStep())
                .build();
    }

}
