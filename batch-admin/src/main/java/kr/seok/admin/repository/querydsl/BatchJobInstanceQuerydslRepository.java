package kr.seok.admin.repository.querydsl;

import kr.seok.admin.dto.JobInstanceDto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BatchJobInstanceQuerydslRepository {

    Map<String, Set<JobInstanceDto>> findJobInstanceGroupByJobName();
}
