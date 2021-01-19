package kr.seok.hospital.domain.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of = {"code", "title"})
public class EnumMapperValue {

    private String code;
    private String title;

    public EnumMapperValue(EnumMapperType enumMapperType) {
        this.code = enumMapperType.getCode();
        this.title = enumMapperType.getTitle();
    }
}
