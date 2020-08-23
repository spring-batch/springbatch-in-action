package com.batch.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 간단하게 사용하기 위한 Batch Job <br />
 *      1. Job
 *      2. Step
 *          2.1 tasklet
 *              - 데이테 조회 및 출력
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BasicJobBuilder {

    private static final String JOB_NAME = "basicJob";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job basicJob() {
        return this.jobBuilderFactory.get(JOB_NAME)
                .start(basicStep())
                .build();
    }

    @Bean
    public Step basicStep() {
        return this.stepBuilderFactory.get(JOB_NAME + "_STEP")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Step Tasklet");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
