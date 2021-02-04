package kr.seok.admin.service;

import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.repository.BatchJobInstanceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BatchJobService {

    private final BatchJobInstanceJpaRepository batchJobInstanceJpaRepository;

    public List<BatchJobInstance> getJobInstance() {
        return batchJobInstanceJpaRepository.findAll();
    }
}
