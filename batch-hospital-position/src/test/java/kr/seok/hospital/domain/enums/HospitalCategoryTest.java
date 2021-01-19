package kr.seok.hospital.domain.enums;

import kr.seok.hospital.domain.enums.factory.EnumMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HospitalCategoryTest {

    @Test
    @DisplayName("enum 행위 메서드 테스트를 구현하기 위한 로직 테스트")
    void testCase1() {
        HospitalCategory[] values = HospitalCategory.values();
        List<HospitalCategory> collect = Arrays.stream(values)
                .collect(Collectors.toList());
        collect.forEach(e -> System.out.println(e.getTitle()));
    }

    @Test
    @DisplayName("enum 타입의 병원타입명 리스트를 조회")
    void testCase2() {
        List<EnumMapperValue> enums = HospitalCategory.getEnumTypeList();
        enums.forEach(System.out::println);
    }

//    @Test
//    @DisplayName("enum 타입의 코드명 확인 -> 확인후 메서드에서 static 제거")
//    void testCase3() {
//        boolean a = HospitalCategory.hasHosNm("종합병원");
//        assertThat(a).isTrue();
//    }
//
//    @Test
//    @DisplayName("enum 타입의 코드 확인 -> 확인후 메서드에서 static 제거")
//    void testCase4() {
//        boolean a = HospitalCategory.hasHosCd("A");
//        assertThat(a).isTrue();
//    }


    @Test
    @DisplayName("코드 리스트 조회")
    void testCase3() {
        String[] codes = HospitalCategory.getCodes();
        Arrays.stream(codes).forEach(System.out::println);
        List<String> codeList = HospitalCategory.getCodeList();
        codeList.forEach(System.out::println);
    }
    @Test
    @DisplayName("코드명 리스트 조회")
    void testCase4() {
        String[] names = HospitalCategory.getNames();
        Arrays.stream(names).forEach(System.out::println);
        List<String> titles = HospitalCategory.getTitles();
        titles.forEach(System.out::println);
    }

    @Test
    @DisplayName("enum에 정의된 코드 값으로 코드명 반롼 테스트")
    void testCase6() {
        HospitalCategory a = HospitalCategory.findByHospitalCate("A");
        HospitalCategory b = HospitalCategory.findByHospitalCate("B");
        assertThat(a.getTitle()).contains("종합병원");
        assertThat(b.getTitle()).contains("병원");

        /* 정의된 코드 범주 외에 검색 시 기본값 조회 */
        HospitalCategory z = HospitalCategory.findByHospitalCate("Z");
        assertThat(z.getTitle()).contains("기타");
    }

    @Autowired
    EnumMapper enumMapper;

    @Test
    @DisplayName("enum 타입 요청시 인스턴스 반복 생성되는 문제를 개선하기 위한 Bean 등록 처리 및 조회 테스트")
    void testCase7() {
        List<EnumMapperValue> hospitalCate = enumMapper.get("HospitalCate");
        hospitalCate.forEach(System.out::println);
    }
}
