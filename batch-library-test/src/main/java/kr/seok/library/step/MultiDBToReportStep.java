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

@Configuration
public class MultiDBToReportStep {

    private static final String STEP_NAME = "MULTI_DB_TO_REPORT";
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    public MultiDBToReportStep(StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    @Bean(name = STEP_NAME + "_STEP")
    public Step multiDbToReportStep() {
        return stepBuilderFactory.get(STEP_NAME + "_STEP_NAME")
                .<ReportDto, ReportDto>chunk(CHUNK_SIZE)
                .reader(multiDbReader())
                .writer(new ExcelItemWriter<>())
                .build();
    }

    private ItemReader<? extends ReportDto> multiDbReader() {
        return new JdbcCursorItemReaderBuilder<ReportDto>()
                .name(STEP_NAME + "_STEP_READER")
                .dataSource(dataSource)
                .sql("SELECT " +
                        "    A.CITY_NM as cityNm " +
                        "    , B.COUNTRY_NM countryNm " +
                        "    , C.LIBRARY_NM libraryNm " +
                        "    , C.LIBRARY_TYPE libraryType " +
                        "FROM TB_CITY A\n" +
                        "JOIN TB_COUNTRY B\n" +
                        "ON A.CITY_ID = B.CITY_ID\n" +
                        "JOIN TB_LIBRARY C\n" +
                        "ON B.COUNTRY_ID = C.COUNTRY_ID\n" +
                        "AND B.CITY_ID = C.CITY_ID\n" +
                        "ORDER BY CITY_NM, COUNTRY_NM, LIBRARY_NM, LIBRARY_TYPE")
                .rowMapper(new BeanPropertyRowMapper<>(ReportDto.class))
                .build();
    }

}
