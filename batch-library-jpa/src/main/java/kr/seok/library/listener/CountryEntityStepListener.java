package kr.seok.library.listener;

import kr.seok.library.domain.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CountryEntityStepListener implements StepExecutionListener {

    private final CountryRepository countryRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        countryRepository.deleteAllInBatch();
        log.info("[LOG] [TB_COUNTRY] [INITIALIZE] [SIZE] [{}]", countryRepository.count());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("[LOG] [TB_COUNTRY] [FINALLY END] [TOTAL_SIZE] [{}]", countryRepository.count());
        return ExitStatus.EXECUTING;
    }
}
