package kr.seok.library.demo.prototype;

import kr.seok.library.step.MultiDBToReportStep;
import kr.seok.library.step.TmpToMultiDbStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Multi DB to Report 프로세스를 수행하는 하나의 Step을 실행하는 Job
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MultiDBToReportPrototype {

    /* Batch */
    private static final String JOB_NAME = "MULTI_DB_TO_REPORT";
    private final JobBuilderFactory jobBuilderFactory;

    /* Step Class */
    private final MultiDBToReportStep multiDBToReportStep;

    /* Multi DB to Report Job */
    @Bean(name = JOB_NAME + "_JOB")
    public Job multiDbToReportJob() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .incrementer(new RunIdIncrementer())
                .start(multiDBToReportStep.multiDbToReportStep())
                .build();
    }

}
