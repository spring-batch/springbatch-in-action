package kr.seok.library.step;

import kr.seok.library.domain.entity.LibraryEntity;
import kr.seok.library.domain.entity.TmpEntity;
import kr.seok.library.repository.CityRepository;
import kr.seok.library.repository.CountryRepository;
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
public class TmpToLibraryStep {
    /* Batch */
    private static final String STEP_NAME = "TMP_TO_LIBRARY_STEP";
    private final StepBuilderFactory stepBuilderFactory;
    private static Set<String> libraryKeySet = new HashSet<>();
    /* DB */
    private final EntityManagerFactory entityManagerFactory;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    public TmpToLibraryStep(StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory, CityRepository cityRepository, CountryRepository countryRepository) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
    }

    @Bean(name = STEP_NAME)
    public Step tmpToLibraryStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<TmpEntity, LibraryEntity>chunk(CHUNK_SIZE)
                .reader(tmpReader())
                .processor(tmpToLibraryProcessor())
                .writer(libraryWriter())
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

    /* 임시테이블에 있는 데이터 중 각 지역의 도서관을 중복 제거하여 LibraryEntity의 persistence context 에 저장 */
    private ItemProcessor<? super TmpEntity, ? extends LibraryEntity> tmpToLibraryProcessor() {
        return (ItemProcessor<TmpEntity, LibraryEntity>) item -> {
            String libraryKey = item.getCityNm() + " " + item.getCountryNm() + " " + item.getLibraryNm();

            /* Set에 키 값이 포함되어 있으면 넘어가기*/
            if(libraryKeySet.contains(libraryKey)) return null;
            /* 값이 포함되지 않은 경우 set에 설정 및 Entity에 저장 */
            libraryKeySet.add(libraryKey);

            /* TODO: Jpa로 처리시 깔끔하게 처리하는 방법이 있을 듯 */
            Long cityId = cityRepository.findByCityNm(item.getCityNm()).get().getId();
            Long countryId = countryRepository.findByCityIdAndCountryNm(cityId, item.getCountryNm()).get().getId();
            String libraryNm = item.getLibraryNm();
            String libraryType = item.getLibraryType();

            return LibraryEntity.builder()
                    .cityId(cityId)
                    .countryId(countryId)
                    .libraryNm(libraryNm)
                    .libraryType(libraryType)
                    .build();
        };
    }

    /* LibraryEntity 의 persistence context에 저장된 데이터를 DB에 flush */
    private ItemWriter<LibraryEntity> libraryWriter() {
        return new JpaItemWriter<LibraryEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
