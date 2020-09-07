package com.batch.demo.library.listener;

import com.batch.demo.library.repository.SidoEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

@Slf4j
public class SidoStep2Listener implements StepExecutionListener {
    private SidoEntityRepository sidoEntityRepository;

    public SidoStep2Listener(SidoEntityRepository sidoEntityRepository) {
        this.sidoEntityRepository = sidoEntityRepository;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        sidoEntityRepository.deleteAll();
        int sidoTableSize = sidoEntityRepository.findAll().size();
        log.info("[LOG] [TB_SIDO] [INITIALIZE] [SIZE] [{}]", sidoTableSize);
        if(sidoTableSize != 0) throw new RuntimeException("데이터 삭제 오류");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.EXECUTING;
    }
}
