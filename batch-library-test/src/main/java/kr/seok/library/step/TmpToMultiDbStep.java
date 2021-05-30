package kr.seok.library.step;

import kr.seok.library.domain.entity.CityEntity;
import kr.seok.library.domain.entity.CountryEntity;
import kr.seok.library.domain.entity.LibraryEntity;
import kr.seok.library.domain.entity.TmpEntity;
import kr.seok.library.repository.CityRepository;
import kr.seok.library.repository.CountryRepository;
import kr.seok.library.repository.LibraryRepository;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kr.seok.library.common.Constants.CHUNK_SIZE;

/**
 * 임시 테이블에서 City, Country, Library 테이블에 한 번에 입력할 수 있도록 한 Step
 */
@Configuration
public class TmpToMultiDbStep {

    private static final String STEP_NAME = "TMP_TO_MULTI_DB";
    private final StepBuilderFactory stepBuilderFactory;

    /* Jdbc */
    private final DataSource dataSource;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LibraryRepository libraryRepository;

    /* Data Key Set */
    private static Set<String> cityKeySet = new HashSet<>();
    private static Set<String> countryKeySet = new HashSet<>();
    private static Set<String> libraryKeySet = new HashSet<>();

    public TmpToMultiDbStep(StepBuilderFactory stepBuilderFactory, DataSource dataSource, CityRepository cityRepository, CountryRepository countryRepository, LibraryRepository libraryRepository) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.libraryRepository = libraryRepository;
    }

    @Bean(name = STEP_NAME + "_STEP")
    public Step tmpToMultiDbStep() {
        return stepBuilderFactory.get(STEP_NAME + "_STEP")
                .<TmpEntity, TmpEntity>chunk(CHUNK_SIZE)
                .reader(tmpOneReader())
                .writer(multiEntityWriter())
                .build();
    }

    private ItemReader<? extends TmpEntity> tmpOneReader() {
        /* TB_TMP_LIBRARY 테이블의 컬럼리스트를 작성 */
        StringBuilder sb = new StringBuilder();
        for (String fields : TmpEntity.TmpFields.getFields())
            sb.append(fields).append(", ");

        return new JdbcCursorItemReader<TmpEntity>() {{
            /* 실행 Reader 명 설정 */
            setName(STEP_NAME + "_STEP_READER");
            /* Jdbc 방식으로 DB 접근 */
            setDataSource(dataSource);
            /* 조회 쿼리 */
            setSql("SELECT " + sb.substring(0, sb.toString().length() - 2) + " FROM TB_TMP_LIBRARY");
            /* 조회된 row 데이터 Bean으로 매핑 */
            setRowMapper(new BeanPropertyRowMapper<>(TmpEntity.class));
        }};
    }

    private ItemWriter<? super TmpEntity> multiEntityWriter() {
        /* 데이터 처리할 Processor를 리스트에 등록 */
        List<ItemWriter<? super TmpEntity>> delegates = new ArrayList<>();
        delegates.add(cityWriter());
        delegates.add(countryWriter());
        delegates.add(libraryWriter());

        /* Writer 위임 */
        CompositeItemWriter compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(delegates);

        return compositeItemWriter;
    }

    /* Jpa Writer: City */
    private ItemWriter<? super TmpEntity> cityWriter() {
        return items -> {
            List<CityEntity> cityList = new ArrayList<>();

            for (TmpEntity item : items) {
                String cityKey = item.getCityNm();

                if (cityKeySet.contains(cityKey)) continue;
                cityKeySet.add(cityKey);

                cityList.add(
                        CityEntity.builder()
                                .cityNm(cityKey)
                                .build());
            }

            cityRepository.saveAll(cityList);
            cityRepository.flush();
        };
    }

    /* Jpa Writer: Country */
    private ItemWriter<? super TmpEntity> countryWriter() {
        return items -> {
            List<CountryEntity> countryList = new ArrayList<>();

            for (TmpEntity item : items) {
                String countryKey = item.getCityNm() + " " + item.getCountryNm();
                String cityNm = item.getCityNm();
                String countryNm = item.getCountryNm();

                if (countryKeySet.contains(countryKey)) continue;
                countryKeySet.add(countryKey);

                Long cityId = cityRepository.findByCityNm(cityNm).get().getId();
                countryList.add(
                        CountryEntity.builder()
                                .cityId(cityId)
                                .countryNm(countryNm)
                                .build());
            }

            countryRepository.saveAll(countryList);
            countryRepository.flush();
        };
    }

    /* Jpa Writer: Library */
    private ItemWriter<? super TmpEntity> libraryWriter() {
        return items -> {
            List<LibraryEntity> libraryList = new ArrayList<>();
            for (TmpEntity item : items) {
                String libraryKey = item.getCityNm() + " " + item.getCountryNm() + " " + item.getLibraryNm();

                /* Unique 데이터 Filtering */
                if (libraryKeySet.contains(libraryKey)) continue;
                libraryKeySet.add(libraryKey);

                /* Entity 만들기 */
                Long cityId = cityRepository.findByCityNm(item.getCityNm()).get().getId();
                Long countryId = countryRepository.findByCityIdAndCountryNm(cityId, item.getCountryNm()).get().getId();
                String libraryNm = item.getLibraryNm();
                String libraryType = item.getLibraryType();

                libraryList.add(
                        LibraryEntity.builder()
                                .cityId(cityId)
                                .countryId(countryId)
                                .libraryNm(libraryNm)
                                .libraryType(libraryType)
                                .build());
            }
            libraryRepository.saveAll(libraryList);
            libraryRepository.flush();
        };
    }
}
