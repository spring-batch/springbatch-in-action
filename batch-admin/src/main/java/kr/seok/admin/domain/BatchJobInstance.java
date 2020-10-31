package kr.seok.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BatchJobInstance {
    private Long jobInstanceId;
    private Long version;
    private String jobName;
    private String jobKey;
}
