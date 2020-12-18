package kr.seok.library.step;

import kr.seok.library.domain.entity.*;
import kr.seok.library.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Sets;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TaskletStep implements Tasklet {
    private final TmpRepository tmpRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LibraryTypeRepository libraryTypeRepository;
    private final LibraryRepository libraryRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Set<CityEntity> cityEntitySet = Sets.newHashSet();
        Set<CountryEntity> countryEntitySet = Sets.newHashSet();
        Set<LibraryTypeEntity> libraryTypeEntitySet = Sets.newHashSet();
        Set<LibraryEntity> libraryEntitySet = Sets.newHashSet();

        List<TmpEntity> totalEntity = tmpRepository.findAll();

        totalEntity.forEach(item -> {
            String cityNm = item.getCityNm();
            String countryNm = item.getCountryNm();
            String libraryNm = item.getLibraryNm();
            String libraryType = item.getLibraryType();

            CityEntity cityEntity = CityEntity.builder().cityNm(cityNm).build();
            CountryEntity countryEntity = CountryEntity.builder().cityNm(cityNm).countryNm(countryNm).build();
            LibraryTypeEntity libraryTypeEntity = LibraryTypeEntity.builder().libraryType(libraryType).build();
            LibraryEntity libraryEntity = LibraryEntity.builder()
                    .libraryNm(libraryNm)
                    .cityEntity(cityEntity)
                    .countryEntity(countryEntity)
                    .libraryTypeEntity(libraryTypeEntity)
                    .build();

            cityEntitySet.add(cityEntity);
            countryEntitySet.add(countryEntity);
            libraryTypeEntitySet.add(libraryTypeEntity);
            libraryEntitySet.add(libraryEntity);
        });
        log.info("CITY 데이터 확인 : {}", cityEntitySet.size());
        log.info("COUNTRY 데이터 확인 : {}", countryEntitySet.size());
        log.info("LIBRARY_TYPE 데이터 확인 : {}", libraryTypeEntitySet.size());
        log.info("LIBRARY 데이터 확인 : {}", libraryEntitySet.size());

        return RepeatStatus.FINISHED;
    }
}
