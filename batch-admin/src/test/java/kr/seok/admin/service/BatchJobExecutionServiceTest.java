package kr.seok.admin.service;

import kr.seok.admin.domain.BatchJobExecution;
import kr.seok.admin.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class BatchJobExecutionServiceTest {

    private BatchJobExecutionService batchJobExecutionService;

    @Mock
    private BatchJobExecutionRepository batchJobExecutionRepository;
    @Mock
    private BatchJobExecutionContextRepository batchJobExecutionContextRepository;
    @Mock
    private BatchJobExecutionParamsRepository batchJobExecutionParamsRepository;
    @Mock
    private BatchJobInstanceRepository batchJobInstanceRepository;
    @Mock
    private BatchStepExecutionRepository batchStepExecutionRepository;
    @Mock
    private BatchStepExecutionContextRepository batchStepExecutionContextRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        batchJobExecutionService = new BatchJobExecutionService(
                batchJobExecutionRepository,
                batchJobExecutionContextRepository,
                batchJobExecutionParamsRepository,
                batchJobInstanceRepository,
                batchStepExecutionRepository,
                batchStepExecutionContextRepository);
    }

    @Test
    public void list() {
        List<BatchJobExecution> list = batchJobExecutionService.getBatchList();
        for(BatchJobExecution job : list) {
            System.out.println(job);
        }

    }
}