package kr.seok.library.step;

import kr.seok.library.domain.entity.LibraryTmpEntity;
import kr.seok.library.domain.entity.LibraryTypeEntity;
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
public class TmpToLibraryTypeStep {

    private final String STEP_NAME = "JPA_VERSION_STEP_TWO_TMP_TO_LIBRARY_TYPE";

    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    private static Set<LibraryTypeEntity> libraryTypeKeySet = Sets.newHashSet();

    @Bean(name = STEP_NAME + "_STEP")
    public Step jpaTmpToLibraryTypeStep() {
        return stepBuilderFactory.get(STEP_NAME + "_STEP")
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {

                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        log.info("LIBRARY_TYPE : {}", libraryTypeKeySet.size());
                        return ExitStatus.COMPLETED;
                    }
                })
                .<LibraryTmpEntity, LibraryTypeEntity>chunk(CHUNK_SIZE)
                .reader(jpaTmpReader())
                .processor(jpaTmpToLibraryTypeProcessor())
                .writer(jpaLibraryTypeWriter())
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

    private ItemProcessor<? super LibraryTmpEntity, ? extends LibraryTypeEntity> jpaTmpToLibraryTypeProcessor() {
        return item -> {
            LibraryTypeEntity libraryTypeEntity = LibraryTypeEntity.builder()
                    .libraryType(item.getLibraryType())
                    .build();
            if (libraryTypeKeySet.contains(libraryTypeEntity)) return null;
            libraryTypeKeySet.add(libraryTypeEntity);
            return libraryTypeEntity;
        };
    }

    private ItemWriter<? super LibraryTypeEntity> jpaLibraryTypeWriter() {
        return new JpaItemWriterBuilder<LibraryTypeEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
