## 스프링 실행 시 로그 분석하기

### 스프링 로그 레벨
- 로그 레벨 설정

```properties
logging.level.root: INFO
logging.level.org.springframework: DEBUG
logging.level.org.springframework.batch: debug
```

- 로그 포맷 설정
[logback-spring.xml](/src/main/resources/logback-spring.xml)

### 스프링 debugging 
- **로그순서는 Job과 Step 의 구성에 따라 달라질 수 있음**
- LIBRARY_FILE_TO_TMP_JOB 프로세스
![LIBRARY_FILE_TO_TMP_JOB](/img/batchprocess/library-batch-debug-file-to-tmp.png "file to tmp process")

- 체크 포인트
    1. process flow 시 배치 이력은 어떻게 쌓고 있는지
    2. transaction 구간 확인
    
```
[2020-09-08 10:43:26.345] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(1687227181<open>)] for JPA transaction

# JOB_INSTANCE_ID 확인 LIBRARY_FILE_TO_TMP_JOB
SELECT JOB_INSTANCE_ID, JOB_NAME from BATCH_JOB_INSTANCE where JOB_NAME = 'LIBRARY_FILE_TO_TMP_JOB' and JOB_KEY = '0f7e63e9444c170fb33d21a9331f4001';

[2020-09-08 10:43:26.401] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(1687227181<open>)] after transaction
[2020-09-08 10:43:26.402] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(89772444<open>)] for JPA transaction

# 배치 인스턴스의 JOB_EXECUTION_ID 확인
SELECT JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, EXIT_CODE, EXIT_MESSAGE, CREATE_TIME, LAST_UPDATED, VERSION, JOB_CONFIGURATION_LOCATION from BATCH_JOB_EXECUTION E where JOB_INSTANCE_ID = 680 and JOB_EXECUTION_ID in (SELECT max(JOB_EXECUTION_ID) from BATCH_JOB_EXECUTION E2 where E2.JOB_INSTANCE_ID = 680);

[2020-09-08 10:43:26.414] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(89772444<open>)] after transaction
[2020-09-08 10:43:26.415] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(521611410<open>)] for JPA transaction

# JOB_EXECUTION_ID 정보를 통해 파라미터 JOB_EXECUTION_ID 조회 
SELECT JOB_EXECUTION_ID, KEY_NAME, TYPE_CD, STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL, IDENTIFYING from BATCH_JOB_EXECUTION_PARAMS where JOB_EXECUTION_ID = 717;

# 실행 context 조회 
SELECT SHORT_CONTEXT, SERIALIZED_CONTEXT FROM BATCH_JOB_EXECUTION_CONTEXT WHERE JOB_EXECUTION_ID = 717;

# JOB_EXECUTION_ID 를 통해 Step 이력 확인 
SELECT STEP_EXECUTION_ID, STEP_NAME, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, EXIT_CODE, EXIT_MESSAGE, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, LAST_UPDATED, VERSION from BATCH_STEP_EXECUTION where JOB_EXECUTION_ID = 717 order by STEP_EXECUTION_ID;

# 해당 프로젝트의 인스턴스 정보, 실행 정보 입력 
INSERT into BATCH_JOB_INSTANCE(JOB_INSTANCE_ID, JOB_NAME, JOB_KEY, VERSION) values (?, ?, ?, ?);
INSERT into BATCH_JOB_EXECUTION(JOB_EXECUTION_ID, JOB_INSTANCE_ID, START_TIME, END_TIME, STATUS, EXIT_CODE, EXIT_MESSAGE, VERSION, CREATE_TIME, LAST_UPDATED, JOB_CONFIGURATION_LOCATION) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
INSERT into BATCH_JOB_EXECUTION_PARAMS(JOB_EXECUTION_ID, KEY_NAME, TYPE_CD, STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL, IDENTIFYING) values (?, ?, ?, ?, ?, ?, ?, ?);
INSERT into BATCH_JOB_EXECUTION_PARAMS(JOB_EXECUTION_ID, KEY_NAME, TYPE_CD, STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL, IDENTIFYING) values (?, ?, ?, ?, ?, ?, ?, ?);
INSERT INTO BATCH_JOB_EXECUTION_CONTEXT (SHORT_CONTEXT, SERIALIZED_CONTEXT, JOB_EXECUTION_ID) VALUES(?, ?, ?);

[2020-09-08 10:43:26.522] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(521611410<open>)] after transaction
[2020-09-08 10:43:26.523]  INFO [o.s.b.c.l.s.SimpleJobLauncher$1.run:142] - Job: [SimpleJob: [name=LIBRARY_FILE_TO_TMP_JOB]] launched with the following parameters: [{version=8, -job.name=LIBRARY_FILE_TO_TMP_JOB}]
[2020-09-08 10:43:26.524] DEBUG [o.s.b.c.j.AbstractJob.execute:296] - Job execution starting: JobExecution: id=717, version=0, startTime=null, endTime=null, lastUpdated=Tue Sep 08 10:43:26 KST 2020, status=STARTING, exitStatus=exitCode=UNKNOWN;exitDescription=, job=[JobInstance: id=680, version=0, Job=[LIBRARY_FILE_TO_TMP_JOB]], jobParameters=[{version=8, -job.name=LIBRARY_FILE_TO_TMP_JOB}]
[2020-09-08 10:43:26.530] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(677930699<open>)] for JPA transaction

# 버전 정보 및 실행 이력 횟수 조회 
SELECT VERSION FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID= 717;
SELECT COUNT(*) FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID = 717;
UPDATE BATCH_JOB_EXECUTION set START_TIME = ?, END_TIME = ?,  STATUS = ?, EXIT_CODE = ?, EXIT_MESSAGE = ?, VERSION = ?, CREATE_TIME = ?, LAST_UPDATED = ? where JOB_EXECUTION_ID = ? and VERSION = ?;

[2020-09-08 10:43:26.565] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(677930699<open>)] after transaction
[2020-09-08 10:43:26.569] DEBUG [o.s.d.r.c.s.TransactionalRepositoryProxyPostProcessor$AbstractFallbackTransactionAttributeSource.getTransactionAttribute:355] - Adding transactional method 'deleteAll' with attribute: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
[2020-09-08 10:43:26.571] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.getTransaction:372] - Creating new transaction with name [org.springframework.data.jpa.repository.support.SimpleJpaRepository.deleteAll]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
[2020-09-08 10:43:26.572] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(2121754508<open>)] for JPA transaction
[2020-09-08 10:43:26.575] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:420] - Exposing JPA transaction as JDBC [org.springframework.orm.jpa.vendor.HibernateJpaDialect$HibernateConnectionHandle@113eed88]
[2020-09-08 10:43:26.596]  INFO [o.h.h.i.QueryTranslatorFactoryInitiator.initiateService:47] - HHH000397: Using ASTQueryTranslatorFactory
[2020-09-08 10:43:26.986] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.processCommit:743] - Initiating transaction commit
[2020-09-08 10:43:26.986] DEBUG [o.s.o.j.JpaTransactionManager.doCommit:528] - Committing JPA transaction on EntityManager [SessionImpl(2121754508<open>)]
[2020-09-08 10:43:36.227] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(2121754508<open>)] after transaction
[2020-09-08 10:43:36.228] DEBUG [o.s.d.r.c.s.TransactionalRepositoryProxyPostProcessor$AbstractFallbackTransactionAttributeSource.getTransactionAttribute:355] - Adding transactional method 'findAll' with attribute: PROPAGATION_REQUIRED,ISOLATION_DEFAULT,readOnly
[2020-09-08 10:43:36.228] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.getTransaction:372] - Creating new transaction with name [org.springframework.data.jpa.repository.support.SimpleJpaRepository.findAll]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT,readOnly
[2020-09-08 10:43:36.229] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(454167880<open>)] for JPA transaction
[2020-09-08 10:43:36.229] DEBUG [o.s.j.d.DataSourceUtils.prepareConnectionForTransaction:183] - Setting JDBC Connection [HikariProxyConnection@594491525 wrapping com.mysql.cj.jdbc.ConnectionImpl@14ee143a] read-only
[2020-09-08 10:43:36.233] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:420] - Exposing JPA transaction as JDBC [org.springframework.orm.jpa.vendor.HibernateJpaDialect$HibernateConnectionHandle@78324e97]
[2020-09-08 10:43:36.239] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.processCommit:743] - Initiating transaction commit
[2020-09-08 10:43:36.239] DEBUG [o.s.o.j.JpaTransactionManager.doCommit:528] - Committing JPA transaction on EntityManager [SessionImpl(454167880<open>)]
[2020-09-08 10:43:36.245] DEBUG [o.s.j.d.DataSourceUtils.resetConnectionAfterTransaction:240] - Resetting read-only flag of JDBC Connection [HikariProxyConnection@594491525 wrapping com.mysql.cj.jdbc.ConnectionImpl@14ee143a]
[2020-09-08 10:43:36.247] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(454167880<open>)] after transaction
[2020-09-08 10:43:36.247]  INFO [c.b.d.l.l.LibraryTmpJobListener.beforeJob:23] - [LOG] [CSV_TABLE] [SIZE] [0]
[2020-09-08 10:43:36.248] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(1995338497<open>)] for JPA transaction

# Job 실행 이력 상세
SELECT JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, EXIT_CODE, EXIT_MESSAGE, CREATE_TIME, LAST_UPDATED, VERSION, JOB_CONFIGURATION_LOCATION 
from BATCH_JOB_EXECUTION where JOB_INSTANCE_ID = 680 order by JOB_EXECUTION_ID desc;

# JOB_EXECUTION_ID의 실행 이력
SELECT JOB_EXECUTION_ID, KEY_NAME, TYPE_CD, STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL, IDENTIFYING 
from BATCH_JOB_EXECUTION_PARAMS where JOB_EXECUTION_ID = 717;

# Step 실행 이력 상세 
SELECT STEP_EXECUTION_ID, STEP_NAME, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, EXIT_CODE, EXIT_MESSAGE, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, LAST_UPDATED, VERSION 
from BATCH_STEP_EXECUTION where JOB_EXECUTION_ID = 717 order by STEP_EXECUTION_ID;

[2020-09-08 10:43:36.274] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(1995338497<open>)] after transaction
[2020-09-08 10:43:36.275] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(635586824<open>)] for JPA transaction

# Job 실행 이력 상세
SELECT JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, EXIT_CODE, EXIT_MESSAGE, CREATE_TIME, LAST_UPDATED, VERSION, JOB_CONFIGURATION_LOCATION 
from BATCH_JOB_EXECUTION where JOB_INSTANCE_ID = 680 order by JOB_EXECUTION_ID desc;

# Job 파라미터 이력 확인
SELECT JOB_EXECUTION_ID, KEY_NAME, TYPE_CD, STRING_VAL, DATE_VAL, LONG_VAL, DOUBLE_VAL, IDENTIFYING 
from BATCH_JOB_EXECUTION_PARAMS where JOB_EXECUTION_ID = 717;

# Step 실행 이력 상세 
SELECT STEP_EXECUTION_ID, STEP_NAME, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, EXIT_CODE, EXIT_MESSAGE, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, LAST_UPDATED, VERSION 
from BATCH_STEP_EXECUTION where JOB_EXECUTION_ID = 717 order by STEP_EXECUTION_ID;

[2020-09-08 10:43:36.311] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(635586824<open>)] after transaction
[2020-09-08 10:43:36.313] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(1312137753<open>)] for JPA transaction

# insert step history
INSERT into BATCH_STEP_EXECUTION(STEP_EXECUTION_ID, VERSION, STEP_NAME, JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS, COMMIT_COUNT, READ_COUNT, FILTER_COUNT, WRITE_COUNT, EXIT_CODE, EXIT_MESSAGE, READ_SKIP_COUNT, WRITE_SKIP_COUNT, PROCESS_SKIP_COUNT, ROLLBACK_COUNT, LAST_UPDATED) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
# insert step history summary
INSERT INTO BATCH_STEP_EXECUTION_CONTEXT (SHORT_CONTEXT, SERIALIZED_CONTEXT, STEP_EXECUTION_ID) VALUES(?, ?, ?);

[2020-09-08 10:43:36.372] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(1312137753<open>)] after transaction
[2020-09-08 10:43:36.373]  INFO [o.s.b.c.j.SimpleStepHandler.handleStep:146] - Executing step: [LIBRARY_FILE_TO_TMP_JOB_STEP]
[2020-09-08 10:43:36.373] DEBUG [o.s.b.c.s.AbstractStep.execute:187] - Executing: id=807
[2020-09-08 10:43:36.374] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(148647961<open>)] for JPA transaction

# insert 
UPDATE BATCH_STEP_EXECUTION set START_TIME = ?, END_TIME = ?, STATUS = ?, COMMIT_COUNT = ?, READ_COUNT = ?, FILTER_COUNT = ?, WRITE_COUNT = ?, EXIT_CODE = ?, EXIT_MESSAGE = ?, VERSION = ?, READ_SKIP_COUNT = ?, PROCESS_SKIP_COUNT = ?, WRITE_SKIP_COUNT = ?, ROLLBACK_COUNT = ?, LAST_UPDATED = ? where STEP_EXECUTION_ID = ? and VERSION = ?;
SELECT VERSION FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID= 717;

[2020-09-08 10:43:36.392] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(148647961<open>)] after transaction
[2020-09-08 10:43:36.394] DEBUG [o.s.b.c.s.StepScope.get:109] - Creating object in scope=step, name=scopedTarget.LIBRARY_FILE_READER
[2020-09-08 10:43:36.400] DEBUG [o.s.b.c.s.StepScope.registerDestructionCallback:140] - Registered destruction callback in scope=step, name=scopedTarget.LIBRARY_FILE_READER
[2020-09-08 10:43:36.418] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(2058613140<open>)] for JPA transaction

UPDATE BATCH_STEP_EXECUTION_CONTEXT SET SHORT_CONTEXT = ?, SERIALIZED_CONTEXT = ? WHERE STEP_EXECUTION_ID = ?;

[2020-09-08 10:43:36.500] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(2058613140<open>)] after transaction
[2020-09-08 10:43:36.507] DEBUG [o.s.b.r.s.RepeatTemplate.start:479] - Starting repeat context.
[2020-09-08 10:43:36.508] DEBUG [o.s.b.r.s.RepeatTemplate.getNextResult:373] - Repeat operation about to start at count=1
[2020-09-08 10:43:36.509] DEBUG [o.s.b.c.s.c.StepContextRepeatCallback.doInIteration:70] - Preparing chunk execution for StepContext: org.springframework.batch.core.scope.context.StepContext@1f2497d9
[2020-09-08 10:43:36.509] DEBUG [o.s.b.c.s.c.StepContextRepeatCallback.doInIteration:80] - Chunk execution starting: queue size=0
[2020-09-08 10:43:36.513] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.getTransaction:372] - Creating new transaction with name [null]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
[2020-09-08 10:43:36.515] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(2036515285<open>)] for JPA transaction
[2020-09-08 10:43:37.147] DEBUG [o.s.b.c.s.StepScope.get:109] - Creating object in scope=step, name=scopedTarget.LIBRARY_DTO_TO_ENTITY_PROCESSOR
[2020-09-08 10:43:37.156]  INFO [c.b.c.l.CustomItemWriterListener.beforeWrite:23] - [LOG] [Writer] [BEFORE] [1000]
[2020-09-08 10:43:37.157] DEBUG [o.s.b.c.s.StepScope.get:109] - Creating object in scope=step, name=scopedTarget.LIBRARY_ENTITY_WRITER
[2020-09-08 10:43:37.163] DEBUG [o.s.b.i.d.JpaItemWriter.doWrite:97] - Writing to JPA with 1000 items.
[2020-09-08 10:43:39.036] DEBUG [o.s.b.i.d.JpaItemWriter.doWrite:109] - 1000 entities merged.
[2020-09-08 10:43:39.036] DEBUG [o.s.b.i.d.JpaItemWriter.doWrite:110] - 0 entities found in persistence context.
[2020-09-08 10:43:40.549]  INFO [c.b.c.l.CustomItemWriterListener.afterWrite:29] - [LOG] [Writer] [AFTER] [1000]
[2020-09-08 10:43:40.549] DEBUG [o.s.b.c.s.i.ChunkOrientedTasklet.execute:89] - Inputs not busy, ended: false
[2020-09-08 10:43:40.549] DEBUG [o.s.b.c.s.t.TaskletStep$ChunkTransactionCallback.doInTransaction:439] - Applying contribution: [StepContribution: read=1000, written=1000, filtered=0, readSkips=0, writeSkips=0, processSkips=0, exitStatus=EXECUTING]
[2020-09-08 10:43:40.550] DEBUG [o.s.o.j.JpaTransactionManager.doGetTransaction:356] - Found thread-bound EntityManager [SessionImpl(2036515285<open>)] for JPA transaction
[2020-09-08 10:43:40.550] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.handleExistingTransaction:473] - Participating in existing transaction
[2020-09-08 10:43:40.550] DEBUG [o.s.j.c.JdbcTemplate.update:860] - Executing prepared SQL update

UPDATE BATCH_STEP_EXECUTION_CONTEXT SET SHORT_CONTEXT = ?, SERIALIZED_CONTEXT = ? WHERE STEP_EXECUTION_ID = ?;

[2020-09-08 10:43:40.553] DEBUG [o.s.b.c.s.t.TaskletStep$ChunkTransactionCallback.doInTransaction:455] - Saving step execution before commit: StepExecution: id=807, version=1, name=LIBRARY_FILE_TO_TMP_JOB_STEP, status=STARTED, exitStatus=EXECUTING, readCount=1000, filterCount=0, writeCount=1000 readSkipCount=0, writeSkipCount=0, processSkipCount=0, commitCount=1, rollbackCount=0, exitDescription=
[2020-09-08 10:43:40.553] DEBUG [o.s.o.j.JpaTransactionManager.doGetTransaction:356] - Found thread-bound EntityManager [SessionImpl(2036515285<open>)] for JPA transaction
[2020-09-08 10:43:40.554] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.handleExistingTransaction:473] - Participating in existing transaction
[2020-09-08 10:43:40.554] DEBUG [o.s.j.c.JdbcTemplate.update:860] - Executing prepared SQL update

UPDATE BATCH_STEP_EXECUTION set START_TIME = ?, END_TIME = ?, STATUS = ?, COMMIT_COUNT = ?, READ_COUNT = ?, FILTER_COUNT = ?, WRITE_COUNT = ?, EXIT_CODE = ?, EXIT_MESSAGE = ?, VERSION = ?, READ_SKIP_COUNT = ?, PROCESS_SKIP_COUNT = ?, WRITE_SKIP_COUNT = ?, ROLLBACK_COUNT = ?, LAST_UPDATED = ? where STEP_EXECUTION_ID = ? and VERSION = ?;
SELECT VERSION FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID= 717;

[2020-09-08 10:43:40.561] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.processCommit:743] - Initiating transaction commit
[2020-09-08 10:43:40.562] DEBUG [o.s.o.j.JpaTransactionManager.doCommit:528] - Committing JPA transaction on EntityManager [SessionImpl(2036515285<open>)]
[2020-09-08 10:43:40.570] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(2036515285<open>)] after transaction
[2020-09-08 10:43:40.570] DEBUG [o.s.b.r.s.RepeatTemplate.getNextResult:373] - Repeat operation about to start at count=2
[2020-09-08 10:43:40.570] DEBUG [o.s.b.c.s.c.StepContextRepeatCallback.doInIteration:70] - Preparing chunk execution for StepContext: org.springframework.batch.core.scope.context.StepContext@1f2497d9
[2020-09-08 10:43:40.571] DEBUG [o.s.b.c.s.c.StepContextRepeatCallback.doInIteration:80] - Chunk execution starting: queue size=0
[2020-09-08 10:43:40.571] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.getTransaction:372] - Creating new transaction with name [null]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
[2020-09-08 10:43:40.571] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(1378863496<open>)] for JPA transaction
[2020-09-08 10:43:40.734]  INFO [c.b.c.l.CustomItemWriterListener.beforeWrite:23] - [LOG] [Writer] [BEFORE] [1000]
[2020-09-08 10:43:40.734] DEBUG [o.s.b.i.d.JpaItemWriter.doWrite:97] - Writing to JPA with 1000 items.
[2020-09-08 10:43:43.324] DEBUG [o.s.b.i.d.JpaItemWriter.doWrite:109] - 1000 entities merged.
[2020-09-08 10:43:43.325] DEBUG [o.s.b.i.d.JpaItemWriter.doWrite:110] - 0 entities found in persistence context.
[2020-09-08 10:43:44.793]  INFO [c.b.c.l.CustomItemWriterListener.afterWrite:29] - [LOG] [Writer] [AFTER] [1000]
[2020-09-08 10:43:44.794] DEBUG [o.s.b.c.s.i.ChunkOrientedTasklet.execute:89] - Inputs not busy, ended: false
[2020-09-08 10:43:44.794] DEBUG [o.s.b.c.s.t.TaskletStep$ChunkTransactionCallback.doInTransaction:439] - Applying contribution: [StepContribution: read=1000, written=1000, filtered=0, readSkips=0, writeSkips=0, processSkips=0, exitStatus=EXECUTING]
[2020-09-08 10:43:44.794] DEBUG [o.s.o.j.JpaTransactionManager.doGetTransaction:356] - Found thread-bound EntityManager [SessionImpl(1378863496<open>)] for JPA transaction
[2020-09-08 10:43:44.794] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.handleExistingTransaction:473] - Participating in existing transaction
[2020-09-08 10:43:44.794] DEBUG [o.s.j.c.JdbcTemplate.update:860] - Executing prepared SQL update

UPDATE BATCH_STEP_EXECUTION_CONTEXT SET SHORT_CONTEXT = ?, SERIALIZED_CONTEXT = ? WHERE STEP_EXECUTION_ID = ?;

[2020-09-08 10:43:44.798] DEBUG [o.s.b.c.s.t.TaskletStep$ChunkTransactionCallback.doInTransaction:455] - Saving step execution before commit: StepExecution: id=807, version=2, name=LIBRARY_FILE_TO_TMP_JOB_STEP, status=STARTED, exitStatus=EXECUTING, readCount=2000, filterCount=0, writeCount=2000 readSkipCount=0, writeSkipCount=0, processSkipCount=0, commitCount=2, rollbackCount=0, exitDescription=
[2020-09-08 10:43:44.799] DEBUG [o.s.o.j.JpaTransactionManager.doGetTransaction:356] - Found thread-bound EntityManager [SessionImpl(1378863496<open>)] for JPA transaction
[2020-09-08 10:43:44.799] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.handleExistingTransaction:473] - Participating in existing transaction
[2020-09-08 10:43:44.799] DEBUG [o.s.j.c.JdbcTemplate.update:860] - Executing prepared SQL update

UPDATE BATCH_STEP_EXECUTION set START_TIME = ?, END_TIME = ?, STATUS = ?, COMMIT_COUNT = ?, READ_COUNT = ?, FILTER_COUNT = ?, WRITE_COUNT = ?, EXIT_CODE = ?, EXIT_MESSAGE = ?, VERSION = ?, READ_SKIP_COUNT = ?, PROCESS_SKIP_COUNT = ?, WRITE_SKIP_COUNT = ?, ROLLBACK_COUNT = ?, LAST_UPDATED = ? where STEP_EXECUTION_ID = ? and VERSION = ?;

[2020-09-08 10:43:44.803] DEBUG [o.s.j.c.JdbcTemplate.query:667] - Executing prepared SQL query

SELECT VERSION FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID=?;

[2020-09-08 10:43:44.807] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.processCommit:743] - Initiating transaction commit
[2020-09-08 10:43:44.807] DEBUG [o.s.o.j.JpaTransactionManager.doCommit:528] - Committing JPA transaction on EntityManager [SessionImpl(1378863496<open>)]
[2020-09-08 10:43:44.816] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(1378863496<open>)] after transaction
[2020-09-08 10:43:44.816] DEBUG [o.s.b.r.s.RepeatTemplate.getNextResult:373] - Repeat operation about to start at count=3
[2020-09-08 10:43:44.816] DEBUG [o.s.b.c.s.c.StepContextRepeatCallback.doInIteration:70] - Preparing chunk execution for StepContext: org.springframework.batch.core.scope.context.StepContext@1f2497d9
[2020-09-08 10:43:44.816] DEBUG [o.s.b.c.s.c.StepContextRepeatCallback.doInIteration:80] - Chunk execution starting: queue size=0
[2020-09-08 10:43:44.817] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.getTransaction:372] - Creating new transaction with name [null]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
[2020-09-08 10:43:44.817] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(950205379<open>)] for JPA transaction
[2020-09-08 10:43:44.819] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:420] - Exposing JPA transaction as JDBC 
[2020-09-08 10:43:44.968]  INFO [c.b.c.l.CustomItemWriterListener.beforeWrite:23] - [LOG] [Writer] [BEFORE] [1000]
[2020-09-08 10:43:44.968] DEBUG [o.s.b.i.d.JpaItemWriter.doWrite:97] - Writing to JPA with 1000 items.
[2020-09-08 10:43:48.050] DEBUG [o.s.b.i.d.JpaItemWriter.doWrite:109] - 1000 entities merged.
[2020-09-08 10:43:48.050] DEBUG [o.s.b.i.d.JpaItemWriter.doWrite:110] - 0 entities found in persistence context.
[2020-09-08 10:43:49.776]  INFO [c.b.c.l.CustomItemWriterListener.afterWrite:29] - [LOG] [Writer] [AFTER] [1000]
[2020-09-08 10:43:49.776] DEBUG [o.s.b.c.s.i.ChunkOrientedTasklet.execute:89] - Inputs not busy, ended: false
[2020-09-08 10:43:49.776] DEBUG [o.s.b.c.s.t.TaskletStep$ChunkTransactionCallback.doInTransaction:439] - Applying contribution: [StepContribution: read=1000, written=1000, filtered=0, readSkips=0, writeSkips=0, processSkips=0, exitStatus=EXECUTING]
[2020-09-08 10:43:49.776] DEBUG [o.s.o.j.JpaTransactionManager.doGetTransaction:356] - Found thread-bound EntityManager [SessionImpl(950205379<open>)] for JPA transaction
[2020-09-08 10:43:49.776] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.handleExistingTransaction:473] - Participating in existing transaction
[2020-09-08 10:43:49.777] DEBUG [o.s.j.c.JdbcTemplate.update:860] - Executing prepared SQL update

UPDATE BATCH_STEP_EXECUTION_CONTEXT SET SHORT_CONTEXT = ?, SERIALIZED_CONTEXT = ? WHERE STEP_EXECUTION_ID = ?;

[2020-09-08 10:43:49.780] DEBUG [o.s.b.c.s.t.TaskletStep$ChunkTransactionCallback.doInTransaction:455] - Saving step execution before commit: StepExecution: id=807, version=3, name=LIBRARY_FILE_TO_TMP_JOB_STEP, status=STARTED, exitStatus=EXECUTING, readCount=3000, filterCount=0, writeCount=3000 readSkipCount=0, writeSkipCount=0, processSkipCount=0, commitCount=3, rollbackCount=0, exitDescription=
[2020-09-08 10:43:49.780] DEBUG [o.s.o.j.JpaTransactionManager.doGetTransaction:356] - Found thread-bound EntityManager [SessionImpl(950205379<open>)] for JPA transaction
[2020-09-08 10:43:49.780] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.handleExistingTransaction:473] - Participating in existing transaction
[2020-09-08 10:43:49.780] DEBUG [o.s.j.c.JdbcTemplate.update:860] - Executing prepared SQL update

UPDATE BATCH_STEP_EXECUTION set START_TIME = ?, END_TIME = ?, STATUS = ?, COMMIT_COUNT = ?, READ_COUNT = ?, FILTER_COUNT = ?, WRITE_COUNT = ?, EXIT_CODE = ?, EXIT_MESSAGE = ?, VERSION = ?, READ_SKIP_COUNT = ?, PROCESS_SKIP_COUNT = ?, WRITE_SKIP_COUNT = ?, ROLLBACK_COUNT = ?, LAST_UPDATED = ? where STEP_EXECUTION_ID = ? and VERSION = ?;

[2020-09-08 10:43:49.783] DEBUG [o.s.j.c.JdbcTemplate.query:667] - Executing prepared SQL query

SELECT VERSION FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID=?;

[2020-09-08 10:43:49.786] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.processCommit:743] - Initiating transaction commit
[2020-09-08 10:43:49.786] DEBUG [o.s.o.j.JpaTransactionManager.doCommit:528] - Committing JPA transaction on EntityManager [SessionImpl(950205379<open>)]
[2020-09-08 10:43:49.793] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(950205379<open>)] after transaction
[2020-09-08 10:43:49.794] DEBUG [o.s.b.r.s.RepeatTemplate.getNextResult:373] - Repeat operation about to start at count=4
[2020-09-08 10:43:49.794] DEBUG [o.s.b.c.s.c.StepContextRepeatCallback.doInIteration:70] - Preparing chunk execution for StepContext: org.springframework.batch.core.scope.context.StepContext@1f2497d9
[2020-09-08 10:43:49.794] DEBUG [o.s.b.c.s.c.StepContextRepeatCallback.doInIteration:80] - Chunk execution starting: queue size=0
[2020-09-08 10:43:49.794] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.getTransaction:372] - Creating new transaction with name [null]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
[2020-09-08 10:43:49.794] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(1117388522<open>)] for JPA transaction
[2020-09-08 10:43:49.834]  INFO [c.b.c.l.CustomItemWriterListener.beforeWrite:23] - [LOG] [Writer] [BEFORE] [318]
[2020-09-08 10:43:49.834] DEBUG [o.s.b.i.d.JpaItemWriter.doWrite:97] - Writing to JPA with 318 items.
[2020-09-08 10:43:50.836] DEBUG [o.s.b.i.d.JpaItemWriter.doWrite:109] - 318 entities merged.
[2020-09-08 10:43:50.837] DEBUG [o.s.b.i.d.JpaItemWriter.doWrite:110] - 0 entities found in persistence context.
[2020-09-08 10:43:51.307]  INFO [c.b.c.l.CustomItemWriterListener.afterWrite:29] - [LOG] [Writer] [AFTER] [318]
[2020-09-08 10:43:51.307] DEBUG [o.s.b.c.s.i.ChunkOrientedTasklet.execute:89] - Inputs not busy, ended: true
[2020-09-08 10:43:51.308] DEBUG [o.s.b.c.s.t.TaskletStep$ChunkTransactionCallback.doInTransaction:439] - Applying contribution: [StepContribution: read=318, written=318, filtered=0, readSkips=0, writeSkips=0, processSkips=0, exitStatus=EXECUTING]
[2020-09-08 10:43:51.308] DEBUG [o.s.o.j.JpaTransactionManager.doGetTransaction:356] - Found thread-bound EntityManager [SessionImpl(1117388522<open>)] for JPA transaction
[2020-09-08 10:43:51.308] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.handleExistingTransaction:473] - Participating in existing transaction
[2020-09-08 10:43:51.308] DEBUG [o.s.j.c.JdbcTemplate.update:860] - Executing prepared SQL update


UPDATE BATCH_STEP_EXECUTION_CONTEXT SET SHORT_CONTEXT = ?, SERIALIZED_CONTEXT = ? WHERE STEP_EXECUTION_ID = ?;

[2020-09-08 10:43:51.311] DEBUG [o.s.b.c.s.t.TaskletStep$ChunkTransactionCallback.doInTransaction:455] - Saving step execution before commit: StepExecution: id=807, version=4, name=LIBRARY_FILE_TO_TMP_JOB_STEP, status=STARTED, exitStatus=EXECUTING, readCount=3318, filterCount=0, writeCount=3318 readSkipCount=0, writeSkipCount=0, processSkipCount=0, commitCount=4, rollbackCount=0, exitDescription=
[2020-09-08 10:43:51.312] DEBUG [o.s.o.j.JpaTransactionManager.doGetTransaction:356] - Found thread-bound EntityManager [SessionImpl(1117388522<open>)] for JPA transaction
[2020-09-08 10:43:51.312] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.handleExistingTransaction:473] - Participating in existing transaction
[2020-09-08 10:43:51.312] DEBUG [o.s.j.c.JdbcTemplate.update:860] - Executing prepared SQL update

UPDATE BATCH_STEP_EXECUTION set START_TIME = ?, END_TIME = ?, STATUS = ?, COMMIT_COUNT = ?, READ_COUNT = ?, FILTER_COUNT = ?, WRITE_COUNT = ?, EXIT_CODE = ?, EXIT_MESSAGE = ?, VERSION = ?, READ_SKIP_COUNT = ?, PROCESS_SKIP_COUNT = ?, WRITE_SKIP_COUNT = ?, ROLLBACK_COUNT = ?, LAST_UPDATED = ? where STEP_EXECUTION_ID = ? and VERSION = ?;

[2020-09-08 10:43:51.315] DEBUG [o.s.j.c.JdbcTemplate.query:667] - Executing prepared SQL query

SELECT VERSION FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID=?;

[2020-09-08 10:43:51.319] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.processCommit:743] - Initiating transaction commit
[2020-09-08 10:43:51.319] DEBUG [o.s.o.j.JpaTransactionManager.doCommit:528] - Committing JPA transaction on EntityManager [SessionImpl(1117388522<open>)]
[2020-09-08 10:43:51.326] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(1117388522<open>)] after transaction
[2020-09-08 10:43:51.327] DEBUG [o.s.b.r.s.RepeatTemplate.isComplete:447] - Repeat is complete according to policy and result value.
[2020-09-08 10:43:51.327] DEBUG [o.s.b.c.s.AbstractStep.execute:218] - Step execution success: id=807
[2020-09-08 10:43:51.327]  INFO [c.b.c.l.CustomStepListener.afterStep:19] - [LOG] [STEP] [AFTER] [COMPLETED]
[2020-09-08 10:43:51.327] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.getTransaction:372] - Creating new transaction with name [org.springframework.batch.core.repository.support.SimpleJobRepository.updateExecutionContext]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
[2020-09-08 10:43:51.327] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(2118462763<open>)] for JPA transaction

UPDATE BATCH_STEP_EXECUTION_CONTEXT SET SHORT_CONTEXT = ?, SERIALIZED_CONTEXT = ? WHERE STEP_EXECUTION_ID = ?;

[2020-09-08 10:43:51.333] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.processCommit:743] - Initiating transaction commit
[2020-09-08 10:43:51.333] DEBUG [o.s.o.j.JpaTransactionManager.doCommit:528] - Committing JPA transaction on EntityManager [SessionImpl(2118462763<open>)]
[2020-09-08 10:43:51.338] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(2118462763<open>)] after transaction
[2020-09-08 10:43:51.338] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(617765955<open>)] for JPA transaction

UPDATE BATCH_STEP_EXECUTION set START_TIME = ?, END_TIME = ?, STATUS = ?, COMMIT_COUNT = ?, READ_COUNT = ?, FILTER_COUNT = ?, WRITE_COUNT = ?, EXIT_CODE = ?, EXIT_MESSAGE = ?, VERSION = ?, READ_SKIP_COUNT = ?, PROCESS_SKIP_COUNT = ?, WRITE_SKIP_COUNT = ?, ROLLBACK_COUNT = ?, LAST_UPDATED = ? where STEP_EXECUTION_ID = ? and VERSION = ?;
SELECT VERSION FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID=?;

[2020-09-08 10:43:51.352] DEBUG [o.s.b.c.s.AbstractStep.execute:284] - Step execution complete: StepExecution: id=807, version=6, name=LIBRARY_FILE_TO_TMP_JOB_STEP, status=COMPLETED, exitStatus=COMPLETED, readCount=3318, filterCount=0, writeCount=3318 readSkipCount=0, writeSkipCount=0, processSkipCount=0, commitCount=4, rollbackCount=0
[2020-09-08 10:43:51.352] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.getTransaction:372] - Creating new transaction with name [org.springframework.batch.core.repository.support.SimpleJobRepository.updateExecutionContext]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
[2020-09-08 10:43:51.352] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(1821935650<open>)] for JPA transaction

UPDATE BATCH_JOB_EXECUTION_CONTEXT SET SHORT_CONTEXT = ?, SERIALIZED_CONTEXT = ? WHERE JOB_EXECUTION_ID = ?;

[2020-09-08 10:43:51.357] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.processCommit:743] - Initiating transaction commit
[2020-09-08 10:43:51.357] DEBUG [o.s.o.j.JpaTransactionManager.doCommit:528] - Committing JPA transaction on EntityManager [SessionImpl(1821935650<open>)]
[2020-09-08 10:43:51.361] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(1821935650<open>)] after transaction
[2020-09-08 10:43:51.361] DEBUG [o.s.b.c.j.SimpleJob.doExecute:149] - Upgrading JobExecution status: StepExecution: id=807, version=6, name=LIBRARY_FILE_TO_TMP_JOB_STEP, status=COMPLETED, exitStatus=COMPLETED, readCount=3318, filterCount=0, writeCount=3318 readSkipCount=0, writeSkipCount=0, processSkipCount=0, commitCount=4, rollbackCount=0, exitDescription=
[2020-09-08 10:43:51.362] DEBUG [o.s.b.c.j.AbstractJob.execute:315] - Job execution complete: JobExecution: id=717, version=1, startTime=Tue Sep 08 10:43:26 KST 2020, endTime=null, lastUpdated=Tue Sep 08 10:43:26 KST 2020, status=COMPLETED, exitStatus=exitCode=COMPLETED;exitDescription=, job=[JobInstance: id=680, version=0, Job=[LIBRARY_FILE_TO_TMP_JOB]], jobParameters=[{version=8, -job.name=LIBRARY_FILE_TO_TMP_JOB}]
[2020-09-08 10:43:51.362] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.getTransaction:372] - Creating new transaction with name [org.springframework.batch.core.repository.support.SimpleJobRepository.update]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
[2020-09-08 10:43:51.362] DEBUG [o.s.o.j.JpaTransactionManager.doBegin:393] - Opened new EntityManager [SessionImpl(114211155<open>)] for JPA transaction

SELECT VERSION FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID=?;
SELECT COUNT(*) FROM BATCH_JOB_EXECUTION WHERE JOB_EXECUTION_ID = ?;
UPDATE BATCH_JOB_EXECUTION set START_TIME = ?, END_TIME = ?,  STATUS = ?, EXIT_CODE = ?, EXIT_MESSAGE = ?, VERSION = ?, CREATE_TIME = ?, LAST_UPDATED = ? where JOB_EXECUTION_ID = ? and VERSION = ?;

[2020-09-08 10:43:51.373] DEBUG [o.s.t.s.AbstractPlatformTransactionManager.processCommit:743] - Initiating transaction commit
[2020-09-08 10:43:51.373] DEBUG [o.s.o.j.JpaTransactionManager.doCommit:528] - Committing JPA transaction on EntityManager [SessionImpl(114211155<open>)]
[2020-09-08 10:43:51.377] DEBUG [o.s.o.j.JpaTransactionManager.doCleanupAfterCompletion:619] - Closing JPA EntityManager [SessionImpl(114211155<open>)] after transaction
[2020-09-08 10:43:51.377]  INFO [o.s.b.c.l.s.SimpleJobLauncher$1.run:145] - Job: [SimpleJob: [name=LIBRARY_FILE_TO_TMP_JOB]] completed with the following parameters: [{version=8, -job.name=LIBRARY_FILE_TO_TMP_JOB}] and the following status: [COMPLETED]
```
