package kr.seok.hospital.demo;

import kr.seok.hospital.flow.Flow_H_DBToDtt;
import kr.seok.hospital.flow.Flow_H_DBToInf;
import kr.seok.hospital.flow.Flow_H_DBToPos;
import kr.seok.hospital.flow.Flow_H_DbOrigin;
import kr.seok.hospital.listener.Listener_H_DB;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Parallel_H_DbToDbConfig {

    private static final String JOB_NAME = "FLOW_H_DbToDb";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final Listener_H_DB listener_h_db;

    private final Flow_H_DbOrigin flow_h_dbOrigin;
    private final Flow_H_DBToInf flow_h_dbToInf;
    private final Flow_H_DBToDtt flow_h_dbToDtt;
    private final Flow_H_DBToPos flow_h_dbToPos;

    @Transactional
    @Bean(name = JOB_NAME + "_JOB")
    public Job hDbToDb() {

        Flow flow1 = new FlowBuilder<Flow>("flow1")
                .start(step1())
                .build();

        Flow flow2 = new FlowBuilder<Flow>("flow2")
                .start(step2())
                .build();

        Flow flow3 = new FlowBuilder<Flow>("flow3")
                .start(step3())
                .build();
        Flow flow4 = new FlowBuilder<Flow>("flow4")
                .start(step4())
                .build();

        Flow parallelStepFlow = new FlowBuilder<Flow>("parallelStepFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(flow2, flow3, flow4)
                .build();

        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .listener(listener_h_db)
                .incrementer(new RunIdIncrementer())
                    // 데이터 조회
                    .start(flow1)
                    // DB 데이터 쓰기
                    .next(parallelStepFlow)
                    .build() // flowJobBuilder instance
                .build(); // job instance
    }

    private Step step4() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP4")
                .tasklet(flow_h_dbToPos)
                .build();
    }

    private Step step3() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP3")
                .tasklet(flow_h_dbToDtt)
                .build();
    }

    private Step step2() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP2")
                .tasklet(flow_h_dbToInf)
                .build();
    }

    private Step step1() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP1")
                .tasklet(flow_h_dbOrigin)
                .build();
    }
}
