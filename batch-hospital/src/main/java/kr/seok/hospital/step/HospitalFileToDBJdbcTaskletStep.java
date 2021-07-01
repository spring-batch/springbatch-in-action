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
import org.springframework.boot.builder.SpringApplicationBuilder;
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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HospitalFileToDBJdbcTaskletStep {

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

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

                    jdbcItemWriter(hospitals);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private void jdbcItemWriter(List<Hospital> hospitals) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("insert ");
        stringBuilder.append("TB_HOSPITAL ");
        stringBuilder.append("(ORG_ID, ADDRESS, ED_OPER_YN, ED_PHONE, ETC, FST_AID_MEDIC_INS_CD, FST_AID_MEDIC_INS_NM, HOS_CATE, HOS_CATE_NM, LATITUDE, LONGITUDE, OPER_DESC_DT, OPER_HOUR_FRI_C, OPER_HOUR_FRI_S, OPER_HOUR_HOL_C, OPER_HOUR_HOL_S, OPER_HOUR_MON_C, OPER_HOUR_MON_S, OPER_HOUR_SAT_C, OPER_HOUR_SAT_S, OPER_HOUR_SUN_C, OPER_HOUR_SUN_S, OPER_HOUR_THU_C, OPER_HOUR_THU_S, OPER_HOUR_TUE_C, OPER_HOUR_TUE_S, OPER_HOUR_WED_C, OPER_HOUR_WED_S, OPER_NM, PHONE1, SIMPLE_MAP, ZIP_CODE1, ZIP_CODE2, DATE) ");
        stringBuilder.append("values ");
        stringBuilder.append("(:id, :addr, :edOperYn, :edPhone, :etc, :fstAidMedicInsCd, :fstAidMedicInsNm, :hosCate, :hosCateNm, :lat, :lon, :operDescDt, :operHourFriC, :operHourFriS, :operHourHolC, :operHourHolS, :operHourMonC, :operHourMonS, :operHourSatC, :operHourSatS, :operHourSunC, :operHourSunS, :operHourThuC, :operHourThuS, :operHourThuC, :operHourThuS, :operHourWedC, :operHourWedS, :operNm, :phone1, :simpleMap, :zipCode1, :zipCode2, :date)");

        JdbcBatchItemWriter<Hospital> writerBuilder = new JdbcBatchItemWriterBuilder<Hospital>()
                .dataSource(dataSource)
                .beanMapped()
                .sql(stringBuilder.toString())
                .build();

        writerBuilder.afterPropertiesSet();
        writerBuilder.write(hospitals);
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
//                if(i == 1) break;
                i++;
            }

            log.info("전체 라인 수(헤더라인 제외) : {} ", (i - 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }

}
