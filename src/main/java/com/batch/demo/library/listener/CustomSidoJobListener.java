package com.batch.demo.library.listener;

import com.batch.demo.library.repository.SidoEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class CustomSidoJobListener<T> implements JobExecutionListener {
    private SidoEntityRepository sidoEntityRepository;

    public CustomSidoJobListener(SidoEntityRepository sidoEntityRepository) {
        this.sidoEntityRepository = sidoEntityRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        sidoEntityRepository.deleteAll();
        log.info("[LOG] [TB_SIDO] [DELETE_ALL] [CHECK SIZE:{}]", sidoEntityRepository.findAll().size());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
    }
}
