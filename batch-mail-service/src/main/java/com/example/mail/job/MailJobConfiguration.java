package com.example.mail.job;


import com.example.mail.template.MailTemplateBuilder;
import kr.seok.common.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MailJobConfiguration {

    private static final String JOB_NAME = "mailJob";
    public static final int CHUNK_SIZE = 1000;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final EmailService emailService;
    private final MailTemplateBuilder mailTemplateBuilder;

    @Bean(value = JOB_NAME)
    public Job mailJob() {
        log.info("[LOG] [JOB] [START] {}", JOB_NAME);
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(dataReader())
                .build();
    }

    @Bean
    @JobScope
    public Step dataReader() {
        log.info("[LOG] [STEP] [START] {}", "mailStep");
        return stepBuilderFactory.get("mailStep")
                .chunk(CHUNK_SIZE)
//                .reader(mybatisReader())
//                .writer(mailSender())
                .build();
    }
//
//    @Bean
//    public JpaPagingItemReader<?> mybatisReader() {
//        log.info("[LOG] [READER] [START] {}", "mybatisReader");
//        String queryId = "com.example.jobs.mapper.UserMapper.findAllData";
//        return new MyBatisCursorItemReaderBuilder<>()
//                .sqlSessionFactory(sqlSessionFactory)
//                .queryId(queryId)
//                .build();
//    }
//
//    public ItemWriter<? super User> mailSender() {
//        log.info("[LOG] [WRITER] [START] {}", "mailSender");
//        return items -> {
//            String[] toRecipients = items.stream()
//                    .map(User::getEmail)
//                    .toArray(String[]::new);
//            String subject = "제목";
//            String templateName = "mail_template";
//            String template = mailTemplateBuilder.makeTemplate(templateName, items);
//            emailService.sendSimpleMessage(subject, template, toRecipients);
//            log.info("[LOG] [WRITER] [END] : SEND");
//        };
//    }
}
