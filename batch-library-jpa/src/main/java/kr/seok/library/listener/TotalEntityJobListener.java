package kr.seok.library.listener;

import kr.seok.library.domain.repository.CityRepository;
import kr.seok.library.domain.repository.CountryRepository;
import kr.seok.library.domain.repository.LibraryRepository;
import kr.seok.library.domain.repository.LibraryTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TotalEntityJobListener implements JobExecutionListener {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LibraryTypeRepository libraryTypeRepository;
    private final LibraryRepository libraryRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        libraryRepository.deleteAllInBatch();
        log.info("[LOG] [TB_LIBRARY] [INITIALIZE] [SIZE] [{}]", libraryRepository.count());
        libraryTypeRepository.deleteAllInBatch();
        log.info("[LOG] [TB_LIBRARY_TYPE] [INITIALIZE] [SIZE] [{}]", libraryTypeRepository.count());
        countryRepository.deleteAllInBatch();
        log.info("[LOG] [TB_COUNTRY] [INITIALIZE] [SIZE] [{}]", countryRepository.count());
        cityRepository.deleteAllInBatch();
        log.info("[LOG] [TB_CITY] [INITIALIZE] [SIZE] [{}]", cityRepository.count());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        log.info("[LOG] [TB_CITY] [FINALLY END] [TOTAL_SIZE] [{}]", cityRepository.count());
        log.info("[LOG] [TB_LIBRARY_TYPE] [FINALLY END] [TOTAL_SIZE] [{}]", libraryTypeRepository.count());
        log.info("[LOG] [TB_COUNTRY] [FINALLY END] [TOTAL_SIZE] [{}]", countryRepository.count());
        log.info("[LOG] [TB_LIBRARY] [FINALLY END] [TOTAL_SIZE] [{}]", libraryRepository.count());

    }
}
