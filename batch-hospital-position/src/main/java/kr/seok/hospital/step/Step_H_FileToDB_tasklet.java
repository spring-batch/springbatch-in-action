package kr.seok.hospital.step;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Step_H_FileToDB_tasklet {

    /* 38s7ms */
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(name = "STEP_H_FileToDB")
    public Step hFileToDbStep() {
        return stepBuilderFactory.get("STEP_H_FileToDB")
                .tasklet((contribution, chunkContext) -> {

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
