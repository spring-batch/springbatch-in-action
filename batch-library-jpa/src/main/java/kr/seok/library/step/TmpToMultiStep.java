package kr.seok.library.step;

import kr.seok.library.tasklet.OneToManyTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 임시 테이블 데이터를 정규화된 테이블에 넣는 작업
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class TmpToMultiStep {

    /* Batch */
    private static final String STEP_NAME = "JPA_VERSION_STEP_TWO";
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;

    private final OneToManyTasklet oneToManyTasklet;

    @Bean
    public Job jpaVersionJobTwo() {
        return jobBuilderFactory.get(STEP_NAME + "_JOB")
                .incrementer(new RunIdIncrementer())
                .start(taskStep())
                .build();
    }

    private Step taskStep() {
        return stepBuilderFactory.get(STEP_NAME + "_STEP")
                .tasklet(oneToManyTasklet)
                .build();
    }
}
