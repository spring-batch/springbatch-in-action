package kr.seok.library.listener;

import kr.seok.library.domain.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CityEntityStepListener implements StepExecutionListener {

    private final CityRepository cityRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        cityRepository.deleteAllInBatch();
        log.info("[LOG] [TB_CITY] [INITIALIZE] [SIZE] [{}]", cityRepository.count());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("[LOG] [TB_CITY] [FINALLY END] [TOTAL_SIZE] [{}]", cityRepository.count());
        return ExitStatus.EXECUTING;
    }
}
