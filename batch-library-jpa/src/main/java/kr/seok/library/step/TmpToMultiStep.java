package kr.seok.library.step;

import kr.seok.library.domain.entity.CityEntity;
import kr.seok.library.domain.entity.CountryEntity;
import kr.seok.library.domain.entity.LibraryEntity;
import kr.seok.library.domain.entity.TmpEntity;
import kr.seok.library.domain.repository.TmpRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Sets;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Set;

/**
 * 임시 테이블 데이터를 정규화된 테이블에 넣는 작업
 */
@Configuration
@RequiredArgsConstructor
public class TmpToMultiStep {

    /* Batch */
    private static final String STEP_NAME = "JPA_VERSION_STEP_TWO";
    private final StepBuilderFactory stepBuilderFactory;
    /* JPA */
    private final EntityManagerFactory entityManagerFactory;

    private final JobBuilderFactory jobBuilderFactory;
    private final TmpRepository tmpRepository;

    @Bean
    public Job jpaVersionJobTwo() {
        return jobBuilderFactory.get(STEP_NAME + "_JOB")
                .start(jobVersionStepTwo())
                .build();
    }

    @Bean
    public Step jobVersionStepTwo() {
        return stepBuilderFactory.get(STEP_NAME)
                .tasklet((contribution, chunkContext) -> {
                    List<TmpEntity> totalEntity = tmpRepository.findAll();
                    Set<CityEntity> cityEntitySet = Sets.newHashSet();
                    Set<CountryEntity> countryEntitySet = Sets.newHashSet();
                    Set<LibraryEntity> libraryEntityHashSet = Sets.newHashSet();
                    return null;
                })
                .build();
    }
}
