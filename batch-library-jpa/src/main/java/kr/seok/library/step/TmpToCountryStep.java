package kr.seok.library.step;

import kr.seok.library.domain.entity.CityEntity;
import kr.seok.library.domain.entity.CountryEntity;
import kr.seok.library.domain.entity.LibraryTmpEntity;
import kr.seok.library.domain.repository.CityRepository;
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
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.Set;

import static kr.seok.library.common.Constants.CHUNK_SIZE;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TmpToCountryStep {

    private final String STEP_NAME = "JPA_VERSION_STEP_TWO_TMP_TO_COUNTRY";
    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;
    private static Set<CountryEntity> countryKeySet = Sets.newHashSet();

    private final CityRepository cityRepository;

    @Bean(name = STEP_NAME + "_STEP")
    public Step jpaTmpToCountryStep() {
        return stepBuilderFactory.get(STEP_NAME + "_STEP")
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {

                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        log.info("COUNTRY : {}", countryKeySet.size());
                        return ExitStatus.COMPLETED;
                    }
                })
                .<LibraryTmpEntity, CountryEntity>chunk(CHUNK_SIZE)
                .reader(jpaTmpReader())
                .processor(jpaTmpToCountryProcessor())
                .writer(jpaCountryWriter())
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

    private ItemProcessor<? super LibraryTmpEntity, ? extends CountryEntity> jpaTmpToCountryProcessor() {
        return item -> {
            CityEntity cityEntity = cityRepository.findByCityNm(item.getCityNm());
            CountryEntity countryEntity = CountryEntity.builder()
                    .countryNm(item.getCountryNm())
                    .cityEntity(cityEntity)
                    .build();
            if (countryKeySet.contains(countryEntity)) return null;
            countryKeySet.add(countryEntity);
            return countryEntity;
        };
    }

    private ItemWriter<? super CountryEntity> jpaCountryWriter() {
        return new JpaItemWriterBuilder<CountryEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
