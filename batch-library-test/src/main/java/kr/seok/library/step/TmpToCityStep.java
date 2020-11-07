package kr.seok.library.step;

import kr.seok.library.domain.entity.CityEntity;
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

/**
 * 임시 테이블에서 유일한 도시명만을 City Table에 적재하기 위한 Step
 */
@Configuration
public class TmpToCityStep {
    /* Batch */
    private static final String STEP_NAME = "TMP_TO_CITY_STEP";
    private final StepBuilderFactory stepBuilderFactory;
    private static Set<String> cityKeySet = new HashSet<>();

    /* Jpa DB */
    private final CityRepository cityRepository;
    private final EntityManagerFactory entityManagerFactory;

    public TmpToCityStep(StepBuilderFactory stepBuilderFactory, CityRepository cityRepository, EntityManagerFactory entityManagerFactory) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.cityRepository = cityRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @Bean(name = STEP_NAME)
    public Step tmpToCityStep() {
        return stepBuilderFactory.get(STEP_NAME)
                .<TmpEntity, CityEntity>chunk(CHUNK_SIZE)
                .reader(tmpReader())
                .processor(tmpToCityProcessor())
                .writer(cityWriter())
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

    /* 임시 테이블에 저장된 데이터를 CityEntity 의 Persistence Context 에 저장 */
    private ItemProcessor<? super TmpEntity, CityEntity> tmpToCityProcessor() {
        return (ItemProcessor<TmpEntity, CityEntity>) item -> {
            /* Set에 키 값이 포함되어 있으면 넘어가기*/
            if(cityKeySet.contains(item.getCityNm())) return null;
            /* 값이 포함되지 않은 경우 set에 설정 및 Entity에 저장 */
            cityKeySet.add(item.getCityNm());
            return CityEntity.builder().cityNm(item.getCityNm()).build();
        };
    }

    /* CityEntity persistence context에 저장된 Entity를 flush 처리 */
    private ItemWriter<CityEntity> cityWriter() {
        return new JpaItemWriter<CityEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
