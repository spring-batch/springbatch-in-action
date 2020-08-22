package com.batch.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


}
