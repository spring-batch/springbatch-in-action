package kr.seok.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
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
