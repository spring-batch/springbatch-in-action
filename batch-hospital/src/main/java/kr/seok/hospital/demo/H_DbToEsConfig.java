package kr.seok.hospital.demo;

import kr.seok.hospital.domain.dto.HospitalEsEntity;
import kr.seok.hospital.repository.HospitalInfJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class H_DbToEsConfig {

    private static final String JOB_NAME = "H_DbToEs";
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;

    private final HospitalInfJpaRepository hospitalInfJpaRepository;

    @Bean(name = JOB_NAME + "_JOB")
    public Job hDbToEs() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .incrementer(new RunIdIncrementer())
                .start(hDbToEsStep())
                .build();
    }

    private Step hDbToEsStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .tasklet((contribution, chunkContext) -> {

                    List<HospitalEsEntity> esEntities = hospitalInfJpaRepository.findLeftJoinAll();
//                    esEntities.forEach(hosEs -> System.out.println(hosEs));

                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
