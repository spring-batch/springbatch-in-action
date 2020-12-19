package kr.seok.library.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ManyEntityToExcelStep {
    private final String STEP_NAME = "JPA_VERSION_STEP_THREE_MANY_ENTITY_TO_EXCEL";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jpaManyToExcelJob() {
        return jobBuilderFactory.get(STEP_NAME + "_JOB")
                .incrementer(new RunIdIncrementer())
                .start(jpaManyToExcelStep())
                .build();
    }

    private Step jpaManyToExcelStep() {
        return null;
    }
}
