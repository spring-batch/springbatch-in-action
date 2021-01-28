package kr.seok.es.repository;


import kr.seok.es.domain.HospitalEsEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface HospitalEsRepository extends ElasticsearchRepository<HospitalEsEntity, String> {

}
