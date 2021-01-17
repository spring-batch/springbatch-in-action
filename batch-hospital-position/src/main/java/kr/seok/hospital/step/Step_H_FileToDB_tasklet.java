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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Step_H_FileToDB_tasklet {

    /* 38s7ms */
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;

    @Bean(name = "STEP_H_FileToDB")
    public Step hFileToDbStep() {
        return stepBuilderFactory.get("STEP_H_FileToDB")
                .tasklet((contribution, chunkContext) -> {

                    /* 데이터 */
                    List<HospitalFileDto> dto = readFile();
                    List<Hospital> hospitals = getDtoToEntity(dto);

                    /* jpaItemWriter 30s 초반 대 */
//                    jpaItemWriterProcess(hospitals);
                    /* jdbcItemWriter 20s 중반 대 */
                    jdbcItemWriter(hospitals);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private void jdbcItemWriter(List<Hospital> hospitals) throws Exception {
        JdbcBatchItemWriter<Hospital> writerBuilder = new JdbcBatchItemWriterBuilder<Hospital>()
                .dataSource(dataSource)
                .beanMapped()
                .sql("insert \n" +
                        "    into\n" +
                        "        TB_HOSPITAL\n" +
                        "        (ORG_ID, ADDRESS, ED_OPER_YN, ED_PHONE, ETC, FST_AID_MEDIC_INS_CD, FST_AID_MEDIC_INS_NM, HOS_CATE, HOS_CATE_NM, LATITUDE, LONGITUDE, OPER_DESC_DT, OPER_HOUR_FRI_C, OPER_HOUR_FRI_S, OPER_HOUR_HOL_C, OPER_HOUR_HOL_S, OPER_HOUR_MON_C, OPER_HOUR_MON_S, OPER_HOUR_SAT_C, OPER_HOUR_SAT_S, OPER_HOUR_SUN_C, OPER_HOUR_SUN_S, OPER_HOUR_THU_C, OPER_HOUR_THU_S, OPER_HOUR_TUE_C, OPER_HOUR_TUE_S, OPER_HOUR_WED_C, OPER_HOUR_WED_S, OPER_NM, PHONE1, SIMPLE_MAP, ZIP_CODE1, ZIP_CODE2, DATE) \n" +
                        "    values\n" +
                        "        (:id, :addr, :edOperYn, :edPhone, :etc, :fstAidMedicInsCd, :fstAidMedicInsNm, :hosCate, :hosCateNm, :lat, :lon, :operDescDt, :operHourFriC, :operHourFriS, :operHourHolC, :operHourHolS, :operHourMonC, :operHourMonS, :operHourSatC, :operHourSatS, :operHourSunC, :operHourSunS, :operHourThuC, :operHourThuS, :operHourThuC, :operHourThuS, :operHourWedC, :operHourWedS, :operNm, :phone1, :simpleMap, :zipCode1, :zipCode2, :date)")
                .build();

        writerBuilder.afterPropertiesSet();
        writerBuilder.write(hospitals);
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
    public List<HospitalFileDto> readFile() {

        List<HospitalFileDto> tmp = new ArrayList<>();
        /* resources 아래 경로로 읽기 */
        ClassPathResource classPathResource = new ClassPathResource("files/seoul_hospital_position_info_utf8.csv");
        try {
            File file = new File(classPathResource.getURI());

            if(!file.exists()) throw new FileNotFoundException("파일이 존재하지 않습니다.");
            if(!file.canRead()) throw new Exception("읽을 수 없는 파일 입니다.");

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), UTF_8));
            String line = "";
            int i = 0;

            while((line = br.readLine()) != null) {
                if(!(i == 0)) {
                    HospitalFileDto hospitalFileDto = getSplitData(line);
                    tmp.add(hospitalFileDto);
                }
//                if(i == 1) break;
                i++;
            }

            log.info("전체 라인 수 : {}", i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmp;
    }

    /* dto로 변경 */
    private HospitalFileDto getSplitData(String line) {
        String[] s = line.split(",");
        return HospitalFileDto.builder()
                .id(s[0])
                .addr(s[1])
                .hosCate(s[2])
                .hosCateNm(s[3])
                .fstAidMedicInsCd(s[4])
                .fstAidMedicInsNm(s[5])
                .edOperYn(s[6])
                .etc(s[7])
                .operDescDt(s[8])
                .simpleMap(s[9])

                .operNm(s[10])
                .phone1(s[11])
                .edPhone(s[12])
                .operHourMonC(s[13])
                .operHourTueC(s[14])
                .operHourWedC(s[15])
                .operHourThuC(s[16])
                .operHourFriC(s[17])
                .operHourSatC(s[18])
                .operHourSunC(s[19])

                .operHourHolC(s[20])
                .operHourMonS(s[21])
                .operHourTueS(s[22])
                .operHourWedS(s[23])
                .operHourThuS(s[24])
                .operHourFriS(s[25])
                .operHourSatS(s[26])
                .operHourSunS(s[27])
                .operHourHolS(s[28])
                .zipCode1(s[29])

                .zipCode2(s[30])
                .lat(Double.parseDouble(s[31].replaceAll("\"", "")))
                .lon(Double.parseDouble(s[32].replaceAll("\"", "")))
                .date(s[33])

                .build();
    }

}
