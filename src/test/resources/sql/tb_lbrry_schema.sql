DROP TABLE dbmsexpert.TB_LBRRY;

CREATE TABLE dbmsexpert.TB_LBRRY
(
    LBRRY_CODE                 INT              NOT NULL,
    LBRRY_NM                   VARCHAR2(20)     NULL,
    LBRRY_SE                   VARCHAR2(20)     NULL,
    SEAT_Co                    VARCHAR2(20)     NULL,
    BOOK_Co                    INT              NULL,
    PBLICTN_Co                 INT              NULL,
    NONEBOOK_Co                INT              NULL,
    LON_Co                     INT              NULL,
    LONDAY_CNT                 INT              NULL,
    RDNM_adr                   VARCHAR2(200)    NULL,
    OPERINSTITUTION_NM         VARCHAR2(150)    NULL,
    LBRRY_PHONENUMBER          VARCHAR2(30)     NULL,
    HOMEPAGEURL                VARCHAR2(20)     NULL,
    WEEKDAY_OPER_OPEN_Hhmm     CHAR(5)      NULL,
    WEEKDAY_OPER_CLOSE_Hhmm    CHAR(5)      NULL,
    SAT_OPER_OPEN_Hhmm         CHAR(5)      NULL,
    SAT_OPER_CLOSE_Hhmm        CHAR(5)      NULL,
    HOLIDAY_OPER_OPEN_Hhmm     CHAR(5)      NULL,
    HOLIDAY_OPER_CLOSE_Hhmm    CHAR(5)      NULL,
    CTPRVN_CODE                INT              NOT NULL,
    SIGNGU_CODE                INT              NOT NULL,
    CONSTRAINT TB_LBRRY_PK PRIMARY KEY (LBRRY_CODE)
)
/
drop sequence dbmsexpert.TB_LBRRY_SEQ;
CREATE SEQUENCE dbmsexpert.TB_LBRRY_SEQ
START WITH 1
INCREMENT BY 1;
/

CREATE OR REPLACE TRIGGER dbmsexpert.TB_LBRRY_AI_TRG
BEFORE INSERT ON dbmsexpert.TB_LBRRY
REFERENCING NEW AS NEW FOR EACH ROW
BEGIN
    SELECT dbmsexpert.TB_LBRRY_SEQ.NEXTVAL
    INTO :NEW.LBRRY_CODE
    FROM DUAL;
END;
/

--DROP TRIGGER dbmsexpert.TB_LBRRY_AI_TRG;
/

--DROP SEQUENCE dbmsexpert.TB_LBRRY_SEQ;
/

COMMENT ON TABLE dbmsexpert.TB_LBRRY IS '도서관 데이터 관리 테이블'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.LBRRY_CODE IS '도서관 코드'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.LBRRY_NM IS '도서관 명'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.LBRRY_SE IS '도서관 유형'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.SEAT_Co IS '열람좌석 수'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.BOOK_Co IS '자료(도서) 수'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.PBLICTN_Co IS '자료(연간간행물) 수'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.NONEBOOK_Co IS '자료(비도서) 수'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.LON_Co IS '대출가능권 수'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.LONDAY_CNT IS '대출가능 일수'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.RDNM_adr IS '소재지도로명 주소'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.OPERINSTITUTION_NM IS '운영기관 명'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.LBRRY_PHONENUMBER IS '도서관 전화번호'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.HOMEPAGEURL IS '홈페이지주소'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.WEEKDAY_OPER_OPEN_Hhmm IS '평일 운영 시작 시간'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.WEEKDAY_OPER_CLOSE_Hhmm IS '평일 운영 종료 시간'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.SAT_OPER_OPEN_Hhmm IS '토요일 운영 시작 시간'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.SAT_OPER_CLOSE_Hhmm IS '토요일 운영 종료 시간'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.HOLIDAY_OPER_OPEN_Hhmm IS '공휴일 운영 시작 시간'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.HOLIDAY_OPER_CLOSE_Hhmm IS '공휴일 운영 종료 시간'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.CTPRVN_CODE IS '시도 코드'
/

COMMENT ON COLUMN dbmsexpert.TB_LBRRY.SIGNGU_CODE IS '시군구 코드'
/

commit;

