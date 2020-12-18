package kr.seok.area.domain;

import kr.seok.area.domain.entity.AreaEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileLineDto {
    private String line;

    public AreaEntity toAreaEntity() {
        String[] splitLine = line.split("\\s");
        if(splitLine.length == 3) {
            return AreaEntity.builder()
                    .areaCd(splitLine[0])
                    .bigLocalNm(splitLine[1])
                    .useYn("존재".equals(splitLine[2]))
                    .build();
        } else if(splitLine.length == 4) {
            return AreaEntity.builder()
                    .areaCd(splitLine[0])
                    .bigLocalNm(splitLine[1])
                    .midLocalNm(splitLine[2])
                    .useYn("존재".equals(splitLine[3]))
                    .build();
        } else if(splitLine.length == 5) {
            return AreaEntity.builder()
                    .areaCd(splitLine[0])
                    .bigLocalNm(splitLine[1])
                    .midLocalNm(splitLine[2])
                    .smallLocalNm(splitLine[3])
                    .useYn("존재".equals(splitLine[4]))
                    .build();
        }
        return AreaEntity.builder()
                .areaCd(splitLine[0])
                .bigLocalNm(splitLine[1])
                .midLocalNm(splitLine[2])
                .smallLocalNm(splitLine[3])
                .tooSmallLocalNm(splitLine[4])
                .useYn("존재".equals(splitLine[5]))
                .build();
    }
}
