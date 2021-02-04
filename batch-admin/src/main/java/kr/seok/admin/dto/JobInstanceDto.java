package kr.seok.admin.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class JobInstanceDto {

    private Long jobInstanceId;
    private Long version;
    private String jobName;
    private String jobKey;

    @Getter
    @NoArgsConstructor
    static class JobInstanceResponse {
        private String jobName;

        @Builder
        public JobInstanceResponse(String jobName) {
            this.jobName = jobName;
        }
    }

    @Builder
    @QueryProjection
    public JobInstanceDto(final Long jobInstanceId, Long version, String jobName, String jobKey) {
        this.jobInstanceId = jobInstanceId;
        this.version = version;
        this.jobName = jobName;
        this.jobKey = jobKey;
    }
}
