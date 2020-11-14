package kr.seok.estate.domain.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
public class Address {

    @Id
    @GeneratedValue
    private Long id;
    private String addrCd;
    private String addrNm;
    private String useYn;

}
