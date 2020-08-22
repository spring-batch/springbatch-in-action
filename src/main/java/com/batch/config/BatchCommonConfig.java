package com.batch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

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
/* Default DataSource를 안쓰고 Custom으로 Multi DataSource를 사용하기 위한 설정 */
@ComponentScan(basePackageClasses = CustomBatchConfigurer.class)
@Configuration
public class BatchCommonConfig {
}
