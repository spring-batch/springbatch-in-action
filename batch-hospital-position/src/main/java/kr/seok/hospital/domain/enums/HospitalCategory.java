package kr.seok.hospital.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum HospitalCategory implements EnumMapperType {

    HOSPITAL_A("A", "종합병원"),
    HOSPITAL_B("B", "병원"),
    HOSPITAL_C("C", "의원"),
    HOSPITAL_D("D", "요양병원"),
    HOSPITAL_E("E", "한방병원"),
    HOSPITAL_G("G", "한의원"),
    HOSPITAL_I("I", "기타"),
    HOSPITAL_M("M", "치과병원"),
    HOSPITAL_N("N", "치과의원"),
    HOSPITAL_R("R", "보건소"),
    HOSPITAL_W("W", "기타(구급차)");

    private final String hosCateCd;
    private final String hosCateNm;

    HospitalCategory(String hosCateCd, String hosCateNm) {
        this.hosCateCd = hosCateCd;
        this.hosCateNm = hosCateNm;
    }

    @Override
    public String getCode() {
        return hosCateCd;
    }

    @Override
    public String getTitle() {
        return hosCateNm;
    }

    /* 코드명으로 Enum 반롼 */
    public static HospitalCategory findByHospitalCate(String code) {
        return Arrays.stream(HospitalCategory.values())
                .filter(c -> c.hasHosCd(code))
                .findAny()
                .orElse(HOSPITAL_I);
    }

    /* 코드 값이 존재하는지 확인 */
    public boolean hasHosCd(String cd) {
        return Arrays.asList(getCodes())
                .contains(cd);
    }

    /* 코드명 값이 존재하는지 확인 */
    public boolean hasHosNm(String cd) {
        return Arrays.asList(getNames())
                .contains(cd);
    }

    /* enum HosCateCd 리스트를 String[]로 반롼 */
    public static String[] getCodes() {
        return Arrays.stream(HospitalCategory.values())
                .map(HospitalCategory::getHosCateCd)
                .toArray(String[]::new);
    }

    /* enum title 필드를 String[]로 반환 */
    public static String[] getNames() {
        return Arrays.stream(HospitalCategory.values())
                .map(HospitalCategory::getHosCateNm)
                .toArray(String[]::new);
    }

    /* enum 필드를 List<EnumMapperValue>로 변환 */
    public static List<EnumMapperValue> getEnumTypeList() {
        return Arrays.stream(HospitalCategory.values())
                .map(EnumMapperValue::new)
                .collect(Collectors.toList());
    }

    /* enum 필드의 Code 리스트를 조회 */
    public static List<String> getCodeList() {
        return Arrays.stream(HospitalCategory.values())
                .map(HospitalCategory::getCode)
                .collect(Collectors.toList());
    }

    /* enum 필드의 Title 리스트를 조회 */
    public static List<String> getTitles() {
        return Arrays.stream(HospitalCategory.values())
                .map(HospitalCategory::getTitle)
                .collect(Collectors.toList());
    }

    /* enum 필드를 List로 반환 (동적 배열로 반환할 필요가 없지) */
    @Deprecated
    public static List<String> getNameList() {
        return Arrays.stream(HospitalCategory.values())
                .map(HospitalCategory::getHosCateNm)
                .collect(Collectors.toList());
    }
}
