package kr.seok.hospital.demo;

import kr.seok.hospital.step.Step_H_DbToDb_tasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 한 번에 읽어서 각각 테이블에 쓰기
 *
 * [2021-07-01 16:44:05.094]  INFO [o.s.b.c.s.AbstractStep.execute:272] - Step: [STEP_H_DbToDb_STEP] executed in 2m59s137ms
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class HospitalDBToDBSerialTaskletConfig {

    private static final String JOB_NAME = "STEP_H_DbToDb";

    private final JobBuilderFactory jobBuilderFactory;
    private final Step_H_DbToDb_tasklet h_dbToDb_tasklet;

    /**
     * 임시 테이블에 저장된 데이터를 분류 별로 나누어 놓은 테이블에 순차적으로 저장하는 Job
     *
     * @return Db to Db Job
     */
    @Bean(name = JOB_NAME + "_JOB")
    public Job hDbToDb() {

        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .incrementer(new RunIdIncrementer())
                .start(h_dbToDb_tasklet.hDbToDbStep())
                .build(); // job instance
    }

}
