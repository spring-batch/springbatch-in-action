package kr.seok.hospital.domain.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HospitalFileDtoTest {

    @Test
    @DisplayName("reflect 필드 리스트 반환 테스트")
    void testCase2() {
        String[] fields = HospitalFileDto.getFields();
        assertThat(fields.length).isEqualTo(34);
        for (String item : fields) {
            System.out.println(item);
        }
    }

    @Test
    @DisplayName("날자 정보 파싱 입력 테스트")
    void testCase3() {
        String date = "2020-12-07 14:53:12.0";
        date = date.substring(0, date.length() - 2).replaceFirst("\\s", "T");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        System.out.println(localDateTime);
    }
}
