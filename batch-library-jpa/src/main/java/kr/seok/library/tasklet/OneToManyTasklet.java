package kr.seok.library.tasklet;

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

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OneToManyTasklet implements Tasklet {
    private final EntityManager entityManager;
    private final LibraryTmpRepository libraryTmpRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LibraryTypeRepository libraryTypeRepository;
    private final LibraryRepository libraryRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        Set<CityEntity> cityEntitySet = Sets.newHashSet();
        Set<CountryEntity> countryEntitySet = Sets.newHashSet();
        Set<LibraryTypeEntity> libraryTypeEntitySet = Sets.newHashSet();
        Set<LibraryEntity> libraryEntitySet = Sets.newHashSet();

        List<LibraryTmpEntity> totalEntity = libraryTmpRepository.findAll();
        log.info("LIBRARY_TMP 데이터 확인 : {}", totalEntity.size());

        totalEntity.forEach(item -> {
            /* Tmp Entity에서 각 테이블의 유니크한 필드를 가져와 Object로 생성하여 Set 데이터 타입에 저장 */
            String cityNm = item.getCityNm();
            String countryNm = item.getCountryNm();
            String libraryNm = item.getLibraryNm();
            String libraryType = item.getLibraryType();

            /* 유일한 City 값은 총 17건 */
            CityEntity cityEntity = CityEntity.builder().cityNm(cityNm).build();
            /* 유일한 Country 값은 총 244건 */
            CountryEntity countryEntity = CountryEntity.builder().cityEntity(cityEntity).countryNm(countryNm).build();
            /* 유일한 LibraryType 값은 총 6건 */
            LibraryTypeEntity libraryTypeEntity = LibraryTypeEntity.builder().libraryType(libraryType).build();
            /* 유일한 Library 값은 총 3108 건*/
            LibraryEntity libraryEntity = LibraryEntity.builder()
                    .libraryNm(libraryNm)
                    .cityEntity(cityEntity)
                    .countryEntity(countryEntity)
                    .libraryTypeEntity(libraryTypeEntity)
                    .build();

            /* 유일한 값들을 저장 */
            cityEntitySet.add(cityEntity);
            countryEntitySet.add(countryEntity);
            libraryTypeEntitySet.add(libraryTypeEntity);
            libraryEntitySet.add(libraryEntity);
        });

        log.info("CITY 데이터 확인 : {}", cityEntitySet.size());
        log.info("COUNTRY 데이터 확인 : {}", countryEntitySet.size());
        log.info("LIBRARY_TYPE 데이터 확인 : {}", libraryTypeEntitySet.size());
        log.info("LIBRARY 데이터 확인 : {}", libraryEntitySet.size());

        List<CityEntity> cityEntities = cityRepository.saveAll(cityEntitySet);
        log.info("저장 후 CITY 데이터 확인 : {}", cityEntities.size());
        List<CountryEntity> countryEntities = countryRepository.saveAll(countryEntitySet);
        log.info("저장 후 COUNTRY 데이터 확인 : {}", countryEntities.size());
        List<LibraryTypeEntity> libraryTypeEntities = libraryTypeRepository.saveAll(libraryTypeEntitySet);
        log.info("저장 후 LIBRARY_TYPE 데이터 확인 : {}", libraryTypeEntities.size());
        List<LibraryEntity> libraryEntities = libraryRepository.saveAll(libraryEntitySet);
        log.info("저장 후 LIBRARY 데이터 확인 : {}", libraryEntities.size());

        entityManager.flush();
        return RepeatStatus.FINISHED;
    }
}
