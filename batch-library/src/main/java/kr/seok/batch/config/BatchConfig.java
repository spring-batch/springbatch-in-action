package kr.seok.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
/**
 * Enable Spring Batch features and provide a base configuration
 * JobRepository
 * JobLauncher
 * JobRegistry
 * JobExplorer
 * PlatformTransactionManager
 * JobBuilderFactory
 * JobBuilderFactory
 */
@EnableBatchProcessing
public class BatchConfig {
}
