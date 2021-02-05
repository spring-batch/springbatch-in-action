package kr.seok.estate.domain.dto;

import kr.seok.estate.domain.entity.AreaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileLineDto {
    private String line;

    /**
     * AREA_CD      ADDRESS1    ADRESS2     ADDRESS3  ADDRESS4    EXIST_YN
     * 1100000000	서울특별시	                                        존재
     * 1111000000	서울특별시   종로구	                               존재
     * 1111010100	서울특별시   종로구         청운동	               존재
     * 2671032030	부산광역시   기장군         정관면     예림리         폐지
     */
    public AreaEntity toAreaEntity() {
        String[] splitLine = line.split("\\s");
        /* 각 라인을 space로 split하여 토큰 값을 Entity의 필드에 저장 */
        if(splitLine.length == 3) {
            return AreaEntity.builder()
                    .id(Long.parseLong(splitLine[0]))
                    .depth1Nm(splitLine[1])
                    .useYn("존재".equals(splitLine[2]))
                    .build();
        } else if(splitLine.length == 4) {
            return AreaEntity.builder()
                    .id(Long.parseLong(splitLine[0]))
                    .depth1Nm(splitLine[1])
                    .depth2Nm(splitLine[2])
                    .useYn("존재".equals(splitLine[3]))
                    .build();
        } else if(splitLine.length == 5) {
            return AreaEntity.builder()
                    .id(Long.parseLong(splitLine[0]))
                    .depth1Nm(splitLine[1])
                    .depth2Nm(splitLine[2])
                    .depth3Nm(splitLine[3])
                    .useYn("존재".equals(splitLine[4]))
                    .build();
        }
        return AreaEntity.builder()
                .id(Long.parseLong(splitLine[0]))
                .depth1Nm(splitLine[1])
                .depth2Nm(splitLine[2])
                .depth3Nm(splitLine[3])
                .depth4Nm(splitLine[4])
                .useYn("존재".equals(splitLine[5]))
                .build();
    }
}
