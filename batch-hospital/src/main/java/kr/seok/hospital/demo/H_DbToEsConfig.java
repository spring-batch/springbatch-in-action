package kr.seok.hospital.demo;

import kr.seok.hospital.domain.dto.HospitalEsEntity;
import kr.seok.hospital.repository.HospitalInfJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * DB to ES Job 생성 클래스
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class H_DbToEsConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;

    public static final String CREATE_DOCUMENT_URL = "http://localhost:9200/hospital_index/_doc/";
    private static final String JOB_NAME = "H_DbToEs";

    private final HospitalInfJpaRepository hospitalInfJpaRepository;

    @Bean(name = JOB_NAME + "_JOB")
    public Job hDbToEs() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .incrementer(new RunIdIncrementer())
                .start(hDbToEsStep())
                .build();
    }

    private Step hDbToEsStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .tasklet((contribution, chunkContext) -> {

                    List<HospitalEsEntity> esEntities = hospitalInfJpaRepository.findLeftJoinAll();

                    System.out.println(esEntities.size() > 0 ? "OK" : "Not Found Data");
                    // 아래 CloseableHttpClient를 사용하는 로직은 사용하기에 부적합함. (시간이 너무 오래 걸림) 2m48s422ms
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    for(HospitalEsEntity item : esEntities) {
//                        CloseableHttpClient client = HttpClients.createDefault(); /* HttpClient Request */
//                        HttpPut httpPut = new HttpPut(CREATE_DOCUMENT_URL + item.getOrgId());
//
//                        String valueAsString = objectMapper.writeValueAsString(item);
//                        StringEntity entity = new StringEntity( /* convert map to json 된 데이터를 utf8로 인코딩하는 작업 필요 */
//                                valueAsString,
//                                Charset.forName(StandardCharsets.UTF_8.name())
//                        );
//                        httpPut.setEntity(entity);
//                        httpPut.setHeader("content-type", APPLICATION_JSON.toString());
//
//                        CloseableHttpResponse response = client.execute(httpPut);
//                        if(response.getStatusLine().getStatusCode() == 400) {
//                            throw new IllegalArgumentException("에러");
//                        }
//                        client.close();
//                    }

                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
