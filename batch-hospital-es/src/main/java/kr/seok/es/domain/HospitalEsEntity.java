package kr.seok.es.domain;

import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.Id;

@Getter
@Document(
        indexName = "hospital",
        shards = 1,
        createIndex = false
)
public class HospitalEsEntity {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String orgId;

    @Field(type = FieldType.Keyword)
    private String addr;
    @Field(type = FieldType.Keyword)
    private String hosCate;
    @Field(type = FieldType.Keyword)
    private String hosCateNm;

    @Field(type = FieldType.Keyword)
    private String fstAidMedicInsCd;
    @Field(type = FieldType.Keyword)
    private String fstAidMedicInsNm;

    @Field(type = FieldType.Keyword)
    private String edOperYn;
    @Field(type = FieldType.Keyword)
    private String etc;
    @Field(type = FieldType.Keyword)
    private String operDescDt;
    @Field(type = FieldType.Keyword)
    private String simpleMap;
    @Field(type = FieldType.Keyword)
    private String operNm;
    @Field(type = FieldType.Keyword)
    private String phone1;
    @Field(type = FieldType.Keyword)
    private String edPhone;
    @Field(type = FieldType.Keyword)
    private String operHourMonC;
    @Field(type = FieldType.Keyword)
    private String operHourTueC;
    @Field(type = FieldType.Keyword)
    private String operHourWedC;
    @Field(type = FieldType.Keyword)
    private String operHourThuC;
    @Field(type = FieldType.Keyword)
    private String operHourFriC;
    @Field(type = FieldType.Keyword)
    private String operHourSatC;
    @Field(type = FieldType.Keyword)
    private String operHourSunC;
    @Field(type = FieldType.Keyword)
    private String operHourHolC;
    @Field(type = FieldType.Keyword)
    private String operHourMonS;
    @Field(type = FieldType.Keyword)
    private String operHourTueS;
    @Field(type = FieldType.Keyword)
    private String operHourWedS;
    @Field(type = FieldType.Keyword)
    private String operHourThuS;
    @Field(type = FieldType.Keyword)
    private String operHourFriS;
    @Field(type = FieldType.Keyword)
    private String operHourSatS;
    @Field(type = FieldType.Keyword)
    private String operHourSunS;
    @Field(type = FieldType.Keyword)
    private String operHourHolS;

    @Field(type = FieldType.Keyword)
    private String zipCode1;
    @Field(type = FieldType.Keyword)
    private String zipCode2;

    @GeoPointField
    private Double lon;
    @GeoPointField
    private Double lat;

    @Field(type = FieldType.Date, format = DateFormat.date)
    private String date;
}
