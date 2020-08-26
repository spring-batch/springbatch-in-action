package com.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * implements StepExecutionListener 를 구현
 * 또는 @BeforeStep, @AfterStep 어노테이션 설정으로 적용 가능
 */
@Slf4j
@Component
public class CustomStepListener //implements StepExecutionListener
{

    StopWatch stopWatch = new StopWatch("Step Watch");

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        stopWatch.start("initializing Step");
        if(stepExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Before Step");
        } else if(stepExecution.getStatus() == BatchStatus.FAILED) {
            log.info("Before Step Failed");
        }
    }

    @AfterStep()
    public void afterStep(StepExecution stepExecution) {
        if(stepExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("After Step");
        } else if(stepExecution.getStatus() == BatchStatus.FAILED) {
            log.info("After Step Failed");
        }
        stopWatch.stop();
        log.info("{} : {}", stopWatch.getTaskInfo(), stopWatch.getTotalTimeSeconds());
    }
}

