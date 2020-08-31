package com.batch.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

/**
 * implements JobExecutionListener 를 구현
 * 또는 @BeforeJob, @AfterJob 어노테이션 설정으로 적용 가능
 */
@Slf4j
@Component
public class CustomJobListener implements JobExecutionListener
{
    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Before Job");
        } else if(jobExecution.getStatus() == BatchStatus.FAILED) {
            log.info("Before Job Failed");
        }
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("After Job");
        } else if(jobExecution.getStatus() == BatchStatus.FAILED) {
            log.info("After Job Failed");
        }
    }
}
