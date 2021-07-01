package kr.seok.hospital.step;

import kr.seok.hospital.domain.Hospital;
import kr.seok.hospital.domain.dto.HospitalFileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static kr.seok.hospital.domain.dto.HospitalFileDto.getSplitData;

/**
 * The type Hospital file to db jpa tasklet step.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class HospitalFileToDBJpaTaskletStep {

    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    /**
     * Hospital File to DB Tasklet 기반 Step
     *
     * @param filePath 외부에서 파일 경로 주입
     * @return the step
     */
    public Step hFileToDbStep(@Value("${file.path}") String filePath) {
        return stepBuilderFactory.get("STEP_H_FileToDB")
                .tasklet((contribution, chunkContext) -> {

                    /* 데이터 */
                    List<HospitalFileDto> dto = readFile(filePath);
                    List<Hospital> hospitals = getDtoToEntity(dto);

                    jpaItemWriterProcess(hospitals);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    /* JPA ItemWriter */
    private void jpaItemWriterProcess(List<Hospital> hospitals) throws Exception {
        JpaItemWriter<Hospital> jpaItemWriterBuilder =
                new JpaItemWriterBuilder<Hospital>()
                        .usePersist(true)
                        .entityManagerFactory(entityManagerFactory)
                        .build();

        jpaItemWriterBuilder.afterPropertiesSet();
        jpaItemWriterBuilder.write(hospitals);
    }

    private List<Hospital> getDtoToEntity(List<HospitalFileDto> dto) {
        return dto.stream()
                .map(HospitalFileDto::toEntity)
                .collect(Collectors.toList());
    }

    /* 파일을 읽어 dto로 변환하여 반환 */
    public List<HospitalFileDto> readFile(String filePath) {
        List<HospitalFileDto> tmp = new ArrayList<>();
        try {
            /* resources 아래 경로로 읽기 */
            File file = new File(new ClassPathResource(filePath).getURI());

            if (!file.exists()) throw new FileNotFoundException("파일이 존재하지 않습니다.");
            if (!file.canRead()) throw new Exception("읽을 수 없는 파일 입니다.");

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), UTF_8));
            String line = "";
            int i = 0;

            while ((line = br.readLine()) != null) {
                if (!(i == 0)) {
                    tmp.add(getSplitData(line));
                }
                i++;
            }

            log.info("전체 라인 수(헤더라인 제외) : {} ", (i - 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }

}
