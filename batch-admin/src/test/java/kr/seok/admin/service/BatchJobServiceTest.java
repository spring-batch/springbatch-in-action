package kr.seok.admin.service;

import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.repository.BatchJobInstanceJpaRepository;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BatchJobServiceTest {

    private BatchJobService batchJobService;
    //
//    @Mock
//    private BatchJobExecutionRepository batchJobExecutionRepository;
//    @Mock
//    private BatchJobExecutionContextRepository batchJobExecutionContextRepository;
//    @Mock
//    private BatchJobExecutionParamsRepository batchJobExecutionParamsRepository;
    @Mock
    private BatchJobInstanceJpaRepository batchJobInstanceRepository;

    //    @Mock
//    private BatchStepExecutionRepository batchStepExecutionRepository;
//    @Mock
//    private BatchStepExecutionContextRepository batchStepExecutionContextRepository;
//
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        batchJobService = new BatchJobService(
//                batchJobExecutionRepository,
//                batchJobExecutionContextRepository,
//                batchJobExecutionParamsRepository,
                batchJobInstanceRepository
//                batchStepExecutionRepository,
//                batchStepExecutionContextRepository
        );
    }
//
//    @Test
//    public void list() {
//
//    }

    @Test
    @DisplayName("테스트")
    void testCase2() {
        BatchJobInstance jobInstances = batchJobService.getJobInstances(1393L);
        System.out.println(jobInstances);
    }
}
