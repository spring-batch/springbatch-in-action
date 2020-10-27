SELECT
    *
FROM BATCH_JOB_EXECUTION
WHERE 1=1
ORDER BY JOB_EXECUTION_ID DESC;

-- batch_로 시작하는 모든 instance 확인하기

-- batch_로 시작하는 모든 Job을 찾아보자
-- 1. JobInstance 찾기
SELECT
    B.JOB_INSTANCE_ID
     , A.JOB_EXECUTION_ID
     , B.JOB_NAME
FROM BATCH_JOB_EXECUTION A
         JOIN BATCH_JOB_INSTANCE B
              ON A.JOB_INSTANCE_ID = B.JOB_INSTANCE_ID
WHERE 1=1
  AND B.JOB_NAME = 'TOTAL_PROCESS_JOB'
ORDER BY JOB_EXECUTION_ID DESC
;

-- 2. JOB INSTACE에 대한 JOB_EXECUTION 찾기 (실행 정보 찾기)

SELECT
    B.JOB_INSTANCE_ID
     , B.JOB_NAME
     , COUNT(1) AS 'JOB_INSTANCE_ID 당 EXECUTE 건수'
FROM BATCH_JOB_EXECUTION A
         JOIN BATCH_JOB_INSTANCE B
              ON A.JOB_INSTANCE_ID = B.JOB_INSTANCE_ID
WHERE 1=1
  AND B.JOB_NAME = 'TOTAL_PROCESS_JOB'
GROUP BY JOB_INSTANCE_ID, JOB_NAME
;

-- Job 에 따른 Step 정보 보기
SELECT
    D.JOB_INSTANCE_ID          /* JOB 실행시 마다 생성되는 JobInstanceId    */
     , D.JOB_EXECUTION_ID       /* Job Instance에 따른 실행 Id             */
     , E.STEP_EXECUTION_ID      /* JobExecutionId에 따른 StepExecutionId  */
     , D.JOB_NAME               /* Job 실행시 파라미터 job.name             */
     , E.STEP_NAME              /* STEP 실행 NAME                        */
FROM (
         /* 파라미터 테이블과 조인 시 version, job.name, increment 값 설정 수 마다 row 추가 되기 때문에 group by로 jobInstanceId에 따른 jobExecutionId를 확인 */
         SELECT
             A.JOB_INSTANCE_ID
              , A.JOB_NAME
              , B.JOB_EXECUTION_ID
         FROM BATCH_JOB_INSTANCE A
                  JOIN BATCH_JOB_EXECUTION B
                       ON A.JOB_INSTANCE_ID = B.JOB_INSTANCE_ID
                  JOIN BATCH_JOB_EXECUTION_PARAMS C
                       ON B.JOB_EXECUTION_ID = C.JOB_EXECUTION_ID
         WHERE 1=1
           AND A.JOB_NAME = 'TOTAL_PROCESS_JOB'
         GROUP BY A.JOB_INSTANCE_ID, A.JOB_NAME, B.JOB_EXECUTION_ID
     ) D
         JOIN BATCH_STEP_EXECUTION E
              ON E.JOB_EXECUTION_ID = D.JOB_EXECUTION_ID
ORDER BY START_TIME DESC

-- 상세 보기

SELECT
    D.JOB_INSTANCE_ID          /* JOB 실행시 마다 생성되는 JobInstanceId    */
     , D.JOB_EXECUTION_ID       /* Job Instance에 따른 실행 Id             */
     , E.STEP_EXECUTION_ID      /* JobExecutionId에 따른 StepExecutionId  */
     , D.JOB_NAME               /* Job 실행시 파라미터 job.name             */
     , E.STEP_NAME              /* STEP 실행 NAME                        */
     , E.STATUS                 /* 수행 결과 */
     , E.START_TIME             /* 시작 시간 */
     , E.END_TIME               /* 종료 시간 */
     , E.COMMIT_COUNT           /* */
     , E.READ_COUNT             /* 데이터 건수 */
     , E.FILTER_COUNT           /* Processor에서 데이터 거른 건수 */
     , E.WRITE_COUNT            /* 총 건수 - 거른 건수 = Entity에 저장되어 DB에 Flush 될 건수 */
     , E.READ_SKIP_COUNT        /* */
     , E.WRITE_SKIP_COUNT       /* */
     , E.PROCESS_SKIP_COUNT     /* */
     , E.ROLLBACK_COUNT         /* 오류 또는 어떤 예외로 인한 Rollback 건수 */
     , E.EXIT_MESSAGE           /* 오류 발생 시 예외 메시지 */
     , E.LAST_UPDATED           /* 기존에 등록된 건수 (repeat 기능으로인한 update 가 될 듯) */
FROM (
         /* 파라미터 테이블과 조인 시 version, job.name, increment 값 설정 수 마다 row 추가 되기 때문에 group by로 jobInstanceId에 따른 jobExecutionId를 확인 */
         SELECT
             A.JOB_INSTANCE_ID
              , A.JOB_NAME
              , B.JOB_EXECUTION_ID
         FROM BATCH_JOB_INSTANCE A
                  JOIN BATCH_JOB_EXECUTION B
                       ON A.JOB_INSTANCE_ID = B.JOB_INSTANCE_ID
                  JOIN BATCH_JOB_EXECUTION_PARAMS C
                       ON B.JOB_EXECUTION_ID = C.JOB_EXECUTION_ID
         WHERE 1=1
           AND A.JOB_NAME = 'TOTAL_PROCESS_JOB'
         GROUP BY A.JOB_INSTANCE_ID, A.JOB_NAME, B.JOB_EXECUTION_ID
     ) D
         JOIN BATCH_STEP_EXECUTION E
              ON E.JOB_EXECUTION_ID = D.JOB_EXECUTION_ID
ORDER BY START_TIME DESC
;

-- JobInstance 당 몇 번 실행했는지 확인
SELECT
    JOB_INSTANCE_ID
FROM (
         /* 파라미터 테이블과 조인 시 version, job.name, increment 값 설정 수 마다 row 추가 되기 때문에 group by로 jobInstanceId에 따른 jobExecutionId를 확인 */
         SELECT
             A.JOB_INSTANCE_ID
              , A.JOB_NAME
              , B.JOB_EXECUTION_ID
         FROM BATCH_JOB_INSTANCE A
                  JOIN BATCH_JOB_EXECUTION B
                       ON A.JOB_INSTANCE_ID = B.JOB_INSTANCE_ID
                  JOIN BATCH_JOB_EXECUTION_PARAMS C
                       ON B.JOB_EXECUTION_ID = C.JOB_EXECUTION_ID
         WHERE 1=1
           AND A.JOB_NAME = 'TOTAL_PROCESS_JOB'
         GROUP BY A.JOB_INSTANCE_ID, A.JOB_NAME, B.JOB_EXECUTION_ID
     ) D
         JOIN BATCH_STEP_EXECUTION E
              ON E.JOB_EXECUTION_ID = D.JOB_EXECUTION_ID
GROUP BY JOB_INSTANCE_ID
;
