package kr.seok.library.step;

import kr.seok.library.domain.entity.*;
import kr.seok.library.domain.repository.CityRepository;
import kr.seok.library.domain.repository.CountryRepository;
import kr.seok.library.domain.repository.LibraryTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Sets;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import java.util.Set;

import static kr.seok.library.common.Constants.CHUNK_SIZE;

@Slf4j
@Component
@RequiredArgsConstructor
public class TmpToLibraryStep {

    private final String STEP_NAME = "JPA_VERSION_STEP_TWO_TMP_TO_LIBRARY";

    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private static Set<LibraryEntity> libraryKeySet = Sets.newHashSet();

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LibraryTypeRepository libraryTypeRepository;

    @Bean(name = STEP_NAME + "_STEP")
    public Step jpaTmpToLibraryStep() {
        return stepBuilderFactory.get(STEP_NAME + "_STEP")
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {

                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        log.info("LIBRARY SIZE : {}", libraryKeySet.size());
                        return ExitStatus.COMPLETED;
                    }
                })
                .<LibraryTmpEntity, LibraryEntity>chunk(CHUNK_SIZE)
                .reader(jpaTmpReader())
                .processor(jpaTmpToLibraryProcessor())
                .writer(jpaLibraryWriter())
                .build();
    }

    private ItemReader<? extends LibraryTmpEntity> jpaTmpReader() {
        return new JpaPagingItemReaderBuilder<LibraryTmpEntity>()
                .pageSize(CHUNK_SIZE)
                .entityManagerFactory(entityManagerFactory)
                .name(STEP_NAME + "_READER")
                /* 데이터 조회 시 from 에 클래스명을 작성해야 한다. */
                .queryString("SELECT t FROM libraryTmpEntity t")
                .build();
    }

    private ItemProcessor<? super LibraryTmpEntity, ? extends LibraryEntity> jpaTmpToLibraryProcessor() {
        return item -> {

            String cityNm = item.getCityNm();
            String countryNm = item.getCountryNm();
            String libraryType = item.getLibraryType();
            String libraryNm = item.getLibraryNm();

            CityEntity cityEntity = cityRepository.findByCityNm(cityNm);
            CountryEntity countryEntity = countryRepository.findByCityEntityAndCountryNm(cityEntity, countryNm);
            LibraryTypeEntity libraryTypeEntity = libraryTypeRepository.findByLibraryType(libraryType);

            return LibraryEntity.builder()
                    .libraryNm(libraryNm)
                    .cityEntity(cityEntity)
                    .countryEntity(countryEntity)
                    .libraryTypeEntity(libraryTypeEntity)
                    .build();
        };
    }

    private ItemWriter<? super LibraryEntity> jpaLibraryWriter() {
        return new JpaItemWriterBuilder<LibraryEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
