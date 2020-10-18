package kr.seok.admin.service;

import kr.seok.admin.domain.BatchJobExecution;
import kr.seok.admin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BatchJobExecutionService {

    private final BatchJobExecutionRepository batchJobExecutionRepository;
    private final BatchJobExecutionContextRepository batchJobExecutionContextRepository;
    private final BatchJobExecutionParamsRepository batchJobExecutionParamsRepository;
    private final BatchJobInstanceRepository batchJobInstanceRepository;
    private final BatchStepExecutionRepository batchStepExecutionRepository;
    private final BatchStepExecutionContextRepository batchStepExecutionContextRepository;

    @Autowired
    public BatchJobExecutionService(
            BatchJobExecutionRepository batchJobExecutionRepository,
            BatchJobExecutionContextRepository batchJobExecutionContextRepository,
            BatchJobExecutionParamsRepository batchJobExecutionParamsRepository,
            BatchJobInstanceRepository batchJobInstanceRepository,
            BatchStepExecutionRepository batchStepExecutionRepository,
            BatchStepExecutionContextRepository batchStepExecutionContextRepository
    ) {
        this.batchJobExecutionRepository = batchJobExecutionRepository;
        this.batchJobExecutionContextRepository = batchJobExecutionContextRepository;
        this.batchJobExecutionParamsRepository = batchJobExecutionParamsRepository;
        this.batchJobInstanceRepository = batchJobInstanceRepository;
        this.batchStepExecutionRepository = batchStepExecutionRepository;
        this.batchStepExecutionContextRepository = batchStepExecutionContextRepository;
    }

    @Transactional(readOnly = true)
    public List<BatchJobExecution> getBatchList() {
        List<BatchJobExecution> list = batchJobExecutionRepository.findAll();
        return list;
    }
}
