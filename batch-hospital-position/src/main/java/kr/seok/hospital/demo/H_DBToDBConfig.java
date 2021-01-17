package kr.seok.hospital.demo;

import kr.seok.hospital.domain.Hospital;
import kr.seok.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class H_DBToDBConfig {
    private static final String JOB_NAME = "H_DBToDB";
    private final JobBuilderFactory jobBuilderFactory;

    @Bean(name = JOB_NAME + "_JOB")
    public Job hDbToDb() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .incrementer(new RunIdIncrementer())
                .start(hDbToDbStep())
                .build();
    }

    private final StepBuilderFactory stepBuilderFactory;
    private final HospitalRepository hospitalRepository;

    private Step hDbToDbStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .tasklet((contribution, chunkContext) -> {

                    List<Hospital> hospitals = hospitalRepository.findAll();

                    hospitals.forEach(h -> log.info("hos -> {}", h));

                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
