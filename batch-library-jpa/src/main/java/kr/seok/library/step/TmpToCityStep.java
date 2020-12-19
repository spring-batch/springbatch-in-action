package kr.seok.library.step;

import kr.seok.library.domain.entity.CityEntity;
import kr.seok.library.domain.entity.LibraryTmpEntity;
import kr.seok.library.listener.CityEntityStepListener;
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
public class TmpToCityStep {

    private final String STEP_NAME = "JPA_VERSION_TMP_TO_CITY";
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static Set<CityEntity> cityKeySet = Sets.newHashSet();

    @Bean(name = STEP_NAME + "_STEP")
    public Step jpaTmpToCityStep() {
        return stepBuilderFactory.get(STEP_NAME + "_STEP")
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {

                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        log.info("CITY : {}", cityKeySet.size());
                        return ExitStatus.COMPLETED;
                    }
                })
                .<LibraryTmpEntity, CityEntity>chunk(CHUNK_SIZE)
                .reader(jpaTmpReader())
                .processor(jpaTmpToCityProcessor())
                .writer(jpaCityWriter())
                .build();
    }

    /* JPA ItemReader */
    private ItemReader<? extends LibraryTmpEntity> jpaTmpReader() {
        return new JpaPagingItemReaderBuilder<LibraryTmpEntity>()
                .pageSize(CHUNK_SIZE)
                .entityManagerFactory(entityManagerFactory)
                .name(STEP_NAME + "_READER")
                /* 데이터 조회 시 from 에 클래스명을 작성해야 한다. */
                .queryString("SELECT t FROM libraryTmpEntity t")
                .build();
    }

    private ItemProcessor<? super LibraryTmpEntity, ? extends CityEntity> jpaTmpToCityProcessor() {
        return (ItemProcessor<LibraryTmpEntity, CityEntity>) item -> {
            CityEntity cityEntity = CityEntity.builder().cityNm(item.getCityNm()).build();
            /* Set에 키 값이 포함되어 있으면 넘어가기*/
            if(cityKeySet.contains(cityEntity)) return null;
            /* 값이 포함되지 않은 경우 set에 설정 및 Entity에 저장 */
            cityKeySet.add(cityEntity);
            return cityEntity;
        };
    }
    /* JPA ItemWriter */
    private ItemWriter<? super CityEntity> jpaCityWriter() {
        return new JpaItemWriterBuilder<CityEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
