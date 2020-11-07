package kr.seok.library.step;

import kr.seok.library.domain.ReportDto;
import kr.seok.library.writer.ExcelItemWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

import static kr.seok.library.common.Constants.CHUNK_SIZE;

/**
 * Multi DB to Report Step 클래스
 */
@Configuration
public class MultiDBToReportStep {

    /* Batch Attribute */
    private static final String STEP_NAME = "MULTI_DB_TO_REPORT";
    private final StepBuilderFactory stepBuilderFactory;
    /* Database */
    private final DataSource dataSource;

    public MultiDBToReportStep(StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    /* Step으로 분리되어 Bean으로 등록 */
    @Bean(name = STEP_NAME + "_STEP")
    public Step multiDbToReportStep() {
        return stepBuilderFactory.get(STEP_NAME + "_STEP")
                .<ReportDto, ReportDto>chunk(CHUNK_SIZE)
                /* Multi DB Reader */
                .reader(multiDbReader())
                /* Report Writer */
                .writer(new ExcelItemWriter<ReportDto>())
                .build();
    }

    /* Multi DB의 데이터의 필요한 데이터를 조회 */
    private ItemReader<? extends ReportDto> multiDbReader() {
        return new JdbcCursorItemReaderBuilder<ReportDto>()
                .name(STEP_NAME + "_STEP_READER")
                .dataSource(dataSource)
                .sql("SELECT " +
                        "    A.CITY_NM as cityNm " +
                        "    , B.COUNTRY_NM countryNm " +
                        "    , C.LIBRARY_NM libraryNm " +
                        "    , C.LIBRARY_TYPE libraryType " +
                        "FROM TB_CITY A " +
                        "JOIN TB_COUNTRY B " +
                        "ON A.CITY_ID = B.CITY_ID " +
                        "JOIN TB_LIBRARY C " +
                        "ON B.COUNTRY_ID = C.COUNTRY_ID " +
                        "AND B.CITY_ID = C.CITY_ID " +
                        "ORDER BY CITY_NM, COUNTRY_NM, LIBRARY_NM, LIBRARY_TYPE")
                .rowMapper(new BeanPropertyRowMapper<>(ReportDto.class))
                .build();
    }
}
