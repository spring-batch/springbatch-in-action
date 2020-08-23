package com.batch.sample;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * 단일 작업용 샘플 클래스
 */
public class CustomTasklet implements Tasklet {

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        /* Execute 실행 전 실행되는 Listener */
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {
        /* Execute 실행 후 실행되는 Listener */
        return null;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        /* Tasklet 작업의 메인이 되는 메서드 */
        return null;
    }
}
