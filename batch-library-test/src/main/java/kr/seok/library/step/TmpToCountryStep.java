package kr.seok.library.step;

import kr.seok.library.domain.entity.CountryEntity;
import kr.seok.library.domain.entity.TmpEntity;
import kr.seok.library.repository.CityRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashSet;
import java.util.Set;

import static kr.seok.library.common.Constants.CHUNK_SIZE;

@Configuration
public class TmpToCountryStep {
    /* Batch */
    private static final String STEP_NAME = "TMP_TO_COUNTRY_STEP";
    private final StepBuilderFactory stepBuilderFactory;
    private static Set<String> countryKeySet = new HashSet<>();
    /* DB */
    private final EntityManagerFactory entityManagerFactory;
    private final CityRepository cityRepository;

    public TmpToCountryStep(StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory, CityRepository cityRepository) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.cityRepository = cityRepository;
    }

    @Bean(name = STEP_NAME)
    public Step tmpToCountryStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<TmpEntity, CountryEntity>chunk(CHUNK_SIZE)
                .reader(tmpReader())
                .processor(tmpToCountryProcessor())
                .writer(countryWriter())
                .build();
    }

    /* One Reader: JdbcCursorItemReader Type */
    private ItemReader<? extends TmpEntity> tmpReader() {
        return new JpaPagingItemReaderBuilder<TmpEntity>()
                .pageSize(CHUNK_SIZE)
                /* Jpa EntityManagerFactory */
                .entityManagerFactory(entityManagerFactory)
                /* JpaPagingItemReader 실행 시 이름 */
                .name(STEP_NAME + "_READER")
                /* QueryProvider or QueryString */
                .queryString("SELECT t FROM TMP_ENTITY t")
                .build();
    }

    private ItemProcessor<? super TmpEntity, ? extends CountryEntity> tmpToCountryProcessor() {
        return (ItemProcessor<TmpEntity, CountryEntity>) item -> {
            String countryKey = item.getCityNm() + " " + item.getCountryNm();

            /* Set에 키 값이 포함되어 있으면 넘어가기*/
            if(countryKeySet.contains(countryKey)) return null;
            /* 값이 포함되지 않은 경우 set에 설정 및 Entity에 저장 */
            countryKeySet.add(countryKey);

            /* TODO: Jpa로 처리시 깔끔하게 처리하는 방법이 있을 듯 */
            Long cityId = cityRepository.findByCityNm(item.getCityNm()).get().getId();
            return CountryEntity.builder()
                    .cityId(cityId)
                    .countryNm(item.getCountryNm())
                    .build();
        };
    }

    private ItemWriter<CountryEntity> countryWriter() {
        return new JpaItemWriter<CountryEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
