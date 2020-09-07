package com.batch.demo.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_SIDO")
@Builder
public class Sido {

    @Id
    @Column(name = "ctprvn_code")
    private Integer ctprvnCode;
    @Column(name = "ctprvn_nm")
    private String ctprvnNm;
}
