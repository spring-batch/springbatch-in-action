package kr.seok.hospital.demo;

import kr.seok.hospital.step.Step_H_DbToDb_tasklet;
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
public class Serial_H_DBToDBConfig {

    private static final String JOB_NAME = "STEP_H_DbToDb";

    private final JobBuilderFactory jobBuilderFactory;
    private final Step_H_DbToDb_tasklet h_dbToDb_tasklet;

    @Bean(name = JOB_NAME + "_JOB")
    public Job hDbToDb() {

        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .incrementer(new RunIdIncrementer())
                .start(h_dbToDb_tasklet.hDbToDbStep())
                .build(); // job instance
    }

}
